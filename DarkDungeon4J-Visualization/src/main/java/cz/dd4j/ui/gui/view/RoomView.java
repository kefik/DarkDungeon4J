package cz.dd4j.ui.gui.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.cuni.amis.clear2d.engine.Component;
import cz.cuni.amis.clear2d.engine.components.CAnimation;
import cz.cuni.amis.clear2d.engine.components.CLabel;
import cz.cuni.amis.clear2d.engine.components.CSprite;
import cz.cuni.amis.clear2d.engine.components.CTickCallback;
import cz.cuni.amis.clear2d.engine.fonts.C2DFonts;
import cz.cuni.amis.clear2d.engine.iface.IDrawable;
import cz.cuni.amis.clear2d.engine.iface.ITickable;
import cz.cuni.amis.clear2d.engine.prefabs.Grid;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.c2d.TileIndoor;

public class RoomView extends Grid implements ITickable {
	
	private static Random random = new Random(1);
	
	private List<Component> decoration;
		
	private CLabel label;
	
	public CSprite sword;
	
	public TrapView trap;
	
	private CTickCallback tick;
	
	public Room room;
	
	public int roomX;
	
	public int roomY;
	
	/**
	 * Affects methods setDoorXXX(), must be set prior to calling them.
	 */
	public boolean renderDoors = false;
	
	public RoomView() {
		this(null, 0, 0);
	}
	
	public RoomView(Room room, int roomX, int roomY) {
		super(TileIndoor.tileWidth, TileIndoor.tileHeight, 5, 5);
		
		this.room = room;
		this.roomX = roomX;
		this.roomY = roomY;
		
		cells[0][0].texture = Ctx.tileIndoor(TileIndoor.Wall_NW_png);
		cells[1][0].texture = Ctx.tileIndoor(TileIndoor.Wall_N_png);
		cells[2][0].texture = Ctx.tileIndoor(TileIndoor.Wall_N_png);
		cells[3][0].texture = Ctx.tileIndoor(TileIndoor.Wall_N_png);
		cells[4][0].texture = Ctx.tileIndoor(TileIndoor.Wall_NE_png);
		cells[0][1].texture = Ctx.tileIndoor(TileIndoor.Wall_W_png);
		cells[4][1].texture = Ctx.tileIndoor(TileIndoor.Wall_E_png);
		cells[0][2].texture = Ctx.tileIndoor(TileIndoor.Wall_W_png);
		cells[4][2].texture = Ctx.tileIndoor(TileIndoor.Wall_E_png);
		cells[0][3].texture = Ctx.tileIndoor(TileIndoor.Wall_W_png);
		cells[4][3].texture = Ctx.tileIndoor(TileIndoor.Wall_E_png);
		cells[0][4].texture = Ctx.tileIndoor(TileIndoor.Wall_SW_png);
		cells[1][4].texture = Ctx.tileIndoor(TileIndoor.Wall_S_png);
		cells[2][4].texture = Ctx.tileIndoor(TileIndoor.Wall_S_png);
		cells[3][4].texture = Ctx.tileIndoor(TileIndoor.Wall_S_png);
		cells[4][4].texture = Ctx.tileIndoor(TileIndoor.Wall_SE_png);
		
		// initialize sword
		setSword(true);
		// disable it as default
		setSword(false);
		
		tick = new CTickCallback(this, this);
	}
	
	public void setLabel(String text) {
		if (label == null) {
			label = new CLabel(this);
			label.initLabel(C2DFonts.inconcolata_bold_8px_white, text, Color.BLACK, Color.GRAY);
			label.cText.pos.x += 3;
			label.cText.pos.y += 4;
			label.cBackground.pos.x += 3;
			label.cBackground.pos.y += 3;
		} else {
			if (text == null || text.length() == 0) {
				label.setEnabled(false);
			} else {
				label.setEnabled(true);
				label.setText(text);
			}
		}
	}
	
	public void setDoorNorth() {
		if (renderDoors) cells[2][0].texture = Ctx.tileIndoor(TileIndoor.Door_png);
		else cells[2][0].texture = null;
		cells[2][0].pos.y -= 8;
	}
	
