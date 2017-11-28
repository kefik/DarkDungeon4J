package cz.dd4j.ui.gui.view;

import java.util.Random;

import cz.cuni.amis.clear2d.engine.components.CSprite;
import cz.cuni.amis.clear2d.engine.components.CTick;
import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.ui.gui.c2d.TileIndoor;

public class CMovementCtrl extends CTick {
	
	private static Random random = new Random(System.currentTimeMillis());
	
	private cz.dd4j.simulation.data.dungeon.elements.entities.Entity body;

	private RoomsView roomsView;
	
	public Room currentRoom;
	public Corridor currentCorridor;
	
	public int roomViewX = 32;
	public int roomViewY = 32;
	
	public int corridorViewX = 0;
	public int corridorViewY = 0;
	
	public boolean animating = false;

	private CMovement movement;
	
	/**
	 * @param owner initial owner the component; if null, does nothing
	 * @param movement 
	 */
	public CMovementCtrl(Entity owner, cz.dd4j.simulation.data.dungeon.elements.entities.Entity body, RoomsView roomsView, CMovement movement) {
		super(owner);
		this.body = body;
		this.roomsView = roomsView;
		this.movement = movement;
	} 
	
	@Override
	public void tick(C2DTime time) {
		boolean alive = body != null && body != null && body.alive;
		if (!alive) {
			movement.animating = false;
			return;
		}
		
		if (currentRoom == null && currentCorridor == null) {
			if (body.atRoom != null) {
				initPosition(body.atRoom);
				return;
			}
			if (body.atCorridor != null) {
				initPosition(body.atCorridor);
				return;
			}
		} else 
		if (currentRoom != null) {
			if (body.atRoom != null && currentRoom != body.atRoom) {
				moveToRoom(body.atRoom);
				return;
			}
			if (body.atCorridor != null) {
				moveToCorridor(body.atCorridor);
				return;
			}
		} else {
			// currentCorridor != null
			if (body.atRoom != null) {
				moveToRoom(body.atRoom);
				return;
			} else
			if (body.atCorridor != null && currentCorridor != body.atCorridor) {
				moveToCorridor(body.atCorridor);
				return;
			}
		}		
		
		animating = false;
	}

	private void initPosition(Room atRoom) {	
		RoomView atRoomView = roomsView.getRoomView(atRoom);
		
		owner.pos.x = atRoomView.pos.x + roomViewX + random.nextInt(8) - 4;
		owner.pos.y = atRoomView.pos.y + roomViewY + random.nextInt(8) - 4;
		
		currentRoom = atRoom;
	}

	private void initPosition(Corridor atCorridor) {
		RoomView roomView1 = roomsView.getRoomView(atCorridor.room1);
		RoomView roomView2 = roomsView.getRoomView(atCorridor.room2);
		
		float roomView1CenterX = roomView1.pos.x + 5 * TileIndoor.tileWidth / 2;
		float roomView1CenterY = roomView1.pos.y + 5 * TileIndoor.tileWidth / 2;
		
		float roomView2CenterX = roomView2.pos.x + 5 * TileIndoor.tileHeight / 2;
		float roomView2CenterY = roomView2.pos.y + 5 * TileIndoor.tileHeight / 2;
		
		float spriteCorridorX = (roomView1CenterX + roomView2CenterX) / 2 + corridorViewX - owner.getComponent(CSprite.class).texture.getWidth() / 2;
		float spriteCorridorY = (roomView1CenterY + roomView2CenterY) / 2 + corridorViewY - owner.getComponent(CSprite.class).texture.getHeight() / 2;
		
		owner.pos.x = spriteCorridorX;
		owner.pos.y = spriteCorridorY;
	}

	private void moveToRoom(Room toRoom) {
		RoomView roomView = roomsView.getRoomView(toRoom);
		
		movement.setTarget(roomView.pos.x + roomViewX, roomView.pos.y + roomViewY);
		
		animating = movement.animating;
		
		if (!animating) {
			currentCorridor = null;
			currentRoom = toRoom;
		}
	}

	private void moveToCorridor(Corridor toCorridor) {
		animating = true;
		
		RoomView roomView1 = roomsView.getRoomView(toCorridor.room1);
		RoomView roomView2 = roomsView.getRoomView(toCorridor.room2);
		
		float roomView1CenterX = roomView1.pos.x + 5 * TileIndoor.tileWidth / 2;
		float roomView1CenterY = roomView1.pos.y + 5 * TileIndoor.tileWidth / 2;
		
		float roomView2CenterX = roomView2.pos.x + 5 * TileIndoor.tileHeight / 2;
		float roomView2CenterY = roomView2.pos.y + 5 * TileIndoor.tileHeight / 2;
		
		float spriteCorridorX = (roomView1CenterX + roomView2CenterX) / 2 + corridorViewX - owner.getComponent(CSprite.class).texture.getWidth() / 2;
		float spriteCorridorY = (roomView1CenterY + roomView2CenterY) / 2 + corridorViewY - owner.getComponent(CSprite.class).texture.getHeight() / 2;
		
		movement.setTarget(spriteCorridorX, spriteCorridorY);
		
		animating = movement.animating;
		
		if (!animating) {
			currentCorridor = toCorridor;
			currentRoom = null;
		}
	}

}
