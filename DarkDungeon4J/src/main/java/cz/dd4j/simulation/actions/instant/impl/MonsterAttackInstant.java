package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IMonsterInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public class MonsterAttackInstant implements IMonsterInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Monster monster, Action action) {
		if (action.target == null) return false;
		if (monster.atCorridor != null) {
			return monster.atCorridor.hero == action.target;
		} else
		if (monster.atRoom != null) {
			return monster.atRoom.hero == action.target;
		}		
		return false;
	}

	@Override
	public void run(Monster monster, Action action) {
		Hero target = (Hero)action.target;
		target.alive = false;
		monster.action = null;
	}

}
