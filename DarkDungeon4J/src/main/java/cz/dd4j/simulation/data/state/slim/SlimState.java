package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;

/**
 * This is a slim representation of runtime data of {@link SimState}.
 * 
 * I.e., it represent state of simulation bodies ({@link SimState#heroes}, {@link SimState#monsters}, {@link SimState#features}) and runtime state of {@link SimState#dungeon}.
 * 
 * It does not represent static information, e.g., {@link Dungeon} topology stored within {@link Room}s.
 * 
 * It is a tight fit onto the current implementation in order to make the state as small as possible in order to be able to {@link #clone()} it as fast as possible.
 * 
 * 
 * 
 * @author Jimmy
 */
public class SlimState implements Cloneable {

	/**
	 * GOAL BIT|TRAP BIT|ROOM ID|HERO REF|MONSTER REF
	 */
	private int[] rooms;
	
	/**
	 * ALIVE BIT|MONSTER ID|AT ROOM REF|0s
	 */
	private int[] monsters;
	
	/**
	 * ALIVE BIT|HERO ID|AT ROOM REF|HAND ITEM ID|0
	 */
	private int[] heroes;
	
	
//  WE CONSIDER ALL FEATURES TO BE TRAPS
//	/**
//	 * ALIVE BIT|FEATURE ID|AT ROOM REF|0s
//	 */
//	private int[] features;
	
	public SlimState(SimState source) {
		
	}
	
	public boolean roomIsGoal(int room) {
		return (room >> 31) > 0;
	}
	
	public boolean roomIsTrap(int room) {
		return ((room << 1) >> 31) > 0;
	}
	
	public int roomId(int room) {
		return (room << 11) >> 22;
	}
	
	public int roomHeroRef(int room) {
		return (room << 21) >> 22;
	}
	
	/**
	 * One of the fastest implementation we can get...
	 */
	@Override	
	public SlimState clone() {
		SlimState result = new SlimState(null);
		result.rooms = rooms.clone();
		result.monsters = monsters.clone();
		result.heroes = heroes.clone();
		return result;
	}
	
	
	
}
