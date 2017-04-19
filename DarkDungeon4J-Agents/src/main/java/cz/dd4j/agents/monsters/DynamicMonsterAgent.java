package cz.dd4j.agents.monsters;

import cz.dd4j.agents.MonsterAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

@AutoConfig
public class DynamicMonsterAgent extends MonsterAgentBase {

	@Configurable
	private double movementProbability;
	
	@Override
	public Command act() {		
		if (monster.atRoom == null) return null;
		if (monster.atRoom.hero != null) return new Command(EAction.ATTACK, monster.atRoom.hero);
		if (random.nextDouble() < movementProbability) {
			return getRandomAction(monster, EAction.MOVE, false);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "DynamicMonsterAgent[moveProb=" + movementProbability + "]";
	}

}
