package cz.dd4j.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.dd4j.domain.EItem;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.Path;
import cz.dd4j.utils.collection.IntMap;
import cz.dd4j.utils.csv.CSV.CSVRow;
import cz.dd4j.utils.reporting.IReporting;

public class DungeonDescriptor implements IReporting {

	/**
	 * How many rooms are within the dungeon.
	 */
	public int roomsCount;
	
	/**
	 * How many monsters are within the dungeon.
	 */
	public int monstersCount;
	
	/**
	 * How many features are within the dungeon.
	 */
	public int featuresCount;
	
	/**
	 * Rooms containing a monster.
	 */
	public Set<Room> monsterRooms;

	/**
	 * Rooms containing a feature.
	 */
	public Set<Room> featureRooms;

	/**
	 * Rooms containing a sword;
	 */
	public Set<Room> swordRooms;
	
	/**
	 * Minimum distance between the hero and the goal room, not accounting for traps and monsters.
	 */
	public int goalDistance;
	
	/**
	 * Minimum distance between the hero and the goal room, not accounting for traps.
	 */
	public int goalDistanceNonMonster;
	
	/**
	 * Negative value == no sword reachable sword in the at the beginning of the scenario; taking monsters into an account
	 */
	public int nearestSwordDistance;
	
	/**
	 * Lowest danger number from {@link #danger}.
	 */
	public int lowestDanger;
	
	/**
	 * KEY   == level (DANG function)
	 * VALUE == number of threats (the shortest paths between a monster and a hero)
	 */
	public IntMap<Integer> danger = new IntMap<Integer>();

	public static DungeonDescriptor describe(DungeonXML dungeonXML, IAStarHeuristic<Room> heuristic) {
		DungeonLoaderXML loader = new DungeonLoaderXML();
		Dungeon dungeon = loader.loadDungeon(dungeonXML);
		return describe(dungeon, heuristic);
	}
	
	public static DungeonDescriptor describe(Dungeon dungeon, IAStarHeuristic<Room> heuristic) {
		DungeonDescriptor result = new DungeonDescriptor();
		describe(dungeon, heuristic, result);
		return result;
	}
		
	public static void describe(Dungeon dungeon, IAStarHeuristic<Room> heuristic, DungeonDescriptor result) {
		if (result == null) {
			throw new RuntimeException("result == null, invalid, initialize it by yourself first!");
		}
				
		// CHECK THE DUNGEON
		Room heroRoom = null;		
		Room goalRoom = null;		
		result.monsterRooms = new HashSet<Room>();
		result.featureRooms = new HashSet<Room>();
		result.swordRooms = new HashSet<Room>();
		
		for (Room room : dungeon.rooms.values()) {
			if (room.isGoalRoom()) goalRoom = room;
			if (room.hero != null) heroRoom = room;
			if (room.monster != null) result.monsterRooms.add(room);
			if (room.feature != null) result.featureRooms.add(room);
			if (room.item != null && room.item.isA(EItem.SWORD)) result.swordRooms.add(room);
		}
		
		// ROOMS COUNT
		result.roomsCount = dungeon.rooms.size();
		result.monstersCount = result.monsterRooms.size();
		result.featuresCount = result.featureRooms.size(); 
		
		// CONSTRUCT PATHS
		DungeonPaths paths = new DungeonPaths(dungeon);
		
		Path<Room> path = null;
		
		// FIND MINIMAL PATH TO GOAL
		path = paths.findPath(heroRoom, goalRoom);
		
		if (path == null) {
			throw new RuntimeException("Invalid dungeon, hero cannot reach the goal.");
		}
		
		result.goalDistance = path.getDistanceNodes();
		
		path = paths.findPath(heroRoom, goalRoom, DungeonPaths.ASTAR_NO_MONSTERS_VIEW);
		if (path == null) {
			result.goalDistanceNonMonster = -1;
		} else {
			result.goalDistanceNonMonster = path.getDistanceNodes();
		}
		
		// FIND DANGEROUSNESS
		result.lowestDanger = -1;
		
		for (Room monsterRoom : result.monsterRooms) {
			path = paths.findPath(monsterRoom, heroRoom, DungeonPaths.ASTAR_NO_TRAPS_VIEW);
			if (path == null) continue;
			result.danger.inc(path.getDistanceNodes());
			if (result.lowestDanger < 0 || result.lowestDanger > path.getDistanceNodes()) result.lowestDanger = path.getDistanceNodes();
		}
		
		// FIND THE NEAREST SWORD		
		result.nearestSwordDistance = -1;
		
		for (Room swordRoom : result.swordRooms) {
			path = paths.findPath(heroRoom, swordRoom, DungeonPaths.ASTAR_NO_MONSTERS_VIEW);
			if (path == null) continue;
			if (result.nearestSwordDistance < 0 || result.nearestSwordDistance > path.getDistanceNodes()) result.nearestSwordDistance = path.getDistanceNodes();
		}
		
		// WE'RE DONE
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		
		result.append("DD[");
		
		result.append("goalIn=" + goalDistance + ", goalSafeIn=" + goalDistanceNonMonster + ", swordSafeIn=" + nearestSwordDistance + ", dang=" + lowestDanger + ", dangs=");
		
		if (danger.size() == 0) {
			result.append("N/A");
		} else {
			List<Integer> keys = new ArrayList<Integer>();
			keys.addAll(danger.keySet());
			Collections.sort(keys);
			
			boolean first = true;
			for (Integer key : keys) {
				if (first) first = false;
				else result.append(",");
				result.append(key);
				result.append("=>");
				result.append(danger.get(key));
			}
		}
		
		result.append("]");
		
		return super.toString();
	}
	
	// =========
	// REPORTING
	// =========
	
	public static final String CSV_ROOMS_COUNT = "DESC-rooms_count";
	public static final String CSV_MONSTERS_COUNT = "DESC-monsters_count";
	public static final String CSV_FEATURES_COUNT = "DESC-features_count";	
	public static final String CSV_GOAL_DISTANCE = "DESC-goal_distance";
	public static final String CSV_GOAL_DISTANCE_NON_MONSTER = "DESC-goal_distance_non_monster";
	public static final String CSV_NEAREST_SWORD_DISTANCE = "DESC-nearest_sword_distance";
	public static final String CSV_INITIAL_DANGER = "DESC-initial_danger";

	@Override
	public List<String> getCSVHeaders() {
		List<String> result = new ArrayList<String>();
		
		result.add(CSV_ROOMS_COUNT);
		result.add(CSV_MONSTERS_COUNT);
		result.add(CSV_FEATURES_COUNT);
		
		result.add(CSV_GOAL_DISTANCE);
		result.add(CSV_GOAL_DISTANCE_NON_MONSTER);
		result.add(CSV_NEAREST_SWORD_DISTANCE);
		result.add(CSV_INITIAL_DANGER);

		return result;
	}

	@Override
	public CSVRow getCSVRow() {
		CSVRow result = new CSVRow();
		
		result.add(CSV_ROOMS_COUNT, roomsCount);
		result.add(CSV_MONSTERS_COUNT, monstersCount);
		result.add(CSV_FEATURES_COUNT, featuresCount);
		
		result.add(CSV_GOAL_DISTANCE, goalDistance);
		result.add(CSV_GOAL_DISTANCE_NON_MONSTER, goalDistanceNonMonster);
		result.add(CSV_NEAREST_SWORD_DISTANCE, nearestSwordDistance);
		result.add(CSV_INITIAL_DANGER, lowestDanger);
		
		return result;
	}
	
}
