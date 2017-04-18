package cz.dd4j.loader.agents;

import java.io.File;

import cz.dd4j.agents.IAgent;
import cz.dd4j.simulation.data.agents.Agents;

public interface IAgentsLoaderImpl<AGENT extends IAgent> {

	public Agents<AGENT> loadAgents(File xmlFile);	
	
}
