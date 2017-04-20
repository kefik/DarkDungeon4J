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
			firstObserve = false;
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
			if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
				roomsWithSword.add(room);		
			}
		}
	}
	
	protected void preparePDDLStatic() {
		pddlStaticPartCache.setLength(0);
		
		StringBuffer sb = pddlStaticPartCache;
		
		//(define (problem p1) 
		sb.append("(define (problem p1)");
		sb.append(pddlNewLine);
		
		//(:domain DarkDungeon)
		sb.append("(:domain " + domainName + ")");
		sb.append(pddlNewLine);
		sb.append(pddlNewLine);
		
		//(:objects r1 r2 r3 r4 - room
        //          s - sword)
		sb.append("(:objects");
		for (Room room : dungeon.rooms.values()) {
			sb.append(" ");
			sb.append(room.id.name);
		}
		sb.append(" - room");
		sb.append(pddlNewLine);
		
		sb.append("          ");
		boolean atLeastOneSword = false;
		for (Room room : roomsWithSword) {
			if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
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
		sb.append(pddlNewLine);
		sb.append(pddlNewLine);
				
		//(:init
		sb.append("(:init");
		sb.append(pddlNewLine);
		sb.append(pddlNewLine);
		
		// GRAPH
		//    (connected r1 r2)
		//    ...
		
		for (Room room : dungeon.rooms.values()) {
			for (Corridor corridor : room.corridors) {
				Room other = corridor.getOtherRoom(room);
				sb.append("    (connected " + room.id.name + " " + other.id.name + ")");
				sb.append(pddlNewLine);
			}
		}
	}
	
	protected void processDungeonUpdate(Dungeon dungeon) {
		roomsWithSword.clear();
		for (Room room : dungeon.rooms.values()) {
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
		if (problemFile == null) {
			problemFile = getWorkingFile("problem.pddl");
		}
		FileOutputStream outStream = null;
		PrintWriter print = null;
		try {
			 outStream = new FileOutputStream(problemFile);
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
				problemFile.delete();
			} catch (Exception e3) {
			}
			throw new RuntimeException("Failed to generate PDDL problem file into: " + problemFile.getAbsolutePath(), e);
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
		return problemFile;
	}

	protected void generateProblem(PrintWriter print) throws IOException {
		//(define (problem p1) 
		print.print(pddlStaticPartCache.toString());
		print.print(pddlNewLine);
				
		//    (alive)
		if (hero.alive) { 
			print.print("    (alive)");
			print.print(pddlNewLine);
		}
		
		//    (has_sword)
		if (hero.hand != null && EH.isA(hero.hand.type, EItem.SWORD)) {
			print.print("    (has_sword)");
			print.print(pddlNewLine);
		}
		
		// ENTITIES
		//    (monster_at r2)
   	    //    (trap_at r3)
   	    //    (sword_at r1)
        //    (hero_at r1)
		
		// monsters...
		for (Monster monster : monsters.values()) {
			if (monster.alive && monster.atRoom != null) {
				print.print("    (monster_at " + monster.atRoom.id.name + ")");
				print.print(pddlNewLine);
			}
		}
		// traps
		for (Feature feature : features.values()) {
			if (feature.alive && EH.isA(feature.type, EFeature.TRAP) && feature.atRoom != null) {
				print.print("    (trap_at " + feature.atRoom.id.name + ")");
				print.print(pddlNewLine);
			}
		}
		// swords
		for (Room room : roomsWithSword) {
			print.print("    (sword_at " + room.id.name + ")");
			print.print(pddlNewLine);
		}
		// hero
		if (hero.atRoom != null) {
			print.print("    (hero_at " + hero.atRoom.id.name + ")");
			print.print(pddlNewLine);
		} else {
			throw new RuntimeException("hero.atRoom is null, invalid");
		}
		
		//)
		print.print(")");
		print.print(pddlNewLine);
		
		print.print("");
		print.print(pddlNewLine);

		//(:goal (and (alive)(hero_at r4)))
		if (goalRooms.size() > 0) {
			print.print("(:goal (and (alive)(hero_at " + goalRooms.get(0).id.name + ")))");
			print.print(pddlNewLine);
		} else {
			throw new RuntimeException("goalRooms.size() == 0, invalid");
		}
		
		print.print("");
		print.print(pddlNewLine);
		//)
		print.print(")");
		print.print(pddlNewLine);
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
			if (line.endsWith("\r")) line = line.substring(0, line.length()-1);
			PDDLAction action = PDDLAction.parseSOL(line);
			if (action != null) {
				result.add(action);
			}
		}	

		return result;
	}

}
