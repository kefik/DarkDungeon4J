package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroDisarmInstant implements IHeroInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.DISARM;
	}

	@Override
	public boolean isValid(Hero hero, Action action) {
		if (action.target == null) return false;
		if (hero.hand != null) return false;
		if (hero.atRoom == null) return false;
		if (hero.atRoom.feature != action.target) return false;
		
		return true;
	}
		
	@Override
	public void run(Hero hero, Action action) {
		hero.atRoom.feature.alive = false;	
		hero.action = null;
	}

}
