package cz.dd4j.agents.heroes;

import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.astar.AStar;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.astar.IAStarView;
import cz.dd4j.utils.astar.Path;

import java.util.*;

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

		int minDist = Integer.MAX_VALUE;
		for (Room r: swordRooms) {
			Path<Room> p = astar.findPath(from, r, new IAStarView() {
				@Override
				public boolean isOpened(Object o) {
					return ((Room) o).monster == null;
				}
			});
			if (p != null) {
				minDist = Math.min(minDist, p.getDistanceNodes());
			}
		}

		if (minDist == Integer.MAX_VALUE) { //monster block the way to all the swords, ignore them
			for (Room r : swordRooms) {
				Path<Room> p = astar.findPath(from, r);
				if (p != null) {
					minDist = Math.min(minDist, p.getDistanceNodes());
				}
			}
		}

		return minDist;
	}

	@Override
	public Command act() {
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.type == EItem.SWORD) return actions.attack();
		if (hero.atRoom.feature != null && hero.hand == null) return actions.disarm();
		if (moveIntention == null && hero.atRoom.item != null) return actions.pickup();

		needSword = monsterCount > 0 && (hero.hand == null || hero.hand.type != EItem.SWORD);
		final List<Room> swordRooms = new ArrayList<Room>();
		for (Room r: myDungeon.rooms.values()) {
			if (r.item != null) {
				swordRooms.add(r);
			}
		}

		// ALL POSSIBLE MOVE ACTIONS
		List<Command> moveActions = actionsGenerator.generateFor(hero, EAction.MOVE);


		final AStar<Room> aStar = new AStar<Room>(new IAStarHeuristic<Room>() {
			@Override
			public int getEstimate(Room n1, Room n2) {
				return 0;
			}
		});

		if (needSword) {
			Collections.sort(moveActions, new Comparator<Command>() {
				@Override
				public int compare(Command o1, Command o2) {
					int d1 = distToClosestSword((Room) o1.target, swordRooms, aStar);
					int d2 = distToClosestSword((Room) o2.target, swordRooms, aStar);
					return Integer.compare(d1, d2);
				}
			});
		} else {
			Collections.sort(moveActions, new Comparator<Command>() {
				@Override
				public int compare(Command o1, Command o2) {
					Path<Room> p1 = aStar.findPath((Room) o1.target, goalRoom);
					Path<Room> p2 = aStar.findPath((Room) o2.target, goalRoom);
					int d1 = p1 != null ? p1.getDistanceNodes() : Integer.MAX_VALUE;
					int d2 = p2 != null ? p2.getDistanceNodes() : Integer.MAX_VALUE;
					return Integer.compare(d1, d2);
				}
			});
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

		monsterCount = 0;
		for (Room r: dungeon.rooms.values()) {
			if (goalRoom == null) {
				if (r.isGoalRoom())
					goalRoom = r;
			}
			if (r.monster != null) {
				monsterCount++;
			}

		}

	}


	}
