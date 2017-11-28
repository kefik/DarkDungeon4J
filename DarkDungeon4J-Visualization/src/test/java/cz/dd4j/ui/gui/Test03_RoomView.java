package cz.dd4j.ui.gui;

import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.c2d.TileIndoor;
import cz.dd4j.ui.gui.view.RoomView;

public class Test03_RoomView {

public static void main(String[] args) {
		
		Ctx.init();
		
		DD4JFrame frame = new DD4JFrame();
		
		// NO DOORS & OPENINGS
		RoomView room = new RoomView();
		frame.dungeon.scene.root.addChild(room);
		
		// ALL DOORS
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 6;
		room.pos.y = TileIndoor.tileHeight * 0;
		room.setDoorNorth();
		room.setDoorSouth();
		room.setDoorWest();
		room.setDoorEast();
		frame.dungeon.scene.root.addChild(room);
		
		// ALL OPENINGS
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 13;
		room.pos.y = TileIndoor.tileHeight * 0;
		room.setOpeningNorth();
		room.setOpeningSouth();
		room.setOpeningWest();
		room.setOpeningEast();
		frame.dungeon.scene.root.addChild(room);
		
		// =================
		// EXAMPLE 3x3 ROOMS
		// =================
		
		// [0,0]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 0;
		room.pos.y = TileIndoor.tileHeight * 6;
		room.setDoorEast();
		room.setDoorSouth();
		room.setCarpet1();
		room.setBookshelf();
		room.setStatue();
		room.setTorch();		
		frame.dungeon.scene.root.addChild(room);
		
		// [1,0]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 5 + 12;
		room.pos.y = TileIndoor.tileHeight * 6;
		room.setOpeningWest();
		room.setDoorEast();
		room.setDoorSouth();
		room.setRandomDecoration();
		frame.dungeon.scene.root.addChild(room);
		
		// [2,0]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 10 + 24;
		room.pos.y = TileIndoor.tileHeight * 6;
		room.setOpeningWest();
		room.setDoorSouth();
		room.setRandomDecoration();
		frame.dungeon.scene.root.addChild(room);
		
		// [0,1]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 0;
		room.pos.y = TileIndoor.tileHeight * 11;
		room.setOpeningNorth();
		room.setDoorEast();
		room.setDoorSouth();
		room.setCarpet1();
		room.setRandomDecoration();
		frame.dungeon.scene.root.addChild(room);
		
		// [1,1]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 5 + 12;
		room.pos.y = TileIndoor.tileHeight * 11;
		room.setOpeningNorth();
		room.setOpeningWest();
		room.setDoorEast();
		room.setDoorSouth();
		room.setRandomDecoration();
		frame.dungeon.scene.root.addChild(room);
		
		// [2,1]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 10 + 24;
		room.pos.y = TileIndoor.tileHeight * 11;
		room.setOpeningNorth();
		room.setOpeningWest();
		room.setDoorSouth();
		room.setRandomDecoration();
		frame.dungeon.scene.root.addChild(room);
		
		// [0,2]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 0;
		room.pos.y = TileIndoor.tileHeight * 16;
		room.setOpeningNorth();
		room.setDoorEast();
		room.setRandomDecoration();
		room.setLabel("0-2");
		frame.dungeon.scene.root.addChild(room);
		
		// [1,2]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 5 + 12;
		room.pos.y = TileIndoor.tileHeight * 16;
		room.setOpeningNorth();
		room.setOpeningWest();
		room.setDoorEast();
		room.setRandomDecoration();
		room.setLabel("1-2");
		frame.dungeon.scene.root.addChild(room);
		
		// [2,2]
		room = new RoomView();
		room.pos.x = TileIndoor.tileWidth * 10 + 24;
		room.pos.y = TileIndoor.tileHeight * 16;
		room.setOpeningNorth();
		room.setOpeningWest();
		room.setRandomDecoration();
		room.setLabel("2-2");
		frame.dungeon.scene.root.addChild(room);
		
		frame.setVisible(true);		
	}
	
}
