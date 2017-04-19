package cz.dd4j.agents.monsters;

import cz.dd4j.agents.MonsterAgentBase;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;

public class StaticMonsterAgent extends MonsterAgentBase {
	
	@Override
	public Command act() {		
		if (monster.atRoom != null && monster.atRoom.hero != null) return new Command(EAction.ATTACK, monster.atRoom.hero);
		return null;
	}
	
	@Override
	public String toString() {
		return "StaticMonsterAgent";
	}	

}
