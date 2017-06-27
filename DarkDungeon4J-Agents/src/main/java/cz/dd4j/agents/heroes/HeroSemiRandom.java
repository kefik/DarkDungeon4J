package cz.dd4j.agents.heroes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

/**
 * Semi-random-valid-action hero.
 * 
 * Chooses random action from the set of available random actions as generated by {@link HeroRandom#generateActions(Hero)}.
 * 
 * NEVER goes to the room, which contains danger.
 * 
 * @author Jimmy
 */
public class HeroSemiRandom extends HeroAgentBase {

	public HeroSemiRandom() {
		random = new Random();
	}
		
	@Override
	public Command act() {
		List<Command> actions = generateActions(hero);	
		if (actions.size() == 0) return null;
		return getRandomAction(actions, true);
	}
	
	private List<Command> generateActions(Hero hero) {
		List<Command> result = new ArrayList<Command>();
		
		// MOVE ACTIONS
		for (Corridor corridor : hero.atRoom.corridors) {
			Room otherRoom = corridor.getOtherRoom(hero.atRoom);
			if (otherRoom.monster != null && (hero.hand == null || hero.hand.type != EItem.SWORD)) continue;
			if (otherRoom.feature != null && otherRoom.feature.type == EFeature.TRAP && hero.hand != null) continue;
			result.add(actions.move(corridor.getOtherRoom(hero.atRoom)));
		}
		
		// ATTACK ACTIONS
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.type == EItem.SWORD) {
			result.add(actions.attack());
		}
		
		// DISARM ACTIONS
		if (hero.hand == null && hero.atRoom.feature != null && hero.atRoom.feature.type == EFeature.TRAP) {
			result.add(actions.disarm());
		}
				
		// DROP
		if (hero.hand != null) {
			result.add(actions.drop());
		}
		
		// PICKUP
		if (hero.atRoom.item != null) {
			result.add(actions.pickup());
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
