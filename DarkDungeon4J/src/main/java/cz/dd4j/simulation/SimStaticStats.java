package cz.dd4j.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.utils.collection.IntMap;
import cz.dd4j.utils.collection.LazyMap;
import cz.dd4j.utils.csv.CSV.CSVRow;
import cz.dd4j.utils.reporting.IReporting;

/**
 * All values are read-only during the simulation!
 * @author Jimmy
 */
public class SimStaticStats implements IReporting {

	/**
	 * Original {@link SimStatic} configuration containing the state of the simulation.
	 */
	public SimStaticConfig config;
	
	/**
	 * Result of the simulation; non-null only iff simulation finished.
	 */
	public SimResult simulationResult;
	
	/**
	 * Current (or last) frame number of the simulation.
	 */
	public long frameNumber;

	/**
	 * Time when the simulation started.
	 */
	public long simulationStartMillis;

	/**
	 * Time since the simulation started.
	 */
	public long currentTickMillis;
	
	/**
	 * How many {@link EAction} different agents selected for the execution.
	 */
	public Map<AgentMindBody, IntMap<EAction>> actionSelectedStats = new LazyMap<AgentMindBody, IntMap<EAction>>() {
		@Override
		protected IntMap<EAction> create(Object key) {
			return new IntMap<EAction>();
		}
	};
	
	/**
	 * How many {@link EAction} different agents executed.
	 */
	public Map<AgentMindBody, IntMap<EAction>> actionExecutedStats = new LazyMap<AgentMindBody, IntMap<EAction>>() {
		@Override
		protected IntMap<EAction> create(Object key) {
			return new IntMap<EAction>();
		}
	};
	
	public long simMillis() {
		return currentTickMillis - simulationStartMillis;
	}
	
	// =========
	// REPORTING
	// =========
	
	public static final String CSV_TYPE = "RES-type";
	public static final String CSV_FRAME = "RES-frame";
	public static final String CSV_TIME_MS = "RES-time_ms";

	private List<AgentMindBody> getAgentMindBodiesSorted() {
		List<AgentMindBody> result = new ArrayList<AgentMindBody>();
		
		result.addAll(actionSelectedStats.keySet());
		
		Collections.sort(result, new Comparator<AgentMindBody>() {

			@Override
			public int compare(AgentMindBody o1, AgentMindBody o2) {
				return o1.body.id.name.compareTo(o2.body.id.name);
			}
			
		});
		
		return result;		
	}
	
	@Override
	public List<String> getCSVHeaders() {
		List<String> result = new ArrayList<String>();
		
		if (config != null)           result.addAll(config.getCSVHeaders());
		if (simulationResult != null) result.addAll(simulationResult.getCSVHeaders());
		
		List<AgentMindBody> agents = getAgentMindBodiesSorted();
		
		for (AgentMindBody agent : agents) {
			for (EAction action : EAction.values()) {
				result.add("STATS-" + agent.body.id.name + "-SEL-" + action);
				result.add("STATS-" + agent.body.id.name + "-EXE-" + action);
			}
		}
		
		return result;
	}

	@Override
	public CSVRow getCSVRow() {
		CSVRow row = new CSVRow();
		
		if (config != null)           row.addAll(config.getCSVRow());
		if (simulationResult != null) row.addAll(simulationResult.getCSVRow());
		
		List<AgentMindBody> agents = getAgentMindBodiesSorted();
		
		for (AgentMindBody agent : agents) {
			for (EAction action : EAction.values()) {
				row.add("STATS-" + agent.body.id.name + "-SEL-" + action, actionSelectedStats.get(agent).get(action));
				row.add("STATS-" + agent.body.id.name + "-EXE-" + action, actionExecutedStats.get(agent).get(action));
			}
		}
		
		return row;
	}
	
}
