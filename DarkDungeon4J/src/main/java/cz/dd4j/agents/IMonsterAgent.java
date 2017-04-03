package cz.dd4j.agents;

import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public interface IMonsterAgent {

	/**
	 * Exactly one parameter is non-null and vice versa.
	 * 
	 * @param room
	 * @param corridor
	 * @return
	 */
	public Action act(Room room, Corridor corridor);
	
}
