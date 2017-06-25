package cz.dd4j.generator.dungeon.traps;

import cz.dd4j.utils.config.GenericConfig;

public class TrapsGeneratorConfig extends GenericConfig {

	/**
	 * Generates trap'X', where X is from [1; trapsCount].
	 */
	public int trapsCount;
		
	/**
	 * Generates all trap'X' into room'Y' where Y is from [1; roomsCount].  
	 */
	public int roomsCount;
	
}
