package cz.dd4j.agents.features;

import cz.dd4j.agents.FeatureAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;

public class TrapAgent extends FeatureAgentBase {

	@Override
	public EEntity getAgentType() {
		return EFeature.TRAP;
	}
	
	@Override
	public Command act(Feature feature) {
		if (feature.atRoom == null) return null;
		if (feature.atRoom.hero == null) return null;
		if (feature.atRoom.hero.action == null || feature.atRoom.hero.action.type != EAction.DISARM) {
			// SPRING!
			return new Command(EAction.ATTACK, feature.atRoom.hero);
		}
		
		// OTHERWISE, do nothing...
		return null;
	}
	
	@Override
	public String toString() {
		return "TrapAgent";
	}

}
