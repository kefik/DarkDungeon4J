package cz.dd4j.ui.gui;

import java.io.File;
import java.util.Random;

import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.domain.EItem;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.entities.features.Trap;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.utils.BusyWait;
import cz.dd4j.ui.gui.utils.IWaiting;
import cz.dd4j.ui.gui.view.HeroView;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomView;

public class Test14_SimStatic {
	
	public static void main(String[] args) {
		// ENGINE INIT
		Ctx.init();
		
		// ADVENTURE INIT
		SimStaticConfig config = MockSimStaticConfig.getSimStaticConfig();

		// LOAD SIM STATE
		// WARNING: this assumes use of Eclipse of NetBeans that starts the code within the project folder itself!		
		File dungeonFile = new File("./example/Adventure.xml");

		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(dungeonFile, true);

		config.bindSimState(simState);

		// CREATE THE HERO!
		//File heroesFile = new File("./example/hero-1-test.xml");
		File heroesFile = new File("./example/hero-2-test.xml");
		
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
