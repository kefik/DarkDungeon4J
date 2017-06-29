package cz.dd4j.agents.heroes;

import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.Path;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Using rules for determining in what state it should enter the room.
 * 
 * Move actions are chosen according to the closest way to the goal, picking up the sward is preferred
 * to move to goal.
 * 
 * NEVER goes to the room, which contains unbeatable danger.
 * 
 * @author Martin
 */
public class HeroRulesWithCleverMove extends HeroAgentBase implements IHeroAgent {

	private Command moveIntention;
	private Dungeon myDungeon;
	private Room goalRoom;
	private boolean needSword;
	private long monsterCount;

	int distToClosestSword(Room from, List<Room> swordRooms, AStar<Room> astar) {
		 return swordRooms.stream().map(r -> astar.findPath(from, r)).
				 filter(Objects::nonNull).map(Path::getDistanceNodes).
				 min(Comparator.comparingInt(x -> x)).orElse(Integer.MAX_VALUE);
	}

	@Override
	public Command act() {
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.type == EItem.SWORD) return actions.attack();
		if (hero.atRoom.feature != null && hero.hand == null) return actions.disarm();
		if (moveIntention == null && hero.atRoom.item != null) return actions.pickup();

		needSword = monsterCount > 0 && (hero.hand == null || hero.hand.type != EItem.SWORD);
		List<Room> swordRooms = myDungeon.rooms.values().stream().filter(r -> r.item != null).collect(Collectors.toList());

		// ALL POSSIBLE MOVE ACTIONS
		List<Command> moveActions = actionsGenerator.generateFor(hero, EAction.MOVE);

		AStar<Room> astar = new AStar<>((n1, n2) -> 0);

		if (needSword) {
			moveActions.sort(Comparator.comparingInt(c -> distToClosestSword((Room) c.target, swordRooms, astar)));
		} else {
			moveActions.sort(Comparator.comparingInt(c -> astar.findPath((Room) c.target, goalRoom).getDistanceNodes()));
		}

		while (moveActions.size() > 0) {
			// NO MOVE INTENTION?
			if (moveIntention == null) {
				moveIntention = moveActions.remove(0);
			}

			// ASSESS MOVE INTENTION
			Room target = (Room)(moveIntention.target);

			// TRAP AT THE TARGET ROOM?
			if (target.feature != null) {
				// SOMETHING IN HANDS?
				if (hero.hand != null) {
					// DROP FIRST
					return actions.drop();
				}
			}
			
			// MONSTER AT THE TARGET ROOM?
			if (target.monster != null) {
				// AND NO SWORD?
				if (hero.hand == null) {
					// SWORD IN THE ROOM?
					if (hero.atRoom.item != null && hero.atRoom.item.isA(EItem.SWORD)) {
						return actions.pickup();
					} else {
						// NO SWORD TO PICKUP
						// => DO NOT GO
						moveIntention = null;
						// => TRY ANOTHER OPTION WHERE TO GO
						continue;
					}
				}
			}				
						
			// ALL GOOD, PROCEED
			Command moveAction = moveIntention;
			moveIntention = null;
			return moveAction;
		}
		
		// DUNNO WHAT TO DO...
		// => wait...
		return null;
	}

	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
		myDungeon = dungeon;

		if (goalRoom == null) {
			goalRoom = dungeon.rooms.values().stream().filter((r) -> r.isGoalRoom()).findFirst().get();
		}

		monsterCount = dungeon.rooms.values().stream().filter((r) -> r.monster != null).count();

	}


	}
