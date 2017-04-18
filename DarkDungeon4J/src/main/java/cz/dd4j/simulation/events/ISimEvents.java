package cz.dd4j.simulation.events;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.result.SimResult;

/**
 * Interface for triggering events as they are happening within the simulation.
 * 
 * Every method is "an event".
 * 
 * This can be (and is) used for: 1) logging, 2) visualization
 * 
 * @author Jimmy
 */
public interface ISimEvents {

	/**
	 * Marks the beginning of the simulation.
	 * @param state
	 */
	public void simulationBegin(SimState state);
	
	/**
	 * Simulation frame has begun.
	 * @param frameNumber
	 */
	public void simulationFrameBegin(long frameNumber, long simMillis);
	
	/**
	 * An 'action' has been chosen by 'who' for the execution.
	 * @param who
	 * @param what
	 */
	public void actionSelected(Element who, Command what);
	
	/**
	 * Action performed by some room's {@link Feature}.
	 * @param who
	 * @param what
	 * @param valid true == action carried out by the simulator, false == 'who' wanted to perform 'what' but it was invalid and thus did not produce any effect
	 */
	public void actionStarted(Element who, Command what);
	
	/**
	 * Action performed by some room's {@link Feature}.
	 * @param who
	 * @param what
	 * @param valid true == action carried out by the simulator, false == 'who' wanted to perform 'what' but it was invalid and thus did not produce any effect
	 */
	public void actionEnded(Element who, Command what);
	
	/**
	 * 'who' wanted to perform 'what' but such an action is invalid and was not carried out.
	 * @param who
	 * @param what
	 */
	public void actionInvalid(Element who, Command what);
	
	/**
	 * New {@link Element} appeared within the simulation.
	 * @param feature
	 */
	public void elementCreated(Element element);
	
	/**
	 * {@link Element} destroyed.
	 * @param feature
	 */
	public void elementDead(Element element);

	/**
	 * Simulation frame has ended.
	 * @param frameNumber
	 */
	public void simulationFrameEnd(long frameNumber);
	
	/**
	 * Simulation has ended with {@link SimResult}.
	 * @param result
	 */
	public void simulationEnd(SimResult result);
	
}
