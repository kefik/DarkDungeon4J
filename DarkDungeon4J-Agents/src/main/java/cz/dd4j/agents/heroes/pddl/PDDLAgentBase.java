package cz.dd4j.agents.heroes.pddl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import cz.dd4j.agents.heroes.planners.*;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarView;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

@AutoConfig
public class PDDLAgentBase extends HeroAgentBase {
	
	// =============
	// CONFIGURATION
	// =============
	
	@Configurable
	protected File domainFile = new File("./DarkDungeon.pddl");
	
	@Configurable
	protected String domainName = "DarkDungeon";
	
	@Configurable
	protected File agentWorkingDirBase = new File("./temp");

	@Configurable
	protected AbstractPlannerExecutor executor = new NPlanCygwinExecutor();

	@Configurable
	protected String executorClass = "cz.dd4j.agents.heroes.planners.NPlanNativeExecutor";

	// ============
	// PDDL RUNTIME
	// ============
	
	/**
	 * Used to generate temporary files worked with by the agent...
	 */
	protected UUID uuid;
	
	protected File agentWorkingDir;
	
	protected File problemFile;
	
	protected boolean workingDirExisted = true;

	// ====================
	// DARK DUNGEON RUNTIME
	// ====================
	
	protected Dungeon dungeon;
	
	protected boolean firstObserve = true;
	
	protected Map<Id, Monster> monsters = new HashMap<Id, Monster>();
	protected Map<Id, Feature> features = new HashMap<Id, Feature>();
	protected List<Room> roomsWithSword = new ArrayList<Room>();
	protected List<Room> goalRooms = new ArrayList<Room>();
	
	protected StringBuffer pddlStaticPartCache = new StringBuffer();
	
	protected String pddlNewLine = Const.NEW_LINE;

	protected PDDLInputGenerator inputGenerator;

	// ================
	// AGENT LIFE-CYCLE
	// ================
	
	@Override
	public void prepareAgent() {
		super.prepareAgent();
				
		uuid = UUID.randomUUID();
		
		agentWorkingDir = new File(agentWorkingDirBase, uuid.toString());
		agentWorkingDir.deleteOnExit();
		
		if (!agentWorkingDir.exists()) {
			workingDirExisted = false;
			agentWorkingDir.mkdirs();
		}

		try {
			executor = (AbstractPlannerExecutor)Class.forName(executorClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		inputGenerator = new PDDLInputGenerator(agentWorkingDir);
		executor.prepareEnvironment(agentWorkingDir);
		problemFile = executor.getProblemFile();
		inputGenerator.setPddlNewLine(executor.getPddlNewLine());

		reset();
	}
	
	@Override
	public void simulationEnded() {
		super.simulationEnded();
		if (!workingDirExisted) {
			agentWorkingDir.delete();
		}
		
		reset();
	}
	
	protected void reset() {
		monsters.clear();
		features.clear();
		roomsWithSword.clear();
		goalRooms.clear();
		
		firstObserve = true;
		
		pddlStaticPartCache.setLength(0);
	}
	
	// ==================
	// SENSOMOTORIC CYCLE
	// ==================
	
	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
		this.dungeon = dungeon;
		if (firstObserve) {
			processDungeonFull(dungeon);
			inputGenerator.prepareStaticPart(dungeon, domainName, roomsWithSword);
			firstObserve = false;
		} else {
			processDungeonUpdate(dungeon);
		}		
	}

	protected int dang(Room r) {

		if (r.feature != null && r.feature.type == EFeature.TRAP) {
			if (hero.hand != null && hero.hand.type == EItem.SWORD) //in a room with trap with sword -> dead end
				return 0;
			else
				return 1;
		}

		if (hero.hand != null && hero.hand.type == EItem.SWORD) { // have sword -> safe
			return Integer.MAX_VALUE;
		}

		List<Room> monsters = dungeon.rooms.values().stream().filter((room) -> room.monster != null).collect(Collectors.toList());
		if (monsters.size() == 0) { // no monsters -> safe
			return Integer.MAX_VALUE;
		}

		AStar<Room> astar = new AStar<>((r1, r2) -> 0);
		int min = Integer.MAX_VALUE;
		for (Room mr : monsters) {
			int dist = astar.findPath(mr, r, room -> ((Room) room).feature != null || !((Room) room).feature.isA(EFeature.TRAP)).getDistanceNodes();
			min = Math.min(min, dist);
		}

		return min;
	}

	protected void processDungeonFull(Dungeon dungeon) {
		monsters.clear();
		features.clear();
		goalRooms.clear();
		roomsWithSword.clear();
		
		for (Room room : dungeon.rooms.values()) {
			if (room.monster != null) {
				monsters.put(room.monster.id, room.monster);
			}
			if (room.feature != null) {
				features.put(room.feature.id, room.feature);
			}
			if (room.isGoalRoom()) goalRooms.add(room);
			if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
				roomsWithSword.add(room);		
			}
		}
	}

