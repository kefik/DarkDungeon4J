package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.data.state.SimStateIds;

/**
 * This is a slim representation of runtime data of {@link SimState}.
 * 
 * I.e., it represent state of simulation bodies ({@link SimState#heroes}, {@link SimState#monsters}, {@link SimState#features}) and runtime state of {@link SimState#dungeon}.
 * 
 * It does not represent static information, e.g., {@link Dungeon} topology stored within {@link Room}s.
 * 
 * It is a tight fit onto the current implementation in order to make the state as small as possible in order to be able to {@link #clone()} it as fast as possible.
 * 
 * When storing a reference, we represent it using 10 bits only.
 * 
 * 
 * @author Jimmy
 */
public class SlimState implements Cloneable {

	/**
	 * See {@link SlimRoom} for bit-wise representation of respective ints; ordered according to "rooms ref" {@link SimStateIds#rooms()}.
	 */
	private int[] rooms;
	
	/**
	 * See {@link SlimMonster} for bit-wise representation of respective ints; ordered according to "monsters ref" {@link SimStateIds#monsters()}.
	 */
	private int[] monsters;
	
	/**
	 * See {@link SlimHero} for bit-wise representation of respective ints; ordered according to "heroes ref" {@link SimStateIds#heroes()}.
	 */
	private int[] heroes;
	
	
//  WE CONSIDER ALL FEATURES TO BE TRAPS
//	/**
//	 * ALIVE BIT|FEATURE ID|AT ROOM REF|0s
//	 */
//	private int[] features;
	
	public SlimState() {
	}
	
	/**
	 * Creates SlimState out of OOP {@link SimState}
	 * @param source
	 */
	public SlimState(SimState source) {
		int roomsCount = source.ids.roomIds().size();
		int monstersCount = source.ids.monsterIds().size();
		int heroesCount = source.ids.heroIds().size();

		// SANITY CHECKS
		if (roomsCount    > SlimBase.MAX_ROOMS)    throw new RuntimeException("Cannot turn SimState into SlimState. There are " + roomsCount    + " of rooms > " + SlimBase.MAX_ROOMS    + ", which is the maximum number of rooms SlimState supports.");
		if (monstersCount > SlimBase.MAX_MONSTERS) throw new RuntimeException("Cannot turn SimState into SlimState. There are " + monstersCount + " of rooms > " + SlimBase.MAX_MONSTERS + ", which is the maximum number of monsters SlimState supports.");
		if (heroesCount   > SlimBase.MAX_HEROES)   throw new RuntimeException("Cannot turn SimState into SlimState. There are " + heroesCount   + " of rooms > " + SlimBase.MAX_HEROES   + ", which is the maximum number of heroes SlimState supports.");
		
		rooms = new int[roomsCount+1];
		monsters = new int[monstersCount+1];
		heroes = new int[heroesCount+1];
		
		for (Room room : source.ids.rooms()) {
			if (room == null) continue;
			int roomRef = source.ids.roomRef(room);
			rooms[roomRef] = SlimRoom.fromSimState(source, room);
		}
		
		for (Monster monster : source.ids.monsters()) {
			if (monster == null) continue;
			int monsterRef = source.ids.monsterRef(monster);
			monsters[monsterRef] = SlimMonster.fromSimState(source, monster);
		}
		
		for (Hero hero : source.ids.heroes()) {
			if (hero == null) continue;
			int heroRef = source.ids.heroRef(hero);
			heroes[heroRef] = SlimHero.fromSimState(source, hero);
		}
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
