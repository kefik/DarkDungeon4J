package cz.dd4j.simulation.result;

import cz.dd4j.simulation.data.state.HeroMindBody;

public class SimResult {

	public SimResultType resultType;
	
	public HeroMindBody winner;
	
	public long frameNumber;
	
	public long simTimeMillis;
	
	@Override
	public String toString() {
		return resultType + "[frameNumber=" + frameNumber + ",simTimeMillis=" + simTimeMillis + ",winner=" + winner + "]";
	}
	
}
