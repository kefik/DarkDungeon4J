package cz.dd4j.generator;

import java.util.logging.Logger;

import cz.dd4j.generator.dungeon.CorridorsGeneratorConfig;
import cz.dd4j.generator.dungeon.GridCorridorsGenerator;
import cz.dd4j.generator.dungeon.RoomsGenerator;
import cz.dd4j.generator.dungeon.RoomsGeneratorConfig;
import cz.dd4j.generator.dungeon.SphereCorridorsGenerator;
import cz.dd4j.generator.dungeon.corridors.maze.MazeCorridorsGenerator;
import cz.dd4j.generator.dungeon.corridors.maze.MazeGeneratorConfig;

public class Generator {

	private GeneratorConfig rootConfig;

	public Generator(GeneratorConfig config) {
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
	
	public void generateGrid(int roomsCountFrom, int roomsCountTo) {
		CorridorsGeneratorConfig config = new CorridorsGeneratorConfig();		
		config.assign(rootConfig);
		
		config.roomsCountFrom = roomsCountFrom;
		config.roomsCountTo = roomsCountTo;
		
		GridCorridorsGenerator generator = new GridCorridorsGenerator(config);
		
		generator.generate();
	}
	
	public void generateSphere(int roomsCountFrom, int roomsCountTo) {
		CorridorsGeneratorConfig config = new CorridorsGeneratorConfig();		
		config.assign(rootConfig);
		
		config.roomsCountFrom = roomsCountFrom;
		config.roomsCountTo = roomsCountTo;
		
		SphereCorridorsGenerator generator = new SphereCorridorsGenerator(config);
		
		generator.generate();
	}
	
	public void generateMazes(int xFrom, int yFrom, int xTo, int yTo, int numberMazesPerDimension, int maxExtraJunctions) {
		MazeGeneratorConfig config = new MazeGeneratorConfig(xFrom, xTo, yFrom, yTo, numberMazesPerDimension, maxExtraJunctions);		
		config.assign(rootConfig);
		
		MazeCorridorsGenerator generator = new MazeCorridorsGenerator(config);
		
		generator.generate();
		
	}
	
	
}
