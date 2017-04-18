package cz.dd4j.simulation.actions.instant;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public abstract class InstantActionBase<T extends Entity> implements IInstantAction<T> {

	@Override
	public boolean isValid(T entity, Command action) {
		if (!EH.isA(entity.type, getEntity())) return false;		
		if (action.type == null)               return false;
		if (action.type != getType())          return false;
		
		if (getType().params == null || getType().params.length == 0) return action.target == null;
		
		if (getType().params.length > 1) return false;
		
		return getType().params[0].isValidTarget(action.target.type);
	}

}
