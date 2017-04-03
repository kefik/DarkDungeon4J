package cz.dd4j.simulation.actions.instant.impl;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.actions.instant.IFeatureInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.features.Feature;

public class FeatureAttackInstant implements IFeatureInstantActionExecutor {

	@Override
	public EEntity getEntity() {
		return EEntity.FEATURE;
	}
	
	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Feature feature, Action action) {
		if (feature.type != EFeature.TRAP) return false;
		if (feature.atRoom.hero == null) return false;
		if (feature.atRoom.hero.action != null && feature.atRoom.hero.action.type == EAction.DISARM) return false;
		if (action.target != feature.atRoom.hero) return false;
		return true;
	}

	@Override
	public void run(Feature feature, Action action) {
		feature.atRoom.hero.alive = false;
		feature.action = null;
	}
	
}
