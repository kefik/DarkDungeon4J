package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;

public class MonsterAgentBase implements IMonsterAgent, IConfigurable {

	@Override
	public EEntity getAgentType() {
		return EEntity.MONSTER;
	}
	
	@Override
	public Command act(Room room, Corridor corridor) {
		return null;
	}

	@Override
	public void configure(ConfigMap config) {
	}

}
