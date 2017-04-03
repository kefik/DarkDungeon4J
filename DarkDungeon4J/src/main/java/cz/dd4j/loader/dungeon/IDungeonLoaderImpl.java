package cz.dd4j.loader.dungeon;

import java.io.File;

import cz.dd4j.simulation.data.dungeon.Dungeon;

public interface IDungeonLoaderImpl {

	public Dungeon loadDungeon(File xmlFile);	

}
