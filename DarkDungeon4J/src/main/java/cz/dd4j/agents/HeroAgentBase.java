package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;

public class HeroAgentBase implements IHeroAgent, IConfigurable {

	protected IActionsGenerator actionsGenerator;
	protected Hero hero;

	@Override
	public EEntity getAgentType() {
		return EEntity.HERO;
	}
	
	@Override
	public void configure(ConfigMap config) {
	}
	
	@Override
	public void setActionGenerator(IActionsGenerator actionGenerator) {
		this.actionsGenerator = actionGenerator;
	}

	@Override
	public void observeBody(Hero hero, long timestampMillis) {
		this.hero = hero;
	}

	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
	}

	@Override
	public Command act() {
		return null;
	}

}
