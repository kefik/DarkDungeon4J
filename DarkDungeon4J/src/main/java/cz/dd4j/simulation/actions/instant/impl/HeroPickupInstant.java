package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroPickupInstant extends InstantActionBase<Hero> implements IHeroInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.PICKUP;
	}

	@Override
	public boolean isValid(Hero hero, Command action) {
		if (!super.isValid(hero, action)) return false;
		
		if (hero.hand != null)        return false;
		if (hero.atRoom.item == null) return false;
		
		return hero.atRoom.item == action.target;		
	}
		
		
	@Override
	public void run(Hero hero, Command action) {
		hero.hand = hero.atRoom.item;
		hero.atRoom.item = null;
		hero.action = null;
	}
	
	@Override
	public boolean generateActionsFor(Hero hero, List<Command> actionStore) {
		if (hero.hand == null && hero.atRoom.item != null) {
			actionStore.add(new Command(EAction.PICKUP, hero.atRoom.item));
			return true;
		}
		return false;
	}

}
