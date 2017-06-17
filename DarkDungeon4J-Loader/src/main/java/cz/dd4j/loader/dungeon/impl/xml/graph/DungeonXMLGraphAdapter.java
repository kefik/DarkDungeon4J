package cz.dd4j.loader.dungeon.impl.xml.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.dd4j.loader.dungeon.impl.xml.CorridorXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.astar.graph.ILink;
import cz.dd4j.utils.astar.graph.INode;
import cz.dd4j.utils.astar.graph.Link;
import cz.dd4j.utils.astar.graph.LinkType;

public class DungeonXMLGraphAdapter {

	private static class RoomXMLNode implements INode<RoomXMLNode> {

		public List<ILink<RoomXMLNode>> links = new ArrayList<ILink<RoomXMLNode>>();
		
		public RoomXML room;
		
		public RoomXMLNode(RoomXML room) {
			this.room = room;
		}
		
		@Override
		public Collection<ILink<RoomXMLNode>> getLinks() {
			return links;
		}
		
	}
	
	private Map<Id, RoomXMLNode> rooms = new HashMap<Id, RoomXMLNode>();
		
	public DungeonXMLGraphAdapter(List<RoomXML> rooms, List<CorridorXML> corridors) {
		for (RoomXML room : rooms) {
			this.rooms.put(room.id, new RoomXMLNode(room));
		}
		for (CorridorXML corridor : corridors) {
			if (!rooms.contains(corridor.room1Id)) {
				throw new RuntimeException("Invalid dungeon, data contains corridor that is referencing non-existing room: " + corridor.room1Id);
			}
			if (!rooms.contains(corridor.room2Id)) {
				throw new RuntimeException("Invalid dungeon, data contains corridor that is referencing non-existing room: " + corridor.room2Id);
			}
			
			RoomXMLNode n1 = this.rooms.get(corridor.room1Id);
			RoomXMLNode n2 = this.rooms.get(corridor.room2Id);
			
			Link<RoomXMLNode> link = new Link<RoomXMLNode>(n1, n2, LinkType.BOTH_WAYS, 1);
			
			n1.links.add(link);
			n2.links.add(link);
		}
	}
	
	public RoomXMLNode getRoom(String id) {
		return rooms.get(Id.get(id));
	}
	
	public RoomXMLNode getRoom(Id id) {
		return rooms.get(id);
	}
	
	public RoomXMLNode getRoom(RoomXML room) {
		return rooms.get(room.id);
	}
	
}
