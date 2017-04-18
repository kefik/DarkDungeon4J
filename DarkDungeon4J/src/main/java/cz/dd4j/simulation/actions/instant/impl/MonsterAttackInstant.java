package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IMonsterInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public class MonsterAttackInstant extends InstantActionBase<Monster> implements IMonsterInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.MONSTER;
	}

	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Monster monster, Command action) {
		if (!super.isValid(monster, action)) return false;
		
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
	public void run(Monster monster, Command action) {
		Hero target = (Hero)action.target;
		if (target.hand != null && target.hand.isA(EItem.SWORD)) {
			// monster auto-killed by the hero
			monster.alive = false; 
		} else {
			// hero killed by the monster
			target.alive = false;
		}
		monster.action = null;
	}
	
	@Override
	public boolean generateActionsFor(Monster monster, List<Command> actionStore) {
		if (monster.atRoom.hero != null) {
			actionStore.add(new Command(EAction.ATTACK, monster.atRoom.hero));
			return true;
		}
		return false;
	}

}
