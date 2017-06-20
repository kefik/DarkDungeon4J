package cz.dd4j.generator.adventure;

import cz.dd4j.generator.GeneratorConfig;
import cz.dd4j.generator.adventure.callbacks.AdventureSerializerCallback;

/**
 * We assume that source files are going to be read relatively to the same path as the result is going to be written.
 * 
 * DO NOT USE "." or ".." IN THE DIR PATHS!
 * 
 * DO NOT USE "\\" IN THE DIR PATHS! Use "/" only!
 * 
 * DO NOT END PATHS WITH "/"!
 * 
 * @author Jimmy
 */
public class AdventureGeneratorConfig extends GeneratorConfig {

	public String roomsDir = "rooms";
	
	public String corridorsDir = "corridors/grid";
	
	public String goalsDir = "goals";
	
	public String itemsDir = "items";
	
	public String heroesDir = "heroes";
	
	public String monstersDir = "monsters";
	
	public String trapsDir = "traps";			
	
	public String agentMonstersDir = "agents/monsters";
	
	public String agentTrapsDir = "agents/traps";
	
	public String resultDir = "adventures/grid";
	
	public Range monsters = new Range(1,2);
	public boolean monstersOfTheSameType = true;
	
	public Range items = new Range(1,1);
	
	public Range traps = new Range(1,1);
	
	// FILTERS AND CALLBACKS
	
	public IAdventureFilter[] filters;
	
	public IAdventureCallback[] callback = new IAdventureCallback[]{ new AdventureSerializerCallback() };	
}
