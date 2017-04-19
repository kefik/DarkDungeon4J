package cz.dd4j.simulation.exceptions;

import cz.dd4j.agents.IAgent;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public class AgentException extends RuntimeException {

	/**
	 * AUTO-GENERATED.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * What entity has failed.
	 */
	public final AgentMindBody<? extends Entity, ? extends IAgent> agent;

	public AgentException(AgentMindBody<? extends Entity, ? extends IAgent> agent, String message, Throwable cause) {
		super(message, cause);
		this.agent = agent;
	}
	
}
