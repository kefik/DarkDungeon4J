package cz.dd4j.agents.replay;

import cz.dd4j.agents.AgentBase;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;

public class MonsterAgentReplay extends AgentBase implements IMonsterAgent {

	private Replay replay;
	private Monster body;

	public MonsterAgentReplay(Replay replay) {
		super(EEntity.MONSTER);
		this.replay = replay;
	}

	@Override
	public void observeBody(Monster monsterBody, long currentTickMillis) {
		this.body = monsterBody;
	}

	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
	}

	@Override
	public Command act() {
		return replay.getAgentAction(body.id);
	}
	
	@Override
	public String toString() {
		return "MonsterAgentReplay";
	}

}
