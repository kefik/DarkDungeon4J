package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

/**
 * Represents action-selection for the {@link Hero} body.
 * 
 * @author Jimmy
 */
public interface IHeroAgent extends IAgent {

	// present list of all procedural actions; asi grounded
	
	// zamyslet se i nad scoringem?
	
	/**
	 * Hero's body update.
	 * @param heroBody
	 */
	public void observeBody(Hero heroBody, long timestampMillis);
	
	/**
	 * Information update about the dungeon.
	 * @param dungeon
	 * @param full whether the update is full; true == fully observable environment, false == partialy observable environment 
	 */
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis);
	
	/**
	 * Hero should decide what to do.
	 * 
	 * Return null for no-action.
	 * 
	 * @return
	 */
	public Command act();
	
}
