package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroDisarmInstant extends InstantActionBase<Hero> implements IHeroInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.DISARM;
	}

	@Override
	public boolean isValid(Hero hero, Command action) {
		if (!super.isValid(hero, action)) return false;
		
		if (action.target == null) return false;
		if (hero.hand != null) return false;
		if (hero.atRoom == null) return false;
		if (hero.atRoom.feature != action.target) return false;
		
		return true;
	}
		
	@Override
	public void run(Hero hero, Command action) {
		hero.atRoom.feature.alive = false;	
		hero.action = null;
	}
	
	@Override
	public boolean generateActionsFor(Hero hero, List<Command> actionStore) {
		if (hero.atRoom.feature != null && hero.hand == null) {
			actionStore.add(new Command(EAction.DISARM, hero.atRoom.feature));
			return true;
		}
		return false;
	}

}
