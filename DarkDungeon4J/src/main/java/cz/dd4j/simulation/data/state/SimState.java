package cz.dd4j.simulation.data.state;

import java.util.HashMap;
import java.util.Map;

import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.utils.Id;

/**
 * Current state of the simulation as used by simulator, e.g., ({@link SimStatic}.
 * 
 * @author Jimmy
 */
public class SimState {
	
	// ===========
	// DESCRIPTION
	// ===========
	
	public SimStaticConfig config;

	// =============
	// DUNGEON STATE
	// =============
	
	public Dungeon dungeon;
		
	// ======
	// AGENTS
	// ======
	
	public Map<Id, AgentMindBody<Monster, IMonsterAgent>> monsters = new HashMap<Id, AgentMindBody<Monster, IMonsterAgent>>();
	public Map<Id, AgentMindBody<Hero,    IHeroAgent>>    heroes   = new HashMap<Id, AgentMindBody<Hero,    IHeroAgent>>();
	public Map<Id, AgentMindBody<Feature, IFeatureAgent>> features = new HashMap<Id, AgentMindBody<Feature, IFeatureAgent>>();
	
	// =======
	// TIMEOUT
	// =======
	
	/**
	 * Timeout; the simulation will run for this number of rounds before it times out.
	 */
	public int roundsLeft = 1000;
	
	public AgentMindBody getAgentMindBody(Id id) {
		if (heroes.containsKey(id)) return heroes.get(id);
		if (monsters.containsKey(id)) return monsters.get(id);
		return features.get(id);
	}
	
}
