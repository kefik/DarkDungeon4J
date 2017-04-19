package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public abstract class FeatureAgentBase extends AgentBase implements IFeatureAgent {

	public FeatureAgentBase() {
		super(EEntity.FEATURE);
	}

	@Override
	public abstract Command act(Feature feature);

}
