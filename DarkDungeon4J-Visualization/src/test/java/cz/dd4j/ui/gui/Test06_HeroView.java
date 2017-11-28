package cz.dd4j.ui.gui;

import java.io.File;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.view.HeroView;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomsView;

public class Test06_HeroView {

	public static void main(String[] args) {
		// STATE LOAD
		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(new File("./example/Adventure.xml"));
		
		// ENGINE INIT
		Ctx.init();
		DD4JFrame frame = new DD4JFrame();
		
		// PRESENT
		
		// -- RoomsView
		RoomsView roomsView = new RoomsView(simState);		
		frame.dungeon.scene.root.addChild(roomsView);
		
		// -- MonsterView
		for (AgentMindBody<Monster, IMonsterAgent> monster : simState.monsters.values()) {
			MonsterView monsterView = new MonsterView(monster, roomsView);
			roomsView.addChild(monsterView);
		}
		
		// -- HeroView
		
		//    -- load hero
		File heroesFile = new File("./example/hero-test.xml");		
		AgentsLoader<IHeroAgent> heroesLoader = new AgentsLoader<IHeroAgent>();
		Agents<IHeroAgent> heroes = heroesLoader.loadAgents(heroesFile);
		//    -- bind it with its body
		for (AgentMindBody<Hero, IHeroAgent> hero : simState.heroes.values()) {
			if (!heroes.agents.containsKey(hero.body.id)) {
				throw new RuntimeException("Cannot bind mind into hero body for Hero[id=" + hero.body.id + "], " + hero.body.id + " not found in 'heroes'.");
			}
			hero.mind = heroes.agents.get(hero.body.id);
		}
		//   -- create its view
		for (AgentMindBody<Hero, IHeroAgent> hero: simState.heroes.values()) {
			HeroView heroView = new HeroView(hero, roomsView);
			roomsView.addChild(heroView);
		}		
		
		// MAKE VISIBLE
		frame.setVisible(true);
	}
	
}
