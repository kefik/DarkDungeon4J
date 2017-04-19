package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public abstract class HeroAgentBase extends AgentBase implements IHeroAgent {
	
	protected Hero hero;

	public HeroAgentBase() {
		super(EEntity.HERO);
	}

	@Override
	public void observeBody(Hero hero, long timestampMillis) {
		this.hero = hero;
	}

	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
	}

	@Override
	public abstract Command act();

}
