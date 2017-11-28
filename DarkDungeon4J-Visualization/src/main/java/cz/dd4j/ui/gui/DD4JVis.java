package cz.dd4j.ui.gui;

import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.domain.EDungeonLabel;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.view.HeroView;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomsView;

public class DD4JVis {
	
	public SimState simState;
	
	/**
	 * May be null!
	 */
	public SimStaticStats stats; 
	
	public DD4JFrame frame;
	
	public RoomsView roomsView;
	
	public Map<Monster, MonsterView> monsterViews;
	
	public Map<Hero, HeroView> heroViews;
	
	/**
	 * @param state
	 * @param stats may be null!
	 */
	public DD4JVis(SimState state, SimStaticStats stats) {
		this.simState = state;
		this.stats = stats;
		
		init();		
	}
	
	private void init() {
		// -- RoomsView
		roomsView = new RoomsView(simState);	
		roomsView.pos.x = 10;
		roomsView.pos.y = 10;
				
		
		// FRAME INIT
		frame = new DD4JFrame(roomsView.getTotalWidth() + 20, roomsView.getTotalWidth() + 20);

		// adding room view to frame
		frame.dungeon.scene.root.addChild(roomsView);
		
		// -- MonsterView
		monsterViews = new HashMap<Monster, MonsterView>();
		for (AgentMindBody<Monster, IMonsterAgent> monster : simState.monsters.values()) {
			MonsterView monsterView = new MonsterView(monster, roomsView);
			roomsView.addChild(monsterView);
			monsterViews.put(monster.body, monsterView);	
		}
		
		//   -- create its view
		heroViews = new HashMap<Hero, HeroView>();		
		for (AgentMindBody<Hero, IHeroAgent> hero: simState.heroes.values()) {
			HeroView heroView = new HeroView(hero, roomsView);
			roomsView.addChild(heroView);
			heroViews.put(hero.body, heroView);
		}	
	}
	
	public void die() {
		if (frame != null) {
			frame.die();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			frame = null;
		}
	}
	
}
