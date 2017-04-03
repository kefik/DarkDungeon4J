package cz.dd4j.loader.monsters.impl.xml;

import java.io.File;

import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.loader.monsters.IMonstersLoaderImpl;
import cz.dd4j.simulation.data.agents.monsters.Monsters;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.Configure;
import cz.dd4j.utils.config.IConfigurable;
import cz.dd4j.utils.xstream.XStreamLoader;

public class MonstersLoaderXML extends XStreamLoader<MonstersXML> implements IMonstersLoaderImpl {

	public MonstersLoaderXML() {
		super(MonstersXML.class);
	}

	@Override
	public Monsters loadAgents(File xmlFile) {
		MonstersXML agents = load(xmlFile);
		
		Monsters result = new Monsters();
		
		for (MonsterXML agentXML : agents.monsters) {
			if (result.monsters.containsKey(agentXML.id)) {
				throw new RuntimeException("There are two Monster[id=" + agentXML.id + "] defined within: " + xmlFile.getAbsolutePath());
			}
			IMonsterAgent agent = createAgent(agentXML);
			result.monsters.put(agentXML.id, agent);
		}
		
		return result;
	}

	protected IMonsterAgent createAgent(MonsterXML agentXML) {
		IMonsterAgent agent = createAgentInstance(agentXML.monsterFQCN);
		Configure.configure(agent, agentXML.config);
		return agent;
	}

	private IMonsterAgent createAgentInstance(String agentFQCN) {
		try {
			Class cls = Class.forName(agentFQCN);
			return (IMonsterAgent)(cls.getConstructor().newInstance());
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate monster " + agentFQCN + " as IMonsterAgent, using parameterless constructor.", e);
		}
	}

}
