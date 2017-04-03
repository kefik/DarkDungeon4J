package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IMonsterInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public class MonsterMoveInstant implements IMonsterInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.HERO;
	}

	@Override
	public EAction getType() {
		return EAction.MOVE;
	}

	@Override
	public boolean isValid(Monster monster, Action action) {
		if (action.target == null) return false;
		if (!action.target.isOf(Room.class)) return false;		
		if (monster.atCorridor != null) {
			if (monster.atCorridor.room1 == action.target || monster.atCorridor.room2 == action.target) {
				return ((Room)action.target).monster == null;
			} 
			return false;
		} else
		if (monster.atRoom != null) {
			for (Corridor corridor : monster.atRoom.corridors) {
				if (corridor.room1 == action.target || corridor.room2 == action.target) {
					if (corridor.monster != null) return false;
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@Override
	public void run(Monster monster, Action action) {
		if (monster.atCorridor != null) {
			monster.atCorridor.monster = null;
			monster.atCorridor = null;
			monster.atRoom = (Room)action.target;
			monster.atRoom.monster = monster;
			monster.action = null;
		} else
		if (monster.atRoom != null) {
			for (Corridor corridor : monster.atRoom.corridors) {
				if (corridor.room1 == action.target || corridor.room2 == action.target) {
					monster.atCorridor = corridor;
					monster.atCorridor.monster = monster;
					monster.atRoom.monster = null;
					monster.atRoom = null;
					return;
				}
			}
		}
	}

}
