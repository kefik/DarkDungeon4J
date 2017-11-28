package cz.dd4j.ui.gui;

import java.io.File;

import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomsView;

public class Test05_MonsterView {

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
		
		// MAKE VISIBLE
		frame.setVisible(true);
	}
	
}
