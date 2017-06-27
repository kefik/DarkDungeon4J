package cz.dd4j.descriptor;

import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.IAStarView;
import cz.dd4j.utils.astar.Path;

public class DungeonPaths {
	
	public static class AStarNoHeuristic implements IAStarHeuristic<Room> {

		@Override
		public int getEstimate(Room n1, Room n2) {
			return 0;
		}
		
	}
	
	public static final AStarNoHeuristic ASTAR_NO_HEURISTIC = new AStarNoHeuristic();
	
	public static class AStarDefaultView implements IAStarView<Room> {

		@Override
		public boolean isOpened(Room node) {
			return true;
		}
		
	}
	
	public static final AStarDefaultView ASTAR_DEFAULT_VIEW = new AStarDefaultView();
	
	public static class AStarNoMonstersView implements IAStarView<Room> {

		@Override
		public boolean isOpened(Room node) {
			return node.monster == null;
		}
		
	}
	
	public static final AStarNoMonstersView ASTAR_NO_MONSTERS_VIEW = new AStarNoMonstersView();
	
	public static class AStarNoTrapsView implements IAStarView<Room> {

		@Override
		public boolean isOpened(Room node) {
			return node.feature == null || !node.feature.isA(EFeature.TRAP);
		}
		
	}
	
	public static final AStarNoTrapsView ASTAR_NO_TRAPS_VIEW = new AStarNoTrapsView();
	
	public static class AStarNoMonstersNoTrapsView implements IAStarView<Room> {

		@Override
		public boolean isOpened(Room node) {
			return node.monster == null && (node.feature == null || !node.feature.isA(EFeature.TRAP));
		}
		
	}
	
	public static final AStarNoMonstersNoTrapsView ASTAR_NO_MONSTERS_NO_TRAPS_VIEW = new AStarNoMonstersNoTrapsView();
	
	private IAStarHeuristic<Room> heuristic;
	private AStar<Room> astar;
	
	public DungeonPaths(Dungeon dungeon) {
		this(dungeon, new AStarNoHeuristic());
	}
	
	public DungeonPaths(Dungeon dungeon, IAStarHeuristic<Room> heuristic) {
		this.heuristic = heuristic;
		this.astar = new AStar<Room>(this.heuristic);
	}
	
	public boolean hasPath(Room start, Room end) {
		return hasPath(start, end, ASTAR_DEFAULT_VIEW);
	}
	
	public boolean hasPath(Room start, Room end, IAStarView<Room> view) {
		return findPath(start, end, view) != null;
	}
	
	public Path<Room> findPath(Room start, Room end) {
		return findPath(start, end, ASTAR_DEFAULT_VIEW);
	}

	public Path<Room> findPath(Room start, Room end, IAStarView<Room> view) {
		return astar.findPath(start, end, view);
	}

}
