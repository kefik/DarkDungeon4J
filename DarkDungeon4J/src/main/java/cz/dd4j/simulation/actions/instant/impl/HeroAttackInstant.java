package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public class HeroAttackInstant extends InstantActionBase<Hero> implements IHeroInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}
	
	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Hero hero, Command action) {
		if (!super.isValid(hero, action)) return false;
		
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
	public void run(Hero hero, Command action) {
		Monster target = (Monster)action.target;
		target.alive = false;
		hero.action = null;
	}
	
	@Override
	public boolean generateActionsFor(Hero hero, List<Command> actionStore) {
		if (hero.atRoom.monster != null && hero.hand != null && hero.hand.isA(EItem.SWORD)) {
			actionStore.add(new Command(EAction.ATTACK, hero.atRoom.monster));
			return true;
		}
		return false;
	}

}
