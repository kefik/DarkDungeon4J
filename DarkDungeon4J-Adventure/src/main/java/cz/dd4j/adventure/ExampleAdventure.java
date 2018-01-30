package cz.dd4j.adventure;

import java.io.File;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.ui.gui.VisGUI;
import cz.dd4j.ui.log.VisFile;

public class ExampleAdventure {

	public static void main(String[] args) {
		runDungeon();
	}

	/**
	 * Example Dungeon.
	 */
	private static void runDungeon() {
		SimStaticConfig config = ExperimentEvaluator.getSimStaticConfig();

		// LOAD SIM STATE

		File dungeonFile = new File("./data/dungeons/dungeon-example/dungeon-01.xml");

		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(dungeonFile, true);

		config.bindSimState(simState);

		// CREATE THE HERO!

		// WARNING: this assumes use of Eclipse of NetBeans that starts the code within the project folder itself!		
		//File heroesFile = new File("./data/hero-agents/hero-random.xml");
		//File heroesFile = new File("./data/hero-agents/hero-semi-random.xml");
		File heroesFile = new File("./data/hero-agents/hero-rules-with-random-move.xml");
		//File heroesFile = new File("./data/hero-agents/nplan-cygwin.xml");
		
		AgentsLoader<IHeroAgent> heroesLoader = new AgentsLoader<IHeroAgent>();
		Agents<IHeroAgent> heroes = heroesLoader.loadAgents(heroesFile);

		config.bindHeroes(heroes);

		// SANITY CHECKS...

		if (!config.isReady()) {
			throw new RuntimeException("Configuration is not complete. " + config.getMissingInitDescription());
		}

		// CREATE THE SIMULATION

		SimStatic simulation = new SimStatic(config);

		// BIND THE VISUALIZATION
		// note that any or even all of the following lines can be commented out

		simulation.getEvents().addHandler(new VisConsole());                       // echoes the run into the console
		simulation.getEvents().addHandler(new VisFile("example-dungeon-run.zip")); // writes the log (replay file!) into a file automatically zipping it up
		simulation.getEvents().addHandler(new VisGUI());                           // starts the 2D visualization frame, it slows down the simulation as the simulation is waiting onto the visualization

		// FIRE THE SIMULATION!
		SimStaticStats result = simulation.simulate();

		// OUTPUT RESULT
		System.out.println();
		System.out.println("Finished: " + result.simulationResult);
		System.out.println();

		// DONE!

		System.out.println("---/// DONE ///---");
	}

}
