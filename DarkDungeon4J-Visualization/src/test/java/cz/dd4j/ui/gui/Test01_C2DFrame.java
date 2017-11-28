package cz.dd4j.ui.gui;

import cz.cuni.amis.clear2d.engine.C2DFrame;
import cz.cuni.amis.clear2d.engine.fonts.C2DFonts;
import cz.cuni.amis.clear2d.engine.prefabs.FPS;
import cz.cuni.amis.clear2d.engine.prefabs.Text;
import cz.dd4j.ui.gui.c2d.Ctx;

public class Test01_C2DFrame {
	
	public static void main(String[] args) {
		Ctx.init();
		
		C2DFrame frame = new C2DFrame("C2DTest");
		
		FPS fps = new FPS();
		fps.pos.x = 10;
		fps.pos.y = 10;
		frame.panel.scene.root.addChild(fps);
		
		Text text = new Text(C2DFonts.inconcolata_bold_10px_white, "Working :)");
		text.pos.x = 10;
		text.pos.y = 30;		
		frame.panel.scene.root.addChild(text);
		
		frame.setVisible(true);		
	}
	
}
