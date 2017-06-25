package cz.dd4j.generator.dungeon.heroes;

import cz.dd4j.utils.config.GenericConfig;

public class HeroesGeneratorConfig extends GenericConfig {

	/**
	 * Generates hero'X', where X is from [1; heroesCount].
	 */
	public int heroesCount;
		
	/**
	 * Generates all hero'X' into room'Y' where Y is from [1; roomsCount].  
	 */
	public int roomsCount;
	
}
