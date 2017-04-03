package cz.dd4j.loader.heroes;

import java.io.File;

import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.loader.meta.MetaInfoLoader;
import cz.dd4j.simulation.data.agents.heroes.Heroes;

public class HeroesLoader {

	public Heroes loadAgents(File xmlFile) {
		MetaInfo meta = MetaInfoLoader.getInstance().loadMetaFor(xmlFile);
		IHeroesLoaderImpl loader = createAgentsLoaderImpl(meta.loaderFQCN);
		return loader.loadAgents(xmlFile);
	}
	
	public IHeroesLoaderImpl createAgentsLoaderImpl(String fqcn) {
		try {
			Class cls = Class.forName(fqcn);
			return (IHeroesLoaderImpl)(cls.getConstructor().newInstance());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class " + fqcn + " using parameterless constructor as IHerosLoaderImpl.", e);
		}		
	}
	
}
