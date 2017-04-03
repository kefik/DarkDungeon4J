package cz.dd4j.loader.heroes.impl.xml;

import java.io.File;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.loader.heroes.IHeroesLoaderImpl;
import cz.dd4j.simulation.data.agents.heroes.Heroes;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.Configure;
import cz.dd4j.utils.config.IConfigurable;
import cz.dd4j.utils.xstream.XStreamLoader;

public class HeroesLoaderXML extends XStreamLoader<HeroesXML> implements IHeroesLoaderImpl {

	public HeroesLoaderXML() {
		super(HeroesXML.class);
	}

	@Override
	public Heroes loadAgents(File xmlFile) {
		HeroesXML agents = load(xmlFile);
		
		Heroes result = new Heroes();
		
		for (HeroXML agentXML : agents.heroes) {
			if (result.heroes.containsKey(agentXML.id)) {
				throw new RuntimeException("There are two Hero[id=" + agentXML.id + "] defined within: " + xmlFile.getAbsolutePath());
			}
			IHeroAgent agent = createAgent(agentXML);
			result.heroes.put(agentXML.id, agent);
		}
		
		return result;
	}

	protected IHeroAgent createAgent(HeroXML agentXML) {
		IHeroAgent agent = createAgentInstance(agentXML.heroFQCN);
		Configure.configure(agent, agentXML.config);
		return agent;
	}

	private IHeroAgent createAgentInstance(String agentFQCN) {
		try {
			Class cls = Class.forName(agentFQCN);
			return (IHeroAgent)(cls.getConstructor().newInstance());
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate hero " + agentFQCN + " as IHeroAgent, using parameterless constructor.", e);
		}
	}

}
