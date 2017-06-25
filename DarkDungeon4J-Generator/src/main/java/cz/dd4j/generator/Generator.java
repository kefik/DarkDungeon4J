package cz.dd4j.generator;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.dd4j.generator.adventure.AdventureGenerator;
import cz.dd4j.generator.adventure.AdventureGeneratorConfig;
import cz.dd4j.generator.agents.AgentsGenerator;
import cz.dd4j.generator.agents.AgentsGeneratorConfig;
import cz.dd4j.generator.dungeon.corridors.CorridorsGeneratorConfig;
import cz.dd4j.generator.dungeon.corridors.GridCorridorsGenerator;
import cz.dd4j.generator.dungeon.corridors.TorusCorridorsGenerator;
import cz.dd4j.generator.dungeon.corridors.maze.MazeGenerator;
import cz.dd4j.generator.dungeon.corridors.maze.MazeGeneratorConfig;
import cz.dd4j.generator.dungeon.goals.GoalsGenerator;
import cz.dd4j.generator.dungeon.goals.GoalsGeneratorConfig;
import cz.dd4j.generator.dungeon.heroes.HeroesGenerator;
import cz.dd4j.generator.dungeon.heroes.HeroesGeneratorConfig;
import cz.dd4j.generator.dungeon.items.SwordsGenerator;
import cz.dd4j.generator.dungeon.items.SwordsGeneratorConfig;
import cz.dd4j.generator.dungeon.monsters.MonstersGenerator;
import cz.dd4j.generator.dungeon.monsters.MonstersGeneratorConfig;
import cz.dd4j.generator.dungeon.rooms.RoomsGenerator;
import cz.dd4j.generator.dungeon.rooms.RoomsGeneratorConfig;
import cz.dd4j.generator.dungeon.traps.TrapsGenerator;
import cz.dd4j.generator.dungeon.traps.TrapsGeneratorConfig;
import cz.dd4j.loader.agents.impl.xml.AgentXML;
import cz.dd4j.utils.config.GenericConfig;

public class Generator {

	private GenericConfig rootConfig;

	public Generator(GenericConfig config) {
		this.rootConfig = config;
		if (config.log == null) config.log = Logger.getAnonymousLogger();
	}
	
	public void generateRooms(int countFrom, int countTo) {
		RoomsGeneratorConfig config = new RoomsGeneratorConfig();		
		config.assign(rootConfig);
		
		config.roomsCountFrom = countFrom;
		config.roomsCountTo = countTo;
		
		RoomsGenerator generator = new RoomsGenerator(config);
		
		generator.generate();
	}
	
	public void generateGoals(int roomsCount) {
		GoalsGeneratorConfig config = new GoalsGeneratorConfig();
		
		config.roomsCount = roomsCount;		
		config.assign(rootConfig);
		
		GoalsGenerator generator = new GoalsGenerator(config);
		
		generator.generate();
	}
	
	public void generateGrid(int roomsCountFrom, int roomsCountTo) {
		CorridorsGeneratorConfig config = new CorridorsGeneratorConfig();		
		config.assign(rootConfig);
		
		config.roomsCountFrom = roomsCountFrom;
		config.roomsCountTo = roomsCountTo;
		
		GridCorridorsGenerator generator = new GridCorridorsGenerator(config);
		
		generator.generate();
	}
	
	public void generateTorus(int roomsCountFrom, int roomsCountTo) {
		CorridorsGeneratorConfig config = new CorridorsGeneratorConfig();		
		config.assign(rootConfig);
		
		config.roomsCountFrom = roomsCountFrom;
		config.roomsCountTo = roomsCountTo;
		
		TorusCorridorsGenerator generator = new TorusCorridorsGenerator(config);
		
		generator.generate();
	}
	
	public void generateMazes(int xFrom, int yFrom, int xTo, int yTo, int numberMazesPerDimension, int maxExtraJunctions) {
		MazeGeneratorConfig config = new MazeGeneratorConfig(xFrom, xTo, yFrom, yTo, numberMazesPerDimension, maxExtraJunctions);		
		config.assign(rootConfig);
		
		MazeGenerator generator = new MazeGenerator(config);
		
		generator.generate();		
	}
	
	public void generateMonsters(int monstersCount, int roomsCount) {
		MonstersGeneratorConfig config = new MonstersGeneratorConfig();
		
		config.monstersCount = monstersCount;
		config.roomsCount = roomsCount;		
		config.assign(rootConfig);
		
		MonstersGenerator generator = new MonstersGenerator(config);
		
		generator.generate();		
	}
	
	public void generateHeroes(int heroesCount, int roomsCount) {
		HeroesGeneratorConfig config = new HeroesGeneratorConfig();
		
		config.heroesCount = heroesCount;		
		config.roomsCount = roomsCount;		
		config.assign(rootConfig);
		
		HeroesGenerator generator = new HeroesGenerator(config);
		
		generator.generate();		
	}
	
	public void generateTraps(int trapsCount, int roomsCount) {
		TrapsGeneratorConfig config = new TrapsGeneratorConfig();
				
		config.trapsCount = trapsCount;
		config.roomsCount = roomsCount;
		config.assign(rootConfig);
		
		TrapsGenerator generator = new TrapsGenerator(config);
		
		generator.generate();
	}
	
	public void generateSwords(int swordsCount) {
		SwordsGeneratorConfig config = new SwordsGeneratorConfig();
		
		config.swordCount = swordsCount;		
		config.assign(rootConfig);
		
		SwordsGenerator generator = new SwordsGenerator(config);
		
		generator.generate();
	}
	
	public void generateAgents(String agentIdPrefix, String directory, int agentsCount, AgentXML... agentPrototypes) {
		if (agentPrototypes == null || agentPrototypes.length == 0) return;
		
		AgentsGeneratorConfig config = new AgentsGeneratorConfig();
		
		config.agentIdPrefix = agentIdPrefix;
		config.directory = directory;
		config.agentsCount = agentsCount;	
		config.agentPrototypes = new ArrayList<AgentXML>(agentPrototypes.length);
		for (AgentXML agentPrototype : agentPrototypes) {
			config.agentPrototypes.add(agentPrototype);
		}
		config.assign(rootConfig);
		
		AgentsGenerator generator = new AgentsGenerator(config);
		
		generator.generate();
	}
	
	/**
	 * @param resultDirSuffix must not include ".." or ".", must not end with "/"
	 */
	public void generateAdventures(String resultDirSuffix) {
		AdventureGeneratorConfig config = new AdventureGeneratorConfig();
		
		config.resultDir =  resultDirSuffix;
		config.assign(rootConfig);
		
		AdventureGenerator generator = new AdventureGenerator(config);
		
		generator.generate();
	}
	
	
}
