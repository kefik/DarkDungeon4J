package cz.dd4j.generator.agents;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.loader.agents.impl.xml.AgentXML;
import cz.dd4j.loader.agents.impl.xml.AgentsLoaderXML;
import cz.dd4j.loader.agents.impl.xml.AgentsXML;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;


public class AgentsGenerator extends GeneratorBase<AgentsGeneratorConfig> {
	
	public AgentsGenerator(AgentsGeneratorConfig config) {
		super(AgentsXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(" + config.agentIdPrefix + "1->" + config.agentsCount + ", #prototypes = " + config.agentPrototypes.size() + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (AgentXML agentPrototype : config.agentPrototypes) {
			for (int agentId = 1; agentId <= config.agentsCount; ++agentId) {
				generate(agentId, agentPrototype);	
			}
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int agentId, AgentXML agentPrototype) {
		File targetFile = config.target.getFile("/agents/" + config.directory, config.agentIdPrefix + agentId + "-" + agentPrototype.name + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(agent " + agentPrototype.name + " of id " + agentId + "): generating...");
		
		AgentsXML agents = new AgentsXML();		
		agents.agents = new ArrayList<AgentXML>(1);
		
		agentPrototype.id = Id.get(config.agentIdPrefix + agentId);
		
		agents.agents.add(agentPrototype);
		
		write(targetFile, agents, AgentsLoaderXML.class, "Agent: " + agentPrototype.name + Const.NEW_LINE + "FQCN:  " + agentPrototype.agentFQCN + Const.NEW_LINE + "Requires agent: " + config.agentIdPrefix + agentId);
	}

}
