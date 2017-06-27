package cz.dd4j.simulation.result;

import java.util.ArrayList;
import java.util.List;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.exceptions.AgentException;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.ExceptionToString;
import cz.dd4j.utils.csv.CSV.CSVRow;
import cz.dd4j.utils.reporting.IReporting;

public class SimResult implements IReporting {

	public SimResultType resultType;
	
	public AgentMindBody<Hero, IHeroAgent> winner;
	
	public long frameNumber;
	
	public long simTimeMillis;
	
	public Throwable exception;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (resultType == null) sb.append("SimResult[resultType=null,");
		else {
			sb.append(resultType);
			sb.append("[");
		}
		sb.append("frameNumber=");
		sb.append(frameNumber);
		sb.append(",simTimeMillis=");
		sb.append(simTimeMillis);
		if (winner != null) {
			sb.append(",winner=");
			sb.append(winner);
		}
		sb.append("]");
		if (exception != null) {
			sb.append(Const.NEW_LINE);
			AgentMindBody agent = getExceptionEntityType();
			if (agent != null) {
				sb.append("Failure caused by agent: " + agent);
				sb.append(Const.NEW_LINE);
			}
			sb.append(ExceptionToString.process(exception));
		}
		return sb.toString();
	}
	
	/**
	 * In case of {@link AgentException}, we can extract {@link AgentException#agent} that has caused the failure.
	 * @return
	 */
	public AgentMindBody<? extends Entity, ? extends IAgent> getExceptionEntityType() {
		if (exception != null && exception instanceof AgentException) return ((AgentException)exception).agent;
		return null;
	}
	
	// =========
	// REPORTING
	// =========
	
	public static final String CSV_TYPE = "RES-type";
	public static final String CSV_FRAME = "RES-frame";
	public static final String CSV_TIME_MS = "RES-time_ms";

	@Override
	public List<String> getCSVHeaders() {
		List<String> result = new ArrayList<String>();
		result.add(CSV_TYPE);
		result.add(CSV_FRAME);
		result.add(CSV_TIME_MS);
		return result;
	}

	@Override
	public CSVRow getCSVRow() {
		CSVRow row = new CSVRow();
		
		row.add(CSV_TYPE, resultType.toString());
		row.add(CSV_FRAME, frameNumber);
		row.add(CSV_TIME_MS, simTimeMillis);
		
		return row;
	}
	
}
