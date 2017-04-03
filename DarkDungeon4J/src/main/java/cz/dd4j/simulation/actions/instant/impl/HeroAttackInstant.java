package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public class HeroAttackInstant implements IHeroInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}
	
	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Hero hero, Action action) {
		if (hero.hand == null || !hero.hand.isA(EItem.SWORD)) return false;
		if (hero.atCorridor != null) {
			return hero.atCorridor.monster == action.target;
		} else
		if (hero.atRoom != null) {
			return hero.atRoom.monster == action.target;
		}		
		return false;
	}

	@Override
	public void run(Hero hero, Action action) {
		Monster target = (Monster)action.target;
		target.alive = false;
		hero.action = null;
	}

}
