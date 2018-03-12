package cz.dd4j.simulation.data.state.slim;

import java.io.File;

import org.junit.Test;

import cz.dd4j.adventure.ExperimentEvaluator;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;

public class SlimStateTest {

	/*
	@Test
	public void test01() {
		SimStaticConfig config = ExperimentEvaluator.getSimStaticConfig();
		
		File dungeonFile = new File("./data/dungeons/dungeon-example/dungeon-01.xml");

		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(dungeonFile, true);

		config.bindSimState(simState);

		// CREATE THE HERO!
		File heroesFile = new File("./data/hero-agents/hero-random.xml");
		
		AgentsLoader<IHeroAgent> heroesLoader = new AgentsLoader<IHeroAgent>();
		Agents<IHeroAgent> heroes = heroesLoader.loadAgents(heroesFile);

		config.bindHeroes(heroes);

		// NOW WE HAVE SOME STATE -> INIT IDs
		simState.initIds();
		
		// MAKE SLIM STATE
		System.out.println("LOADING SLIM STATE...");
		SlimState slim = new SlimState(simState);
		
		System.out.println("SLIM STATE LOADED...");
		System.out.println(slim);
		
		System.out.println("---// TEST OK //---");
	}
	*/
	
	@Test
	public void test02() {
		SimStaticConfig config = ExperimentEvaluator.getSimStaticConfig();
		
		File dungeonFile = new File("./data/dungeons/dungeon-example/dungeon-01.xml");

		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(dungeonFile, true);

		config.bindSimState(simState);

		// CREATE THE HERO!
		File heroesFile = new File("./data/hero-agents/hero-random.xml");
		
		AgentsLoader<IHeroAgent> heroesLoader = new AgentsLoader<IHeroAgent>();
		Agents<IHeroAgent> heroes = heroesLoader.loadAgents(heroesFile);

		config.bindHeroes(heroes);

		// NOW WE HAVE SOME STATE -> INIT IDs
		simState.initIds();
		
		// MAKE SLIM STATE
		System.out.println("LOADING SLIM STATE...");
		SlimState slim = new SlimState(simState);
		
		System.out.println("SLIM STATE LOADED...");
		System.out.println(slim);
		
		// COMPARE THE STATE
		for (Room room : simState.dungeon.rooms.values()) {
			System.out.println("SimState room: " + room);
			
			int roomRef = simState.ids.roomRef(room);
			System.out.println("  +-- room ref = " + roomRef);
			
			int slimRoom = SlimRoom.fromSimState(simState, room);
			System.out.println("SlimState room:\n" + SlimRoom.toString(slimRoom));
			
			if (SlimRoom.getRoomRef(slimRoom) != simState.ids.roomRef(room)) throw new RuntimeException("ROOM REF ERROR! slimRoom.roomRef = " + SlimRoom.getRoomRef(slimRoom) + " != " + roomRef + " == simRoom.roomRef");
			if (SlimRoom.isTrap(slimRoom) != (room.feature != null)) throw new RuntimeException("TRAP ERROR! slimRoom.isTrap = " + SlimRoom.isTrap(slimRoom) + " != " + (room.feature != null) + " == simRoom.feature");
			if (SlimRoom.getMonsterRef(slimRoom) != simState.ids.monsterRef(room.monster)) throw new RuntimeException("MONSTER ERROR! slimRoom.monsterRef = " + SlimRoom.getMonsterRef(slimRoom) + " != " + simState.ids.monsterRef(room.monster) + " == simRoom.monsterRef");
			if (SlimRoom.getHeroRef(slimRoom) != simState.ids.heroRef(room.hero)) throw new RuntimeException("HERO ERROR! slimRoom.heroRef = " + SlimRoom.getHeroRef(slimRoom) + " != " + simState.ids.heroRef(room.hero) + " == simRoom.heroRef");
			if (SlimRoom.getItemRef(slimRoom) != simState.ids.itemRef(room.item)) throw new RuntimeException("ITEM ERROR! slimRoom.itemRef = " + SlimRoom.getItemRef(slimRoom) + " != " + simState.ids.itemRef(room.item) + " == simRoom.itemRef");
		}
	}
	
}
