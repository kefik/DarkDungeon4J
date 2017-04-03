package cz.dd4j.simulation.data.dungeon;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.simulation.data.dungeon.elements.places.Room;

/**
 * Represents the graph of the dungeon.
 * 
 * @author Jimmy
 */
public class Dungeon {

	/**
	 * Id =&gt; {@link Room}.
	 */
	public Map<Integer, Room> rooms = new HashMap<Integer, Room>();	
	
}
