package cz.dd4j.agents;

import java.util.Map;
import java.util.Random;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.actions.IActionsValidator;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public interface IAgent {

	/**
	 * Returns {@link Entity} this agent represents.
	 * @return
	 */
	public EEntity getAgentType();
	
	// =============
	// CONFIGURATION
	// =============
	
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
	
	/**
	 * Binds action validator into the agent; agent may use this to check whether the action is valid and will be carried
	 * out of it won't interfere with other agent actions.
	 * 
	 * @param actionValidator
	 */
	public void setActionValidator(IActionsValidator actionValidator);
	
	// =====================
	// SIMULATION LIFE-CYCLE
	// =====================
	
	/**
	 * Calls before the simulation begins.
	 */
	public void prepareAgent();
	
	/**
	 * Simulation has been setup, sensomotoric cycle of an agent will be called soon. 
	 */
	public void simulationStarted();
	
	/**
	 * Agent has been killed, its sensomotoric cycle will not be further called.
	 */
	public void agentDead();
	
	/**
	 * End of simulation.
	 * 
	 * May be called even if {@link #prepareAgent()} or {@link #simulationStarted()} was not invoked due to some exception during the simulation preparation phase.
	 * 
	 * WARNING: exception thrown by this method are ignored and not logged!
	 */
	public void simulationEnded();
	
	// =============
	// DATA REPORTER
	// =============
	
	/**
	 * Returns the report about the agent run; this is used by experiments that reports data about agent runs into CSV files.
	 * @return
	 */
	public Map<String, Object> getReport();
	
	
}
