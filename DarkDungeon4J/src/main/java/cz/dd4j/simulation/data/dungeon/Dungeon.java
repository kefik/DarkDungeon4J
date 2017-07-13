package cz.dd4j.simulation.data.dungeon;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.domain.ELabel;
import cz.dd4j.domain.LabelWithValues;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;

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
	
	// NOT IMPLEMENTED YET... NEVER?
	//public Map<ELabel, LabelWithValues> labels = new HashMap<ELabel, LabelWithValues>();
	
}
