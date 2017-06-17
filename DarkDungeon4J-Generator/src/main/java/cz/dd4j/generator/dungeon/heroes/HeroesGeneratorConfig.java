package cz.dd4j.generator.dungeon.heroes;

import cz.dd4j.generator.GeneratorConfig;

public class HeroesGeneratorConfig extends GeneratorConfig {

	/**
	 * Generates hero'X', where X is from [1; heroesCount].
	 */
	public int heroesCount;
		
	/**
	 * Generates all hero'X' into room'Y' where Y is from [1; roomsCount].  
	 */
	public int roomsCount;
	
}
