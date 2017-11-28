package cz.dd4j.ui.gui;

import java.io.File;

import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.view.RoomsView;

public class Test04_RoomsView {

	public static void main(String[] args) {
		// STATE LOAD
		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(new File("./example/Adventure.xml"));
		
		// ENGINE INIT
		Ctx.init();
		DD4JFrame frame = new DD4JFrame();
		
		// PRESENT
		RoomsView rooms = new RoomsView(simState);		
		frame.dungeon.scene.root.addChild(rooms);
		
		// MAKE VISIBLE
		frame.setVisible(true);
	}
	
}
