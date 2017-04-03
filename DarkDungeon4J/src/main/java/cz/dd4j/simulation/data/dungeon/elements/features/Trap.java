package cz.dd4j.simulation.data.dungeon.elements.features;

import cz.dd4j.domain.EFeature;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;

public class Trap extends Feature {

	public Trap() {
		super(EFeature.TRAP);
	}

	@Override
	public Action act() {
		if (atRoom == null) return null;
		if (atRoom.hero == null) return null;
		if (atRoom.hero.action != null && atRoom.hero.action.type != EAction.DISARM) {
			// SPRING!
			return new Action(EAction.ATTACK, atRoom.hero);
		}
		
		// OTHERWISE, do nothing...
		return null;		
	}

}