	protected void processDungeonUpdate(Dungeon dungeon) {
		roomsWithSword.clear();
		for (Room room : dungeon.rooms.values()) {
			if (room.monster != null) {
				monsters.put(room.monster.id, room.monster);
			}
			if (room.feature != null) {
				features.put(room.feature.id, room.feature);
			}
			if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
				roomsWithSword.add(room);		
			}
		}
	}

	@Override
	public Command act() {
		// INVOKE PLANNER
		List<PDDLAction> plan = plan();
				
		// IF NO ACTION => NO ACTION
		if (plan == null || plan.size() == 0) return null;
		
		// EXECUTE FIRST ACTION FROM THE PLAN
		return translateAction(plan.get(0));
	} 
	
	// =====================
	// PDDLAction -> Command
	// =====================
	
	protected Command translateAction(PDDLAction action) {
		Command result = null;
		
		// TRANSLATE ACTION
		if (action.action == EAction.MOVE) {
			Room room1 = dungeon.rooms.get(action.arg1);
			Room room2 = dungeon.rooms.get(action.arg2);
			if (hero.atRoom == room1) result = actions.move(room2);
			if (hero.atRoom == room2) result = actions.move(room1);
			return result;
		} else {
			result = actions.action(action.action);
		}
		
		// VALIDATE ACTION
		if (actionValidator.isValid(hero, result)) {
			return result;
		}
		
		// INVALID ACTION
		return null;
	}
	
	// ========
	// PLANNING
    // ========

	protected List<PDDLAction> plan() {
		return plan(null);
	}

	protected List<PDDLAction> plan(String goal) {
		// GENERATE PROBLEM FILE
		InputFiles inputs;
		if (goal == null) { //no special goal, plan to goal rooms
			inputs = inputGenerator.generateFiles(hero, monsters, features, roomsWithSword, goalRooms, problemFile, domainFile);
		} else
			inputs = inputGenerator.generateFiles(hero, monsters, features, roomsWithSword, goal, problemFile, domainFile);
		if (inputs.problemFile == null) {
			throw new RuntimeException("Failed to genereate problem file!");
		}

		try {
			// we have domainFile and problemFile
			// => EXECUTE THE PLANNER			
			return execPlanner(inputs.domainFile, inputs.problemFile);
		} catch (Exception e) {
			throw new RuntimeException("Failed to execute the planner.", e);
		} finally {
			try {
				problemFile.delete();
			} catch (Exception e) {				
			}
		}
	}
	
	/**
	 * 
	 * @param domainFile
	 * @param problemFile
	 * @return
	 */
	protected List<PDDLAction> execPlanner(File domainFile, File problemFile) throws Exception {
		return executor.execPlanner(domainFile, problemFile);
	}

	// =====
	// UTILS
	// =====
	
	protected File getWorkingFile(String name) {
		return new File(agentWorkingDir, name);
	}

}
