package cz.dd4j.agents.monsters;

import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.MonsterAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;

public class StaticMonsterAgent extends MonsterAgentBase {

	@Override
	public void configure(ConfigMap config) {
	}
	
	@Override
	public Command act(Room room, Corridor corridor) {		
		if (room.hero != null) return new Command(EAction.ATTACK, room.hero);
		return null;
	}
	
	@Override
	public String toString() {
		return "StaticMonsterAgent";
	}

}
