package cz.dd4j.loader.agents.impl.xml;

import java.io.File;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.loader.LoaderXML;
import cz.dd4j.loader.agents.IAgentsLoaderImpl;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.utils.IdTypeConverter;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.utils.config.Configure;
import cz.dd4j.utils.xstream.XStreamLoader;

public class AgentsLoaderXML<AGENT extends IAgent> extends LoaderXML<AgentsXML> implements IAgentsLoaderImpl<AGENT> {

	public AgentsLoaderXML() {
		super(AgentsXML.class);
		xstream.registerConverter(new IdTypeConverter());
	}

	@Override
	public Agents<AGENT> loadAgents(File xmlFile) {
		AgentsXML agents = load(xmlFile);
		
		Agents<AGENT> result = new Agents<AGENT>();
		
		for (AgentXML agentXML : agents.agents) {
			if (result.agents.containsKey(agentXML.id)) {
				throw new RuntimeException("There are two Feature[id=" + agentXML.id + "] defined within: " + xmlFile.getAbsolutePath());
			}
			AGENT agent = createAgent(agentXML);
			result.agents.put(agentXML.id, agent);
		}
		
		return result;
	}

	protected AGENT createAgent(AgentXML agentXML) {
		AGENT agent = createAgentInstance(agentXML.agentFQCN);
		Configure.configure(agent, agentXML.config);
		return agent;
	}

	private AGENT createAgentInstance(String agentFQCN) {
		Object agent = null;
		try {
			Class cls = Class.forName(agentFQCN);
			agent  = (cls.getConstructor().newInstance());
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate feature " + agentFQCN + ", using parameterless constructor.", e);
		}
		return (AGENT)agent;
	}

}
