package cz.dd4j.loader.simstate;

import java.io.File;

import cz.dd4j.simulation.data.state.SimState;

public interface ISimStateLoaderImpl {

	public SimState loadSimState(File xmlFile);
	
}
