package cz.dd4j.loader.monsters;

import java.io.File;

import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.loader.meta.MetaInfoLoader;
import cz.dd4j.simulation.data.agents.monsters.Monsters;

public class MonstersLoader {

	public Monsters loadAgents(File xmlFile) {
		MetaInfo meta = MetaInfoLoader.getInstance().loadMetaFor(xmlFile);
		IMonstersLoaderImpl loader = createAgentsLoaderImpl(meta.loaderFQCN);
		return loader.loadAgents(xmlFile);
	}
	
	public IMonstersLoaderImpl createAgentsLoaderImpl(String fqcn) {
		try {
			Class cls = Class.forName(fqcn);
			return (IMonstersLoaderImpl)(cls.getConstructor().newInstance());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class " + fqcn + " using parameterless constructor as IMonstersLoaderImpl.", e);
		}		
	}
	
}
