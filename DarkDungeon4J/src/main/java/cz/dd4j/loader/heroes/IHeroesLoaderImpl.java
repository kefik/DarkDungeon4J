package cz.dd4j.loader.heroes;

import java.io.File;

import cz.dd4j.simulation.data.agents.heroes.Heroes;

public interface IHeroesLoaderImpl {

	public Heroes loadAgents(File xmlFile);	
	
}

