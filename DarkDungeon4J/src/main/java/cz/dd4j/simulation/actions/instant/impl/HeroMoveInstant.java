package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public class HeroMoveInstant implements IHeroInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.MOVE;
	}

	@Override
	public boolean isValid(Hero hero, Action action) {
		if (action.target == null) return false;
		if (!(action.target.isOf(Room.class))) return false;		
		if (hero.atCorridor != null) {
			if (hero.atCorridor.room1 == action.target || hero.atCorridor.room2 == action.target) {
				return ((Room)action.target).hero == null;
			} 
			return false;
		} else
		if (hero.atRoom != null) {
			for (Corridor corridor : hero.atRoom.corridors) {
				if (corridor.room1 == action.target || corridor.room2 == action.target) {
					if (corridor.hero != null) return false;
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@Override
	public void run(Hero hero, Action action) {
		if (hero.atCorridor != null) {
			hero.atCorridor.hero = null;
			hero.atCorridor = null;
			hero.atRoom = (Room)action.target;
			hero.atRoom.hero = hero;
			hero.action = null;
		} else
		if (hero.atRoom != null) {
			for (Corridor corridor : hero.atRoom.corridors) {
				if (corridor.room1 == action.target || corridor.room2 == action.target) {
					hero.atCorridor = corridor;
					hero.atCorridor.hero = hero;
					hero.atRoom.hero = null;
					hero.atRoom = null;
					return;
				}
			}
		}
	}

}
