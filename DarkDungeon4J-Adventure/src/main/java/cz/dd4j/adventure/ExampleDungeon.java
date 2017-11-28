package cz.dd4j.adventure;

import java.io.File;
import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.*;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.domain.EEntity;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.actions.instant.IFeatureInstantAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.IMonsterInstantAction;
import cz.dd4j.simulation.actions.instant.impl.FeatureAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroDisarmInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroDropInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroMoveInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroPickupInstant;
import cz.dd4j.simulation.actions.instant.impl.MonsterAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.MonsterMoveInstant;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.ui.gui.VisGUI;

public class ExampleDungeon {

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
		SimState simState = loader.loadSimState(dungeonFile);

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

		// BIND THE CONSOLE VISUALIZATION

		//simulation.getEvents().addHandler(new VisConsole());
		simulation.getEvents().addHandler(new VisGUI());

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
