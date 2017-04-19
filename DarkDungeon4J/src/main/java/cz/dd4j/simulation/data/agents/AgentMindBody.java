package cz.dd4j.simulation.data.agents;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.IAgent;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public class AgentMindBody<ENTITY extends Entity, AGENT extends IAgent> {
	
	public ENTITY body;
	public AGENT mind;
	
	@Override
	public String toString() {
		if (body == null || body.type == null) return "AgentMindBody[body=" + (body == null ? "null" : body) + ",mind=" + (mind == null ? "null" : mind.getClass().getName()) + "]";
		return EH.name(body.type) + "[body=" + (body == null ? "null" : body) + ",mind=" + (mind == null ? "null" : mind.getClass().getName()) + "]";
	}

}
