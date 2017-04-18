package cz.dd4j.simulation.data.dungeon.elements.places;

import cz.dd4j.domain.EPlace;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.utils.Id;

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
public class Corridor extends Place {

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
	
	public Room getOtherRoom(Room room) {
		return getOtherRoom(room.id);
	}
	
	public Room getOtherRoom(Id roomId) {
		if (roomId == room1.id) return room2;
		if (roomId == room2.id) return room1;
		return null;
	}
	
	@Override
	public String toString() {
		return "Corridor[" + (room1 == null ? "null" : room1.id) + " <-> " + (room2 == null ? "null" : room2.id) + "]";
	}
	
}
