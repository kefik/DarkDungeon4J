package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public abstract class MonsterAgentBase extends AgentBase implements IMonsterAgent {

	protected Monster monster;

	public MonsterAgentBase() {
		super(EEntity.MONSTER);
	}

	@Override
	public void observeBody(Monster monster, long currentTickMillis) {
		this.monster = monster;
	}
	
	@Override
	public abstract Command act();
	
}
