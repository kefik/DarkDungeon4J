package cz.dd4j.agents;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;

public abstract class HeroAgentBase extends AgentBase implements IHeroAgent {
	
	public class HeroActions {
		
		public Command disarm() {
			return new Command(EAction.DISARM, hero.atRoom.feature);
		}
		
		public Command pickup() {
			return new Command(EAction.PICKUP, hero.atRoom.item);
		}
		
		public Command drop() {
			return new Command(EAction.DROP, hero.hand);
		}
		
		public Command attack() {
			return new Command(EAction.ATTACK, hero.atRoom.monster);
		}
		
		public Command move(Room toRoom) {
			return new Command(EAction.MOVE, toRoom);
		}
		
		public Command move(Id roomId) {
			for (Corridor corridor : hero.atRoom.corridors) {
				if (corridor.leadsTo(roomId)) return move(corridor.getRoom(roomId));					
			}
			return null;
		}
		
		public Command action(EAction type, Element... params) {
			switch (type) {
			case ATTACK: return attack();
			case DISARM: return disarm();
			case DROP:   return drop();
			case PICKUP: return pickup();
			case MOVE:   return move((Room)params[0]);
			default:
				throw new RuntimeException("Unsupported action type: " + type);
			}
		}
		
		
	}
	
	protected Hero hero;
	
	protected HeroActions actions = new HeroActions();

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
