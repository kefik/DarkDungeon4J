package cz.dd4j.simulation.actions;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public interface IActionsValidator {

	public boolean isValid(Entity entity, Command action);
	
}
