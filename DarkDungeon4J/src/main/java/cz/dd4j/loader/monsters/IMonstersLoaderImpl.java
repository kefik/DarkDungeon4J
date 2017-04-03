package cz.dd4j.loader.monsters;

import java.io.File;

import cz.dd4j.simulation.data.agents.monsters.Monsters;

public interface IMonstersLoaderImpl {

	public Monsters loadAgents(File xmlFile);	
	
}
