package cz.dd4j.ui.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.amis.clear2d.engine.SceneElement;
import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.domain.EDungeonLabel;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.domain.ELabel;
import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.c2d.TileIndoor;
import cz.dd4j.utils.Id;

public class RoomsView extends SceneElement {
	
	private SimState state;
	private int width;
	private int height;
	private int roomsCount;
	
	public Map<Id, RoomView> rooms = new HashMap<Id, RoomView>();
	private String topology;
	
	public RoomsView(SimState state) {
		
		this.state = state;
		
		topology = state.dungeon.labels.getString(EDungeonLabel.TOPOLOGY_TYPE);
		width = state.dungeon.labels.getInt(EDungeonLabel.TOPOLOGY_ROOMS_WIDTH);
		height = state.dungeon.labels.getInt(EDungeonLabel.TOPOLOGY_ROOMS_HEIGHT);
		roomsCount = state.dungeon.labels.getInt(EDungeonLabel.TOPOLOGY_ROOMS_COUNT);
		
		if (topology == null || topology.length() <= 0) throw new RuntimeException("Dungeon does not have the label " + EDungeonLabel.TOPOLOGY_TYPE.id.name + " specified (or incorrect)!");
		if (width <= 0) throw new RuntimeException("Dungeon does not have the label " + EDungeonLabel.TOPOLOGY_ROOMS_WIDTH.id.name + " specified (or incorrect)!");
		if (height <= 0) throw new RuntimeException("Dungeon does not have the label " + EDungeonLabel.TOPOLOGY_ROOMS_HEIGHT.id.name + " specified (or incorrect)!");
		if (roomsCount <= 0) throw new RuntimeException("Dungeon does not have the label " + EDungeonLabel.TOPOLOGY_ROOMS_COUNT.id.name + " specified (or incorrect)!");
		
		if (!EDungeonLabel.TOPOLOGY_TYPE_VALUE_GRID.equals(topology)) {
			throw new RuntimeException("Cannot visualize dungeon with topology " + topology + ", " + EDungeonLabel.TOPOLOGY_TYPE_VALUE_GRID + " only!");
		}
		
		// TODO: this is nuts! ... but we cannot sort rooms as room1 < room10 < room2 ...
		List<Id> roomIds = new ArrayList<Id>();
		for (int i = 1; i <= roomsCount; ++i) {
			roomIds.add(Id.get("room" + i));
		}		
		
		for (int i = 0; i < roomIds.size(); ++i) {
			Id roomId = roomIds.get(i);
			Room room = state.dungeon.rooms.get(roomId);
			
			int roomX = i % width;
			int roomY = i / width;
			
			RoomView roomView = new RoomView(room, roomX, roomY);
			rooms.put(roomId, roomView);
			addChild(roomView);
			
			// POSITION
			roomView.pos.x = roomX * (TileIndoor.tileWidth * 5 + 12);
			roomView.pos.y = roomY * (TileIndoor.tileHeight * 5);
			
			// DOORS & OPENINGS
			boolean mayUp = roomY > 0;
			boolean mayDown = (i + width) < roomsCount;
			boolean mayLeft = roomX > 0;
			boolean mayRight = roomX+1 < width;
			
			boolean up = mayUp && room.hasCorridorTo(roomIds.get(i - width));
			boolean down = mayDown && room.hasCorridorTo(roomIds.get(i + width));
			boolean left = mayLeft && room.hasCorridorTo(roomIds.get(i - 1));
			boolean right = mayRight && room.hasCorridorTo(roomIds.get(i + 1));
			
			if (up) roomView.setOpeningNorth();
			if (down) roomView.setDoorSouth();
			if (left) roomView.setOpeningWest();
			if (right) roomView.setDoorEast();
			
			// SWORD
			if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
				roomView.setSword(true);
			}
			
			// TRAP
			if (room.feature != null && EH.isA(room.feature.type, EFeature.TRAP)) {
				AgentMindBody<Feature, IFeatureAgent> trap = state.features.get(room.feature.id);
				roomView.initTrap(trap);
				roomView.setTrap(true);
			}
			
			// DECORATION || EXIT
			if (room.label == ERoomLabel.GOAL) {
				roomView.setStaircase();
			} else {
				roomView.setRandomDecoration();
			}
			
			// LABEL
			//roomView.setLabel("[" + roomX + "," + roomY + "]");
		}				
		
	}
	
	public int getTotalWidth() {
		return width * (5 * TileIndoor.tileWidth + 12);
	}
	
	public int getTotalHeight() {
		return width * 5 * TileIndoor.tileHeight;
	}
	
	public RoomView getRoomView(Id roomId) {
		return rooms.get(roomId);
	}
	
	public RoomView getRoomView(Room room) {
		return getRoomView(room.id);
	}

}