	public void setDoorSouth() {
		cells[1][4].texture = Ctx.tileIndoor(TileIndoor.Wall_NE_png);
		if (renderDoors) cells[2][4].texture = Ctx.tileIndoor(TileIndoor.Door_png);
		else cells[2][4].texture = null;
		cells[2][4].pos.y += 8;
		cells[3][4].texture = Ctx.tileIndoor(TileIndoor.Wall_NW_png);
	}
	
	public void setDoorWest() {
		cells[0][1].texture = Ctx.tileIndoor(TileIndoor.Wall_SE_png);
		cells[0][1].pos.x -= 12;
		if (renderDoors) cells[0][2].texture = Ctx.tileIndoor(TileIndoor.Door_png);
		else cells[0][2].texture = null;
		cells[0][2].pos.x -= 12 + 2;
		cells[0][3].texture = Ctx.tileIndoor(TileIndoor.Wall_NE_png);
		cells[0][3].pos.x -= 12;
	}
	
	public void setDoorEast() {
		cells[4][1].texture = Ctx.tileIndoor(TileIndoor.Wall_SW_png);
		cells[4][1].pos.x += 12;
		if (renderDoors) cells[4][2].texture = Ctx.tileIndoor(TileIndoor.Door_png);
		else cells[4][2].texture = null;
		cells[4][2].pos.x += 12 + 2;
		cells[4][3].texture = Ctx.tileIndoor(TileIndoor.Wall_NW_png);
		cells[4][3].pos.x += 12;
	}
	
	public void setOpeningNorth() {
		cells[2][0].texture = null;
	}
	
	public void setOpeningSouth() {
		cells[1][4].texture = Ctx.tileIndoor(TileIndoor.Wall_NE_png);
		cells[2][4].texture = null;
		cells[3][4].texture = Ctx.tileIndoor(TileIndoor.Wall_NW_png);
	}
	
	public void setOpeningWest() {
		cells[0][1].texture = Ctx.tileIndoor(TileIndoor.Wall_SE_png);
		cells[0][1].pos.x -= 12;
		cells[0][2].texture = null;
		//cells[0][2].pos.x -= 12 + 8;
		cells[0][3].texture = Ctx.tileIndoor(TileIndoor.Wall_NE_png);
		cells[0][3].pos.x -= 12;
	}
	
	public void setOpeningEast() {
		cells[4][1].texture = Ctx.tileIndoor(TileIndoor.Wall_SW_png);
		cells[4][1].pos.x += 12;
		cells[4][2].texture = null;
		//cells[4][2].pos.x += 12+8;
		cells[4][3].texture = Ctx.tileIndoor(TileIndoor.Wall_NW_png);
		cells[4][3].pos.x += 12;
	}
	
	public void setRandomDecoration() {
		int carpet = random.nextInt(10);
		switch (carpet) {
		case 1: setCarpet1(); break;
		case 2: setCarpet2(); break;		
		}
		
		if (random.nextBoolean()) {
			int bb = random.nextInt(3);
			switch (bb) {
			case 0: setBookshelf(); break;
			case 1: setBed(); break;
			case 2: setStatue(); break;
			}
		}
		
		if (random.nextBoolean()) {
			setTorch();
		}		
	}

	public void setBookshelf() {
		addDecorationSprite(3, 5, TileIndoor.Bookshelf_01_png);
		addDecorationSprite(18, 5, TileIndoor.Bookshelf_02_png);
		addDecorationSprite(3, 21, TileIndoor.Bookshelf_03_png);
		addDecorationSprite(18, 21, TileIndoor.Bookshelf_04_png);		
	}
	
	public void setBed() {
		addDecorationSprite(10, 1, TileIndoor.Bed_01_png);
		addDecorationSprite(10, 17, TileIndoor.Bed_02_png);
	}
	
