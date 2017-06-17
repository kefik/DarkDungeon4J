package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;

public abstract class MonsterAgentBase extends AgentBase implements IMonsterAgent {

	public class MonsterActions {
		
		public Command attack() {
			return new Command(EAction.ATTACK, monster.atRoom.hero);
		}
		
		public Command move(Room toRoom) {
			return new Command(EAction.MOVE, toRoom);
		}
		
		public Command move(Id roomId) {
			for (Corridor corridor : monster.atRoom.corridors) {
				if (corridor.leadsTo(roomId)) return move(corridor.getRoom(roomId));					
			}
			return null;
		}
		
	}
	
	protected Dungeon dungeon;
	
	protected Monster monster;
	
	protected MonsterActions actions = new MonsterActions();

	public MonsterAgentBase() {
		super(EEntity.MONSTER);
	}

	@Override
	public void observeBody(Monster monster, long currentTickMillis) {
		this.monster = monster;
	}
	
	@Override
	public void observeDungeon(Dungeon dungeon, boolean full, long timestampMillis) {
		this.dungeon = dungeon;
	}
	
	@Override
	public abstract Command act();
	
}
