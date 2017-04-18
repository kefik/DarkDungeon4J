package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;

public class FeatureAgentBase implements IFeatureAgent, IConfigurable {

	@Override
	public EEntity getAgentType() {
		return EEntity.FEATURE;
	}
	
	@Override
	public void configure(ConfigMap config) {
	}
	
	@Override
	public Command act(Feature feature) {
		return null;
	}

}
