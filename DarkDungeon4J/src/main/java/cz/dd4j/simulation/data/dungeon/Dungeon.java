package cz.dd4j.simulation.data.dungeon;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.domain.EDungeonLabel;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.ConfigMap;

/**
 * Represents the graph of the dungeon.
 * 
 * @author Jimmy
 */
public class Dungeon {
	
	/**
	 * Id =&gt; {@link Room}.
	 */
	public Map<Id, Room> rooms = new HashMap<Id, Room>();

	/**
	 * {@link EDungeonLabel} -- value.
	 */
	public ConfigMap labels = new ConfigMap();
	
}
