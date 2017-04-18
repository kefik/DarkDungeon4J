package cz.dd4j.simulation.actions.instant.impl;

import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.instant.IFeatureInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActionBase;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public class FeatureAttackInstant extends InstantActionBase<Feature> implements IFeatureInstantAction {

	@Override
	public EEntity getEntity() {
		return EEntity.FEATURE;
	}
	
	@Override
	public EAction getType() {
		return EAction.ATTACK;
	}

	@Override
	public boolean isValid(Feature feature, Command action) {
		if (!super.isValid(feature, action)) return false;
		if (feature.atRoom.hero == null) return false;
		if (feature.atRoom.hero.action != null && feature.atRoom.hero.action.type == EAction.DISARM) return false;
		if (action.target != feature.atRoom.hero) return false;
		return true;
	}

	@Override
	public void run(Feature feature, Command action) {
		feature.atRoom.hero.alive = false;
		feature.action = null;
	}
	
	@Override
	public boolean generateActionsFor(Feature entity, List<Command> actionStore) {
		if (entity.atRoom.hero != null && (entity.atRoom.hero.action == null || entity.atRoom.hero.action.type != EAction.DISARM) ) {
			actionStore.add(new Command(EAction.ATTACK, entity.atRoom.hero));
			return true;
		}
		return false;
	}
	
}
