package cz.dd4j.loader.simstate;

import java.io.File;

import cz.dd4j.simulation.data.state.SimState;

public interface ISimStateLoaderImpl {

	/**
	 * @param xmlFile file to load
	 * @param includeAgents whether to instantiate agents (true) or just load only the dungeon (==false) ~ used for replays
	 * @return
	 */
	public SimState loadSimState(File xmlFile, boolean includeAgents);
	
}
