package cz.dd4j.simulation.data.state;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.simulation.data.dungeon.Dungeon;

/**
 * Current state of the simulation as used by simulators ({@link SimStatic} and {@link SimDynamic}).
 * 
 * @author Jimmy
 */
public class SimState {
	
	// ===========
	// DESCRIPTION
	// ===========
	
	public String id;
	public String description;

	// =============
	// DUNGEON STATE
	// =============
	
	public Dungeon dungeon;
		
	// ======
	// AGENTS
	// ======
	
	public Map<Integer, MonsterMindBody> monsters = new HashMap<Integer, MonsterMindBody>();
	
	public Map<Integer, HeroMindBody> heroes = new HashMap<Integer, HeroMindBody>();
	
}
