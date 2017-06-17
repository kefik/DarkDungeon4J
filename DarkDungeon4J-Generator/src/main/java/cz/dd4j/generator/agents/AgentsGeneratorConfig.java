package cz.dd4j.generator.agents;

import java.util.List;

import cz.dd4j.generator.GeneratorConfig;
import cz.dd4j.loader.agents.impl.xml.AgentXML;

public class AgentsGeneratorConfig extends GeneratorConfig {
	
	/**
	 * Prefix for the agent id.
	 * 
	 * Typically a 'hero' or 'monster' or 'trap'.
	 */
	public String agentIdPrefix;
	
	/**
	 * Target directory, sub-directory of "agents".
	 */
	public String directory;
	
	/**
	 * Generates agentIdPrefix'X', X from [1; agentIdCount].
	 */
	public int agentsCount = 1;
	
	/**
	 * We generate all agents from 'agentPrototypes' for all agentIdPrefix'X'.
	 * 
	 * Use {@link AgentXML#AgentXML(String, String, cz.dd4j.utils.config.ConfigXML...)} constructor for creating them.
	 * 
	 * {@link AgentXML#name} is used within the name of the result file; must be path-sane!
	 */
	public List<AgentXML> agentPrototypes;
	
}
