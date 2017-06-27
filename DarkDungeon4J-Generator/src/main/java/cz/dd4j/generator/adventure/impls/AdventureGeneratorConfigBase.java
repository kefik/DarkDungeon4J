package cz.dd4j.generator.adventure.impls;

import java.io.File;

import cz.dd4j.generator.adventure.IAdventureCallback;
import cz.dd4j.generator.adventure.IAdventureFilter;
import cz.dd4j.generator.adventure.callbacks.AdventureSerializerCallback;
import cz.dd4j.utils.config.BidirConfig;
import cz.dd4j.utils.config.GenericConfig;

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
public class AdventureGeneratorConfigBase extends BidirConfig {

	public String roomsDir = "rooms";
	
	public String corridorsDir = "corridors/grid";
	
	public String goalsDir = "goals";
	
	public String itemsDir = "items";
	
	public String heroesDir = "heroes";
	
	public String monstersDir = "monsters";
	
	public String trapsDir = "traps";			
	
	public String agentMonstersDir = "agents/monsters";
	
	public String agentTrapsDir = "agents/traps";
	
	// FILTERS AND CALLBACKS
	
	public IAdventureFilter[] filters;
	
	public IAdventureCallback[] callback = new IAdventureCallback[]{ new AdventureSerializerCallback() };
	
	public AdventureGeneratorConfigBase() {
		source.dir = new File("result/");
		target.dir = new File("result/adventures/grid");
	}
}
