package cz.dd4j.simulation.data.agents;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.agents.IAgent;
import cz.dd4j.utils.Id;

public class Agents<AGENT extends IAgent> {

	public Map<Id, AGENT> agents = new HashMap<Id, AGENT>();
	
}
