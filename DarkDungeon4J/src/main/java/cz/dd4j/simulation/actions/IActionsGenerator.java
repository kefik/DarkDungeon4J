package cz.dd4j.simulation.actions;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public interface IActionsGenerator {

	/**
	 * Generates all possible actions for current state of 'entity'.
	 * @param entity
	 * @return
	 */
	public List<Command> generateFor(Entity entity);
	
	/**
	 * Generates all actions of 'actionType' for current state of 'entity'.
	 * @param entity
	 * @param actionType
	 * @return
	 */
	public List<Command> generateFor(Entity entity, EAction actionType);
	
}