	public void setCarpet1() {
		if (sword != null && sword.enabled) return;
		if (trap != null && trap.enabled) return;
		addDecorationSprite(16, 24, TileIndoor.Carpet1_01_png);
		addDecorationSprite(32, 24, TileIndoor.Carpet1_02_png);
		addDecorationSprite(48, 24, TileIndoor.Carpet1_03_png);
		addDecorationSprite(16, 40, TileIndoor.Carpet1_04_png);
		addDecorationSprite(32, 40, TileIndoor.Carpet1_05_png);
		addDecorationSprite(48, 40, TileIndoor.Carpet1_06_png);
	}

	public void setCarpet2() {
		if (sword != null && sword.enabled) return;
		if (trap != null && trap.enabled) return;
		addDecorationSprite(16, 24, TileIndoor.Carpet2_01_png);
		addDecorationSprite(32, 24, TileIndoor.Carpet2_02_png);
		addDecorationSprite(48, 24, TileIndoor.Carpet2_03_png);
		addDecorationSprite(16, 40, TileIndoor.Carpet2_04_png);
		addDecorationSprite(32, 40, TileIndoor.Carpet2_05_png);
		addDecorationSprite(48, 40, TileIndoor.Carpet2_06_png);
	}
	
	public void setStatue() {
		addDecorationSprite(8, 0, TileIndoor.Knight_01_png);
		addDecorationSprite(8, 16, TileIndoor.Knight_02_png);
	}
	
	public void setTorch() {
		addDecorationAnimation(54, 6, 8, TileIndoor.Torch1_01_png, TileIndoor.Torch1_02_png);		
	}
	
	public void setSword(boolean state) {
		if (state) {
			if (sword == null) {
				sword = new CSprite<IDrawable>(this, Ctx.texSword);
				sword.pos.x = 54;
				sword.pos.y = 38;
			}
			sword.enabled = true;
		} else {
			if (sword != null) {
				sword.enabled = false;
				// NEVER DESTROY THE INSTANCE, WE NEED IT FOR THE POSITIONING OF THE DROP!
			}
		}
	}
	
	/**
	 * Initialize the trap...
	 * @param trap
	 */
	public void initTrap(AgentMindBody<Feature, IFeatureAgent> trap) {
		if (this.trap == null) {
			this.trap = new TrapView(trap, (RoomsView)parent);			
			this.trap.pos.x = 24;
			this.trap.pos.y = 24;
			this.trap.setEnabled(false);
			addChild(this.trap);
		}
	}
	
	/**
	 * NULL == hide, true == alive, false == disarmed
	 * @param alive
	 */
	public void setTrap(Boolean alive) {
		if (trap == null) {
			return;
		}
		if (alive == null) {
			trap.setEnabled(false);
			return;
		}
		trap.setEnabled(true);
		if (alive) {
			trap.cSprite.texture = Ctx.texTrap;
		} else {
			trap.cSprite.texture = Ctx.texTrapDestroyed;
		}
	}
	
	public void setStaircase() {
		addDecorationSprite(32, 32, Ctx.subtexExit);
	}
	
	private void addDecorationSprite(int x, int y, TileIndoor tile) {
		addDecorationSprite(x, y, Ctx.tileIndoor(tile));		
	}
	
	private void addDecorationSprite(int x, int y, IDrawable tile) {
		if (decoration == null) decoration = new ArrayList<Component>();
		
		CSprite sprite = new CSprite(this, tile);
		sprite.pos.x = x;
		sprite.pos.y = y;		
		decoration.add(sprite);
	}
	
	private void addDecorationAnimation(int x, int y, float fps, TileIndoor... tiles) {
		if (decoration == null) decoration = new ArrayList<Component>();
		
		CAnimation<IDrawable> animation = new CAnimation(this);
		
		IDrawable[] frames = new IDrawable[tiles.length];
		for (int i = 0; i < tiles.length; ++i) {
			frames[i] = Ctx.tileIndoor(tiles[i]);
		}
		
		animation.initAnimation(fps, frames);
		animation.pos.x = x;
		animation.pos.y = y;
		decoration.add(animation);
	}

	@Override
	public void tick(C2DTime time) {
		if (room.feature == null) {
			setTrap(null);
		}
//		else
//		if (EH.isA(room.feature, EFeature.TRAP)) {
//			setTrap(room.feature.alive);
//		}
	}
	
}
