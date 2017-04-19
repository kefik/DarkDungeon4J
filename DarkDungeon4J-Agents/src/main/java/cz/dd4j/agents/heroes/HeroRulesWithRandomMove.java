package cz.dd4j.agents.heroes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.dd4j.agents.HeroAgentBase;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
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
public class HeroRulesWithRandomMove extends HeroAgentBase implements IHeroAgent {

	private Command moveIntention;
	
	@Override
	public Command act() {
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.type == EItem.SWORD) return new Command(EAction.ATTACK, hero.atRoom.monster);
		if (hero.atRoom.feature != null && hero.hand == null) return new Command(EAction.DISARM, hero.atRoom.feature);		
		if (hero.atRoom.item != null) return new Command(EAction.PICKUP, hero.atRoom.item);
		
		// ALL POSSIBLE MOVE ACTIONS
		List<Command> actions = actionsGenerator.generateFor(hero, EAction.MOVE);
		
		while (actions.size() > 0) {
			// NO MOVE INTENTION?
			if (moveIntention == null) {
				// => GET ONE
				int index = random.nextInt(actions.size());
				moveIntention = actions.remove(index); 
			}
			
			// ASSESS MOVE INTENTION			
			Room target = (Room)(moveIntention.target);
			
			// TRAP AT THE TARGET ROOM?
			if (target.feature != null) {
				// SOMETHING IN HANDS?
				if (hero.hand != null) {
					// DROP FIRST
					return new Command(EAction.DROP, hero.hand);
				}
			}
			
			// MONSTER AT THE TARGET ROOM?
			if (target.monster != null) {
				// AND NO SWORD?
				if (hero.hand == null) {
					// SWORD IN THE ROOM?
					if (hero.atRoom.item != null && hero.atRoom.item.isA(EItem.SWORD)) {
						return new Command(EAction.PICKUP, hero.atRoom.item);
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

}
