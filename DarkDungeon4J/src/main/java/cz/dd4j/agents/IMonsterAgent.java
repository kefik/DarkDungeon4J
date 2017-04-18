package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public interface IMonsterAgent extends IAgent {

	/**
	 * Exactly one parameter is non-null and vice versa.
	 * 
	 * @param room
	 * @param corridor
	 * @return
	 */
	public Command act(Room room, Corridor corridor);
	
}
