package cz.dd4j.agents.monsters;

import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;

public class StaticMonster implements IMonsterAgent, IConfigurable {

	@Override
	public void configure(ConfigMap config) {
	}
	
	@Override
	public Action act(Room room, Corridor corridor) {		
		if (room.hero != null) return new Action(EAction.ATTACK, room.hero);
		return null;
	}

}
