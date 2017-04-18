package cz.dd4j.loader.agents;

import java.io.File;

import cz.dd4j.agents.IAgent;
import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.loader.meta.MetaInfoLoader;
import cz.dd4j.simulation.data.agents.Agents;

public class AgentsLoader<AGENT extends IAgent> {

	public Agents<AGENT> loadAgents(File xmlFile) {
		MetaInfo meta = MetaInfoLoader.getInstance().loadMetaFor(xmlFile);
		IAgentsLoaderImpl loader = createAgentsLoaderImpl(meta.loaderFQCN);
		return loader.loadAgents(xmlFile);
	}
	
	public IAgentsLoaderImpl<AGENT> createAgentsLoaderImpl(String fqcn) {
		try {
			Class cls = Class.forName(fqcn);
			return (IAgentsLoaderImpl)(cls.getConstructor().newInstance());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class " + fqcn + " using parameterless constructor as IAgentsLoaderImpl.", e);
		}		
	}
	
}
