package cz.dd4j.generator.adventure;

import cz.dd4j.generator.GeneratorConfig;

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
	
	public Range monsters = new Range(1,2);
	
	public Range items = new Range(1,2);
	
	public Range traps = new Range(1,2);
	
}
