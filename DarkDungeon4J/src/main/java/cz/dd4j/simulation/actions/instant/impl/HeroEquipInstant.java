package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;

public class HeroEquipInstant implements IHeroInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.EQUIP;
	}

	@Override
	public boolean isValid(Hero hero, Action action) {
		if (action.using == null) {
			if (hero.hand != null) return true;
			return false;
		}
		
		// action.uning != null
		if (!(action.using.isOf(Item.class))) return false;
		if (!hero.inventory.has(action.using.id)) return false;
		
		return true;
	}
		
		
	@Override
	public void run(Hero hero, Action action) {
		if (action.using == null) {
			hero.inventory.add(hero.hand);
			hero.hand = null;
			return;
		}
		
		// action.using != null
		Item fromHand = hero.hand;		
		hero.inventory.remove(action.using.id);
		hero.hand = (Item)action.using;	
		if (fromHand != null) {
			hero.inventory.add(fromHand);
		}
		hero.action = null;
	}

}
