package cz.dd4j.loader.dungeon;

import java.io.File;

import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.loader.meta.MetaInfoLoader;
import cz.dd4j.simulation.data.dungeon.Dungeon;

public class DungeonLoader {

	public Dungeon loadDungeon(File xmlFile) {
		MetaInfo meta = MetaInfoLoader.getInstance().loadMetaFor(xmlFile);
		IDungeonLoaderImpl loader = createDungeonLoaderImpl(meta.loaderFQCN);
		return loader.loadDungeon(xmlFile);
	}
	
	public IDungeonLoaderImpl createDungeonLoaderImpl(String fqcn) {
		try {
			Class cls = Class.forName(fqcn);
			return (IDungeonLoaderImpl)(cls.getConstructor().newInstance());			
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class " + fqcn + " using parameterless constructor as IDungeonLoaderImpl.", e);
		}		
	}
	
}
