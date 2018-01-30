package cz.dd4j.adventure;

import java.io.File;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.replay.Replay;
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

/**
 * To be run after you run {@link ExampleAdventure#main(String[])} that produces 'example-dungeon-run.zip' file that is than used as "the replay"
 * @author Jimmy
 */
public class ExampleAdventureReplay {

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
		SimState simState = loader.loadSimState(dungeonFile, false); // false == do not load agents, these are needless for the replay
		
		// LOAD REPLAY
		
		Replay replay = new Replay(simState, "example-dungeon-run.zip"); // reads the replay + injects feature/monster agents there

		// BIND SIM STATE TO THE CONFIG
		
		config.bindSimState(simState);
		
		// BIND REPLAY HERO TO THE CONFIG

		config.bindHeroes(replay.replayHeroAgents);

		// SANITY CHECKS...

		if (!config.isReady()) {
			throw new RuntimeException("Configuration is not complete. " + config.getMissingInitDescription());
		}

		// CREATE THE SIMULATION

		SimStatic simulation = new SimStatic(config);
		
		// BIND THE REPLAY TO THE SIMULATION
		// -- replay is listening to "rounds" and incrementally reads the replay file from the input stream
		
		simulation.getEvents().addHandler(replay);

		// BIND THE VISUALIZATION
		// note that any or even all of the following lines can be commented out
		
		simulation.getEvents().addHandler(new VisConsole());                              // echoes the run into the console
		simulation.getEvents().addHandler(new VisFile("example-dungeon-run-replay.zip")); // writes the log (replay file!) into a file automatically zipping it up
		simulation.getEvents().addHandler(new VisGUI());                                  // starts the 2D visualization frame, it slows down the simulation as the simulation is waiting onto the visualization

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
