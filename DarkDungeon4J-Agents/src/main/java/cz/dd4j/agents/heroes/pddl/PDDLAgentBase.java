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
public abstract class PDDLAgentBase extends HeroAgentBase {
	
	// =============
	// CONFIGURATION
	// =============
	
	@Configurable
	protected File domainFile = new File("./DarkDungeon.pddl");
	
	@Configurable
	protected String domainName = "DarkDungeon";
	
	@Configurable
	protected File agentWorkingDirBase = new File("./temp");
		
	// ============
	// PDDL RUNTIME
	// ============
	
	/**
	 * Used to generate temporary files worked with by the agent...
	 */
	protected UUID uuid;
	
	protected File agentWorkingDir;
	
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
			preparePDDLStatic();
		} else {
			processDungeonUpdate(dungeon);
		}		
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
			if (room.item != null && EH.isA(room.item, EItem.SWORD)) {
				roomsWithSword.add(room);		
			}
		}
	}
	
	protected void preparePDDLStatic() {
		pddlStaticPartCache.setLength(0);
		
		StringBuffer sb = pddlStaticPartCache;
		
		//(define (problem p1) 
		sb.append("(define (problem p1)");
		sb.append(Const.NEW_LINE);
		
		//(:domain DarkDungeon)
		sb.append("(define (" + domainName + " p1)");
		sb.append(Const.NEW_LINE);
		sb.append(Const.NEW_LINE);
		
		//(:objects r1 r2 r3 r4 - room
        //          s - sword)
		sb.append("(:objects");
		for (Room room : dungeon.rooms.values()) {
			sb.append(" ");
			sb.append(room.id.name);
		}
		sb.append(" - room");
		sb.append(Const.NEW_LINE);
		
		sb.append("          ");
		boolean atLeastOneSword = false;
		for (Room room : dungeon.rooms.values()) {
			if (room.hero != null && room.hero.hand != null && EH.isA(room.hero.hand.type, EItem.SWORD)) {
				sb.append(" ");
				sb.append(room.hero.hand.id.name);
				atLeastOneSword = true;
			}
			if (room.item != null && EH.isA(room.item, EItem.SWORD)) {
				roomsWithSword.add(room);
				sb.append(" ");
				sb.append(room.item.id.name);
				atLeastOneSword = true;
			}			
		}
		if (atLeastOneSword) {
			sb.append(" - sword)");
		} else {
			sb.append(")");			
		}
		sb.append(Const.NEW_LINE);
		sb.append(Const.NEW_LINE);
				
		//(:init
		sb.append("(:init");
		sb.append(Const.NEW_LINE);
		sb.append(Const.NEW_LINE);
		
		// GRAPH
		//    (connected r1 r2)
		//    ...
		
		for (Room room : dungeon.rooms.values()) {
			for (Corridor corridor : room.corridors) {
				Room other = corridor.getOtherRoom(room);
				sb.append("    (connected " + room.id.name + " " + other.id.name + ")");
				sb.append(Const.NEW_LINE);
			}
		}
	}
	
	protected void processDungeonUpdate(Dungeon dungeon) {
		roomsWithSword.clear();
		for (Room room : dungeon.rooms.values()) {
			if (room.item != null && EH.isA(room.item, EItem.SWORD)) {
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
			return null;
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
		// GENERATE PROBLEM FILE
		File problemFile = generateProblemFile();
		if (problemFile == null) {
			throw new RuntimeException("Failed to genereate problem file!");
		}
		
		String plannerResult = null;
		try {
			// we have domainFile and problemFile
			// => EXECUTE THE PLANNER			
			return execPlanner(domainFile, problemFile);
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
	protected abstract List<PDDLAction> execPlanner(File domainFile, File problemFile) throws Exception;
	
	// =======================
	// PDDL PROBLEM GENERATION
	// =======================

	protected File generateProblemFile() {
		File targetFile = getWorkingFile("problem.pddl");
		FileOutputStream outStream = null;
		PrintWriter print = null;
		try {
			 outStream = new FileOutputStream(targetFile);
			 print = new PrintWriter(outStream);
			 generateProblem(print);
		} catch (Exception e) {
			if (print != null) {
				try {
					print.close();
				} catch (Exception e1) {
					print = null;
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e2) {
					outStream = null;
				}
			}
			try {
				targetFile.delete();
			} catch (Exception e3) {
			}
			throw new RuntimeException("Failed to generate PDDL problem file into: " + targetFile.getAbsolutePath(), e);
		} finally {
			if (print != null) {
				try {
					print.close();
				} catch (Exception e) {
					print = null;
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e) {
					outStream = null;
				}
			}
		}
		return targetFile;
	}

	protected void generateProblem(PrintWriter print) throws IOException {
		//(define (problem p1) 
		print.println(pddlStaticPartCache.toString());
				
		//    (alive)
		if (hero.alive) { 
			print.println("    (alive)");
		}
		
		//    (has_sword)
		if (hero.hand != null && EH.isA(hero.hand, EItem.SWORD)) {
			print.println("    (has_sword)");
		}
		
		// ENTITIES
		//    (monster_at r2)
   	    //    (trap_at r3)
   	    //    (sword_at r1)
        //    (hero_at r1)
		
		// monsters...
		for (Monster monster : monsters.values()) {
			if (monster.alive && monster.atRoom != null) {
				print.println("    (monster_at " + monster.atRoom.id.name + ")");
			}
		}
		// traps
		for (Feature feature : features.values()) {
			if (feature.alive && EH.isA(feature.type, EFeature.TRAP) && feature.atRoom != null) {
				print.println("    (trap_at " + feature.atRoom.id.name + ")");
			}
		}
		// swords
		for (Room room : roomsWithSword) {
			print.println("    (sword_at " + room.id.name + ")");
		}
		// hero
		if (hero.atRoom != null) {
			print.println("    (hero_at " + hero.atRoom.id.name + ")");
		} else {
			throw new RuntimeException("hero.atRoom is null, invalid");
		}
		
		//)
		print.println(")");
		
		print.println("");

		//(:goal (and (alive)(hero_at r4)))
		if (goalRooms.size() > 0) {
			print.println("(:goal (and (alive)(hero_at " + goalRooms.get(0).id.name + ")))");
		} else {
			throw new RuntimeException("goalRooms.size() == 0, invalid");
		}
		
		print.println("");
		//)
		print.println(")");
	}
	
	// =====
	// UTILS
	// =====
	
	protected File getWorkingFile(String name) {
		return new File(agentWorkingDir, name);
	}
	
	protected List<PDDLAction> parseLines(String lines) {
		if (lines == null || lines.trim().length() == 0) {
			return null;
		}
		
		String[] parts = lines.split("\n");
		List<PDDLAction> result = new ArrayList<PDDLAction>(parts.length);
		for (String line : parts) {
			PDDLAction action = PDDLAction.parseSOL(line);
			if (action != null) {
				result.add(action);
			}
		}	

		return result;
	}

}
