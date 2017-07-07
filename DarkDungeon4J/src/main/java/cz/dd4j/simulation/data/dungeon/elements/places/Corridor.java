package cz.dd4j.simulation.data.dungeon.elements.places;

import cz.dd4j.domain.EPlace;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.Safe;
import cz.dd4j.utils.astar.graph.ILink;
import cz.dd4j.utils.astar.graph.LinkType;

/**
 * Rooms are connected by corridors (dungeon is thus an undirected graph).
 * 
 * Every corridor may be traveled by either of: {@link Hero} and {@link Monster}.
 * 
 * However, no entities make decisions in-corridor. But we need to implement
 * the movement in two phases in order not to allow to move the Hero and Monster
 * through each other without interaction...
 * 
 * @author Jimmy
 */
public class Corridor extends Place implements ILink<Room> 
{

	/**
	 * We make sure: room1.id &lt; room2.id
	 */
	public Room room1;
	
	/**
	 * We make sure: room2.id &gt; room1.id
	 */
	public Room room2;
	
	/**
	 * There can be at max 1 monster in the corridor.
	 */
	public Monster monster;
	
	/**
	 * There can be at max 1 hero in the corridor. 
	 */	
	public Hero hero;
	
	public Corridor(Room room1, Room room2) {
		super(EPlace.CORRIDOR);
		
		if (room1.id.id > room2.id.id) {
			this.room1 = room2;
			this.room2 = room1;
		} else {
			this.room1 = room1;
			this.room2 = room2;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Corridor)) return false;
		Corridor other = (Corridor)obj;
		return Safe.equals(room1, other.room1) && Safe.equals(room2, other.room2) || Safe.equals(room1, other.room2) && Safe.equals(room2, other.room1);
	}
	
	public boolean leadsTo(Room room) {
		return room1 == room || room2 == room;
	}
	
	public boolean leadsTo(Id roomId) {
		return room1.id == roomId || room2.id == roomId;
	}
	
	public Room getRoom(Id roomId) {
		if (room1.id == roomId) return room1;
		if (room2.id == roomId) return room2;
		return null;
	}
	
	public Room getOtherRoom(Room room) {
		return getOtherRoom(room.id);
	}
	
	public Room getOtherRoom(Id roomId) {
		if (roomId == room1.id) return room2;
		if (roomId == room2.id) return room1;
		return null;
	}
	
	// ===============
	// ILink Interface
	// ===============
	
	@Override
	public LinkType getType() {
		return LinkType.BOTH_WAYS;
	}
	
	@Override
	public Room getNode1() {
		return room1;
	}
	
	@Override
	public Room getNode2() {
		return room2;
	}
	
	@Override
	public int getCost() {
		return 1;
	}
	
	@Override
	public boolean mayTravelFrom(Room room) {
		return room1 == room || room2 == room;
	}
	
	@Override
	public Room getOther(Room room) {
		return getOtherRoom(room); 
	}
	
	// ========
	// ToString
	// ========
	
	@Override
	public String toString() {
		return "Corridor[" + (room1 == null ? "null" : room1.id) + " <-> " + (room2 == null ? "null" : room2.id) + "]";
	}
	
}
