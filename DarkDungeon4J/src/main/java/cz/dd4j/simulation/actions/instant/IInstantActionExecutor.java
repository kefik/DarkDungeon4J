package cz.dd4j.simulation.actions.instant;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public interface IInstantActionExecutor<T extends Entity> {

	public EEntity getEntity();
	
	public EAction getType();
	
	public boolean isValid(T entity, Action action);
	
	public void run(T entity, Action action);	
	
}
