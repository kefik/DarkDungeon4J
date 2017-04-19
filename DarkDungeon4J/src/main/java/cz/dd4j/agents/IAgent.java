package cz.dd4j.agents;

import java.util.Random;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.IActionsGenerator;

public interface IAgent {

	public EEntity getAgentType();
	
	/**
	 * Binds instance of {@link Random} number generator this class should use.
	 * 
	 * Provided by the simulation.
	 * 
	 * Provided by the simulation before the agent starts.
	 */
	public void setRandom(Random random);
	
	/**
	 * Binds action generator into the agent; a generator can be used to easily implement random/semi-random agents
	 * or eases automated learning.
	 * 
	 * Provided by the simulation before the agent starts.
	 * 
	 * @param actionGenerator
	 */
	public void setActionGenerator(IActionsGenerator actionGenerator);
	
}
