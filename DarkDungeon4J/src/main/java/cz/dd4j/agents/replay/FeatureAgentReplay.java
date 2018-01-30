package cz.dd4j.agents.replay;

import cz.dd4j.agents.AgentBase;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public class FeatureAgentReplay extends AgentBase implements IFeatureAgent {

	private Replay replay;

	public FeatureAgentReplay(Replay replay) {
		super(EEntity.FEATURE);
		this.replay = replay;
	}

	@Override
	public Command act(Feature feature) {
		return replay.getAgentAction(feature.id);
	}

	@Override
	public String toString() {
		return "FeatureAgentReplay";
	}
	
}
