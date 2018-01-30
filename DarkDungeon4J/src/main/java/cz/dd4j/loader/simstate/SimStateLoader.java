package cz.dd4j.loader.simstate;

import java.io.File;

import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.loader.meta.MetaInfoLoader;
import cz.dd4j.simulation.data.state.SimState;

public class SimStateLoader {

	public SimState loadSimState(File xmlFile, boolean includeAgents) {
		MetaInfo meta = MetaInfoLoader.getInstance().loadMetaFor(xmlFile);
		ISimStateLoaderImpl loader = createSimStateLoaderImpl(meta.loaderFQCN);
		return loader.loadSimState(xmlFile, includeAgents);
	}
	
	public ISimStateLoaderImpl createSimStateLoaderImpl(String fqcn) {
		try {
			Class cls = Class.forName(fqcn);
			return (ISimStateLoaderImpl)(cls.getConstructor().newInstance());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class " + fqcn + " using parameterless constructor as ISimStateLoaderImpl.", e);
		}		
	}
	
}
