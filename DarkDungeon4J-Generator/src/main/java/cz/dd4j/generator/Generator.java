package cz.dd4j.generator;

import java.util.logging.Logger;

import cz.dd4j.generator.dungeon.GridCorridorsGenerator;
import cz.dd4j.generator.dungeon.GridCorridorsGeneratorConfig;
import cz.dd4j.generator.dungeon.RoomsGenerator;
import cz.dd4j.generator.dungeon.RoomsGeneratorConfig;

public class Generator {

	private GeneratorConfig parentConfig;

	public Generator(GeneratorConfig parentConfig) {
		this.parentConfig = parentConfig;
		if (parentConfig.log == null) parentConfig.log = Logger.getAnonymousLogger();
	}
	
	public void generateRooms(int countFrom, int countTo) {
		RoomsGeneratorConfig config = new RoomsGeneratorConfig();		
		config.assign(parentConfig);
		
		config.roomsCountFrom = countFrom;
		config.roomsCountTo = countTo;
		
		RoomsGenerator generator = new RoomsGenerator(config);
		
		generator.generate();
	}
	
	public void generateGrid(int roomsCountFrom, int roomsCountTo) {
		GridCorridorsGeneratorConfig config = new GridCorridorsGeneratorConfig();		
		config.assign(parentConfig);
		
		config.roomsCountFrom = roomsCountFrom;
		config.roomsCountTo = roomsCountTo;
		
		GridCorridorsGenerator generator = new GridCorridorsGenerator(config);
		
		generator.generate();
	}
	
	
}
