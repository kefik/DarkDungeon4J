package cz.dd4j.generator.dungeon.monsters;

import cz.dd4j.generator.GeneratorConfig;

public class MonstersGeneratorConfig extends GeneratorConfig {

	/**
	 * Generates monster'X', where X is from [1; monstersCount].
	 */
	public int monstersCount;
		
	/**
	 * Generates all monster'X' into room'Y' where Y is from [1; roomsCount].  
	 */
	public int roomsCount;
	
}
