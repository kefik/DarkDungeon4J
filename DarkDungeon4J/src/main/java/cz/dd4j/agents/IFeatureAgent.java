package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public interface IFeatureAgent extends IAgent {

	public Command act(Feature feature);
	
}
