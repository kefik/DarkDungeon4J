package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroPickupInstant implements IHeroInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.PICKUP;
	}

	@Override
	public boolean isValid(Hero hero, Action action) {
		if (hero.atRoom.item == null) return false;
		return hero.atRoom.item == action.target;
	}
		
		
	@Override
	public void run(Hero hero, Action action) {
		hero.inventory.add(hero.atRoom.item);
		hero.atRoom.item = null;
		hero.action = null;
	}

}
