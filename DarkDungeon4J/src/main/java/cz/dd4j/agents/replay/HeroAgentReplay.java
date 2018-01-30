package cz.dd4j.agents.replay;

import cz.dd4j.agents.AgentBase;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroAgentReplay extends AgentBase implements IHeroAgent {

	private Replay replay;
	private Hero body;

	public HeroAgentReplay(Replay replay) {
		super(EEntity.HERO);
		this.replay = replay;
	}

	@Override
	public void observeBody(Hero heroBody, long timestampMillis) {
		this.body = heroBody;
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
		return "HeroAgentReplay";
	}
	

}
