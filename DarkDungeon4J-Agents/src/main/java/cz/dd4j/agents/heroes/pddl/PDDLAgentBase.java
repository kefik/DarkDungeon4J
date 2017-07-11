package cz.dd4j.agents.heroes.pddl;

import java.io.File;
import java.net.Inet4Address;
import java.util.*;

import cz.dd4j.agents.heroes.planners.*;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.IAStarView;
import cz.dd4j.utils.astar.Path;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.ConfigXML;
import cz.dd4j.utils.config.Configurable;
import cz.dd4j.utils.csv.CSV;
import org.apache.commons.io.FileUtils;

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

	protected int plannerCalls = 0;
	protected int failedPlans = 0;
	protected int customPlannerCalls = 0;

	// ================
	// AGENT LIFE-CYCLE
	// ================
	
	@Override
	public void prepareAgent() {
		super.prepareAgent();
				
		uuid = UUID.randomUUID();
		
		agentWorkingDir = new File(agentWorkingDirBase, uuid.toString());

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
		FileUtils.deleteQuietly(agentWorkingDir);
		
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

		if (r.monster != null && hero.hand == null)
			return 0;

		if (r.feature != null) {
			if (hero.hand != null) //in a room with trap with sword -> dead end
				return 0;
			else
				return 1;
		}

		if (hero.hand != null) { // have sword -> safe
			return Integer.MAX_VALUE;
		}

		return getClosestMonsterDistance(r);
	}

	protected int dangAfterAction(Command cmd) {
		// room with trap, anything except disarm is dead-end, disarm is distance to closest monster - 1 (monster can move)
		if (hero.atRoom.feature != null) {
			if (cmd.isType(EAction.DISARM)) {
				return Math.max(1, getClosestMonsterDistance(hero.atRoom) - 1);
			}
			else {
				return 0;
			}
		}

		// room with monster
		if (hero.atRoom.monster != null) {
			if (hero.hand == null) {
				return 0;
			}
			Monster m = hero.atRoom.monster;
			hero.atRoom.monster = null;
			int val = dangAfterAction(cmd);
			hero.atRoom.monster = m;
			return val;
		}

		//for moves, the value is the distance to closest monster from the target room - 1 (the monsters can move)
		if (cmd.isType(EAction.MOVE)) {
			Room target = dungeon.rooms.get(cmd.target.id);
			if (target.monster != null) {
				return hero.hand != null ? Integer.MAX_VALUE : 0;
			}
			if (target.feature != null) {
				return hero.hand != null ? 0 : 1;
			}
			return hero.hand != null ? Integer.MAX_VALUE : getClosestMonsterDistance(target) - 1;
		}

		//for sword dropping, the value is the distance to closest monster from the current room - 1 (monsters move)
		if (cmd.isType(EAction.DROP)) {
			return getClosestMonsterDistance(hero.atRoom) - 1;
		}

		//after picking up sword (in a room without trap), the hero is safe
		if (cmd.isType(EAction.PICKUP)) {
			return Integer.MAX_VALUE;
		}

		return 0;
	}

	protected int getClosestMonsterDistance(Room r) {

		int minDist = Integer.MAX_VALUE;

		AStar<Room> astar = new AStar<Room>(new IAStarHeuristic<Room>() {
			@Override
			public int getEstimate(Room n1, Room n2) {
				return 0;
			}
		});

		for (Room room: dungeon.rooms.values()) {
			if (room.monster != null) {
				Path<Room> path = astar.findPath(room, r, new IAStarView() {
					@Override
					public boolean isOpened(Object o) {
						return ((Room) o).feature == null || !((Room) o).feature.isA(EFeature.TRAP);
					}
				});
				if (path != null) {
					minDist = Math.min(minDist, path.getDistanceNodes());
				}
			}
		}

		return minDist;
	}

	protected Monster getClosestMonster(Room r) {

		int minDist = Integer.MAX_VALUE;
		Monster closest = null;

		AStar<Room> astar = new AStar<Room>(new IAStarHeuristic<Room>() {
			@Override
			public int getEstimate(Room n1, Room n2) {
				return 0;
			}
		});

		for (Room room: dungeon.rooms.values()) {
			if (room.monster != null) {
				Path<Room> path = astar.findPath(room, r, new IAStarView() {
					@Override
					public boolean isOpened(Object o) {
						return ((Room) o).feature == null || !((Room) o).feature.isA(EFeature.TRAP);
					}
				});
				if (path != null) {
					int dist = path.getDistanceNodes();
					if (dist < minDist) {
						minDist = dist;
						closest = room.monster;
					}
				}
			}
		}

		return closest;

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
		} else {
			customPlannerCalls++;
			inputs = inputGenerator.generateFiles(hero, monsters, features, roomsWithSword, goal, problemFile, domainFile);
		}
		if (inputs.problemFile == null) {
			throw new RuntimeException("Failed to genereate problem file!");
		}

		try {
			// we have domainFile and problemFile
			// => EXECUTE THE PLANNER
			plannerCalls++;
			List<PDDLAction> plan = execPlanner(inputs.domainFile, inputs.problemFile);
			if (plan == null) {
				failedPlans++;
			}

			return plan;
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

	@Override
	public List<String> getCSVHeaders() {
		List<String> headers = super.getCSVHeaders();
		headers.add("planner_calls");
		headers.add("planner_fails");
		return headers;
	}

	@Override
	public CSV.CSVRow getCSVRow() {
		CSV.CSVRow row = super.getCSVRow();
		row.add("planner_calls", Integer.toString(plannerCalls));
		row.add("planner_fails", Integer.toString(failedPlans));
		row.add("custom_planner_calls", Integer.toString(customPlannerCalls));
		return row;
	}

}
