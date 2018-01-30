package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public interface IFeatureAgent extends IAgent {

	/**
	 * Feature should decide what to do.
	 * 
	 * Return null for no-action.
	 * 
	 * @return
	 */
	public Command act(Feature featureBody);
	
}
