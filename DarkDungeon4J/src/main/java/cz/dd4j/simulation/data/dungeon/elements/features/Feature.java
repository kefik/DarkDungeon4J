package cz.dd4j.simulation.data.dungeon.elements.features;

import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public abstract class Feature extends Entity implements IFeatureAgent {
		
	public Feature(EFeature type) {
		super(type);
	}
	
	@Override
	public abstract Action act();

}
