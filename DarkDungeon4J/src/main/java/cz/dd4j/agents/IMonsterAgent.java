package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public interface IMonsterAgent extends IAgent {

	/**
	 * Monster's body update.
	 * 
	 * @param monster
	 * @param currentTickMillis 
	 * @return
	 */
	public void observeBody(Monster monster, long currentTickMillis);
	
	/**
	 * Monster should decide what to do.
	 * 
	 * Return null for no-action.
	 * 
	 * @return
	 */
	public Command act();
	
}
