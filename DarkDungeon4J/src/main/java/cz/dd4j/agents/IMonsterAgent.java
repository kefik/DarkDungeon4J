package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.Dungeon;
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
	 * Information update about the dungeon.
	 * @param dungeon
	 * @param full whether the update is full; true == fully observable environment, false == partialy observable environment 
	 */
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis);
	
	/**
	 * Monster should decide what to do.
	 * 
	 * Return null for no-action.
	 * 
	 * @return
	 */
	public Command act();
	
}
