package cz.dd4j.simulation.actions.instant;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public interface IInstantAction<T extends Entity> {

	public EEntity getEntity();
	
	public EAction getType();
	
	public boolean isValid(T entity, Command action);
	
	public void run(T entity, Command action);
	
	public boolean generateActionsFor(T entity, List<Command> actionStore);
	
}
