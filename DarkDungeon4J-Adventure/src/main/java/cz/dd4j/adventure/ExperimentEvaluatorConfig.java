package cz.dd4j.adventure;

import java.io.File;

import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.utils.config.BidirConfig;
import cz.dd4j.utils.config.DirLocation;
import cz.dd4j.utils.config.GenericConfig;

public class ExperimentEvaluatorConfig extends BidirConfig {

	public DirLocation heroAgents;
	
	/**
	 * If greater then 0, then the simulator will execute only this number of playouts at max. 
	 */
	public int playoutLimit = -1;
	public int maxCores = -1;
	
	/**
	 * Whether to attach {@link VisConsole} to all executed simulations.
	 */
	public boolean consoleVisualization = false;
	
	/**
	 * Whether to generate zipped replay files.
	 */
	public boolean storeReplays = true;

	/**
	 * Alters the timeout for the number of steps. Once an agent executes PARAM*dungeon.#rooms step without reaching the goal, the simulation will timeout.
	 */
	public double timeoutMultiplier = 10;
	
	public ExperimentEvaluatorConfig() {
		source.dir = new File("../DarkDungeon4J-Generator/result");
		target.dir = new File("./result");		
		heroAgents = new DirLocation(new File("data/hero-agents"));
	}
	
	@Override
	public void assign(GenericConfig from) {
		if (from == null) return;
		super.assign(from);
		if (from instanceof ExperimentEvaluatorConfig) {
			ExperimentEvaluatorConfig eec = (ExperimentEvaluatorConfig)from;
			if (eec.source != null) {
				if (eec.heroAgents.dir != null) heroAgents.dir = eec.heroAgents.dir;
				if (eec.heroAgents.filePrefix != null) heroAgents.filePrefix = eec.heroAgents.filePrefix;
			}			
		}
	}
	
}
