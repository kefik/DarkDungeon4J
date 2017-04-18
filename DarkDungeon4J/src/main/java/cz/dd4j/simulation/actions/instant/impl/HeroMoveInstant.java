package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public class HeroMoveInstant extends InstantActionBase<Hero> implements IHeroInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.MOVE;
	}

	@Override
	public boolean isValid(Hero hero, Command action) {
		if (!super.isValid(hero, action)) return false;
		
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
	public void run(Hero hero, Command action) {
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
	
	@Override
	public boolean generateActionsFor(Hero hero, List<Command> actionStore) {
		if (hero.atRoom != null) {
			for (Corridor corridor : hero.atRoom.corridors) {
				actionStore.add(new Command(EAction.MOVE, corridor.getOtherRoom(hero.atRoom)));
			}
			return true;
		}
		return false;
	}

}
