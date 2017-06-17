package cz.dd4j.agents.monsters;

import cz.dd4j.agents.MonsterAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.IAStarView;
import cz.dd4j.utils.astar.Path;
import cz.dd4j.utils.config.Configurable;

public class KillerMonsterAgent extends MonsterAgentBase {

	@Configurable
	private double movementProbability;
	
	@Override
	public Command act() {
		if (random.nextDouble() > movementProbability) {
			return null;
		}
		
		AStar<Room> astar = new AStar<Room>(new IAStarHeuristic<Room>() {
			@Override
			public int getEstimate(Room n1, Room n2) {
				return 0;
			}
		});
		
		Room heroRoom = null;
		for (Room room : dungeon.rooms.values()) {
			if (room.hero != null) {
				heroRoom = room;
				break;
			}
		}
		
		if (heroRoom != null) {
			Path<Room> path = astar.findPath(monster.atRoom, heroRoom, new IAStarView<Room>() {
				@Override
				public boolean isOpened(Room node) {
					return !node.feature.isA(EFeature.TRAP);
				}
			});
			if (path != null && path.path.size() > 1) {
				return new Command(EAction.MOVE, path.path.get(1));
			}
		}
		
		return null;
	}

}
