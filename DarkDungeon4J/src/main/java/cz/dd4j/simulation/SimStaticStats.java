package cz.dd4j.simulation;

import java.util.Map;

import cz.dd4j.agents.IAgent;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.utils.collection.IntMap;
import cz.dd4j.utils.collection.LazyMap;

/**
 * All values are read-only during the simulation!
 * @author Jimmy
 */
public class SimStaticStats {

	/**
	 * Original {@link SimStatic} configuration containing the state of the simulation.
	 */
	public SimStaticConfig config;
		
	public long frameNumber;

	public long simulationStartMillis;

	public long currentTickMillis;
	
	/**
	 * How many {@link EAction} different agents selected for the execution.
	 */
	public Map<IAgent, IntMap<EAction>> actionSelectedStats = new LazyMap<IAgent, IntMap<EAction>>() {
		@Override
		protected IntMap<EAction> create(Object key) {
			return new IntMap<EAction>();
		}
	};
	
	/**
	 * How many {@link EAction} different agents executed.
	 */
	public Map<IAgent, IntMap<EAction>> actionExecutedStats = new LazyMap<IAgent, IntMap<EAction>>() {
		@Override
		protected IntMap<EAction> create(Object key) {
			return new IntMap<EAction>();
		}
	};
	
	public long simMillis() {
		return currentTickMillis - simulationStartMillis;
	}
	
}
