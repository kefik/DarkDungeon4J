package cz.dd4j.agents.heroes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

/**
 * Using rules for determining in what state it should enter the room.
 * 
 * Move actions are chosen at random {@link HeroRandom#generateActions(Hero)}.
 * 
 * NEVER goes to the room, which contains unbeatable danger.
 * 
 * @author Jimmy
 */
public class HeroRulesWithRandomMove implements IHeroAgent {

	private Random random;
	
	private Hero hero;

	private Action moveIntention;
	
	public HeroRulesWithRandomMove() {
		random = new Random();
	}
	
	public HeroRulesWithRandomMove(long seed) {
		random = new Random(seed);
	}
	
	@Override
	public void observeBody(Hero hero, long timestampMillis) {
		this.hero = hero;
	}

	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
	}

	@Override
	public Action act() {
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.type == EItem.SWORD) return new Action(EAction.ATTACK, hero.atRoom.monster);
		if (hero.atRoom.feature != null && hero.hand == null) return new Action(EAction.DISARM, hero.atRoom.feature);		
		if (hero.atRoom.item != null) return new Action(EAction.PICKUP, hero.atRoom.item);
		
		if (moveIntention == null) {
			List<Action> actions = generateMoves(hero);		
			moveIntention = actions.get(random.nextInt(actions.size())); 
		}
		
		if (moveIntention != null) {
			Room target = (Room)(moveIntention.target);
			// TRAP AT THE ROOM?
			if (target.feature != null && hero.hand != null) {
				// unequip first
				return new Action(EAction.EQUIP, null);
			}
			// MONSTER AT THE ROOM?
			if (target.monster != null && hero.hand == null) {
				// equip sword first...
				return new Action(EAction.EQUIP, null, hero.inventory.get(EItem.SWORD));
			}
			if (target.monster != null && hero.hand.type != EItem.SWORD) {
				// unequip non-sword...
				return new Action(EAction.EQUIP, null);
			}
			
			// ALL GOOD! PROCEED!
			Action move = moveIntention;
			moveIntention = null;
			return move;
		} 
		
		// DUNNO WHAT TO DO...
		// => wait...
		return null;
	}
	
	private boolean hasSwordInInventory() {
		return hero.inventory.has(EItem.SWORD);
	}

	private List<Action> generateMoves(Hero hero) {
		List<Action> result = new ArrayList<Action>();
		
		// MOVE ACTIONS
		for (Corridor corridor : hero.atRoom.corridors) {
			Room otherRoom = corridor.getOtherRoom(hero.atRoom);
			if (otherRoom.monster != null && otherRoom.feature != null) {
				// unsolvable situation...
				continue;
			}
			if (otherRoom.monster != null && (hero.hand == null || hero.hand.type != EItem.SWORD) && !hasSwordInInventory()) continue;
			result.add(new Action(EAction.MOVE, corridor.getOtherRoom(hero.atRoom)));
		}
		
		return result;
	}

}
