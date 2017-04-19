package cz.dd4j.simulation.result;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.exceptions.AgentException;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.ExceptionToString;

public class SimResult {

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
	
}
