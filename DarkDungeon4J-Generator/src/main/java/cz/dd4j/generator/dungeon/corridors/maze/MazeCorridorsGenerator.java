package cz.dd4j.generator.dungeon.corridors.maze;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.CorridorXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.utils.Const;

/**
 * Maze can be generated only for full rectangles!
 * 
 * Minimum number of maze cells per dimension: 3
 * 
 * Always generate mazes so width >= height
 * 
 * Example dimensions: 3x3, 5x3, 10x8, 20x15
 * 
 * Number of required rooms depends on the maze generated; rooms are numbered row-wise.
 * 
 * Example of room numbering.
 * 
 * XXXXX
 * X1X2X
 * X345X
 * XXXXX
 * 
 * @author Jimmy
 */
public class MazeCorridorsGenerator extends GeneratorBase<MazeGeneratorConfig> {

	public MazeCorridorsGenerator(MazeGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int height = config.yFrom; height <= config.yTo; ++height) {
			for (int width = height; width <= config.xTo; ++width) {			
				Maze.random = new Random(width * height);
				for (int i = 0; i < config.numberMazesPerDimension; ++i) {
					generate(width, height, i, config.maxExtraJunctions);
				}
			}	
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int width, int height, int number, int maxExtraJunctions) {			
		config.log.info(getClass().getSimpleName() + ".generate(" + width + "x" + height + "-" + number + "): generating...");		
		
		// GENERATE MAZE
		Maze maze = new Maze(width, height);					
		
		for (int extraJunctions = 0; extraJunctions <= maxExtraJunctions; ++extraJunctions) {
			config.log.info(getClass().getSimpleName() + ".generate(" + width + "x" + height + "-" + number + "): generating extra junctions " + extraJunctions + " / " + maxExtraJunctions + " ...");
			
			// GENERATE EXTRA JUNCTIONS
			if (!maze.placeExtraJunction()) {
				config.log.info(getClass().getSimpleName() + ".generate(" + width + "x" + height + "-" + number + "): no extra junctions possible, skipping.");
				break;
			}
			
			// TARGET FILE TO SAVE
			File targetFile = config.getTargetFile("/corridors/maze", "Maze-" + width + "x" + height +"-V" + number + "-EJ" + extraJunctions + ".xml");
			
			DungeonXML dungeon = new DungeonXML();
			dungeon.corridors = new ArrayList<CorridorXML>();
			
			// COUNT REQUIRED ROOMS
			Map<Tuple2, Integer> rooms = new HashMap<Tuple2, Integer>();		
			for (int y = 1; y <= height; ++y) {
				for (int x = 1; x <= width; ++x) {
					if (maze.isWall(x-1, y-1)) continue;
					rooms.put(new Tuple2(x, y), rooms.size()+1);
				}
			}
			int roomsRequired = rooms.size();
			
			// GENERATE CORRDIRORS
			for (int y = 1; y <= height; ++y) {
				int cellY = y - 1;
				for (int x = 1; x <= width; ++x) {
					int cellX = x - 1;
					
					if (maze.isWall(cellX, cellY)) continue;
					int roomNumber = rooms.get(new Tuple2(x, y));
					
					// CORRIDOR TO THE RIGHT
					if (!maze.isWall(cellX, cellY) && !maze.isWall(cellX+1, cellY) && !maze.isWallBetween(cellX, cellY, cellX+1, cellY)) {
						int roomRight = rooms.get(new Tuple2(x+1, y));					
						dungeon.corridors.add(GeneratorUtils.generateCorridor(roomNumber, roomRight, GeneratorUtils.roomId(roomNumber).name + " == [" + x + "," + y + "] -- link RIGHT -- [" + (x+1) + "," + y + "] == " + GeneratorUtils.roomId(roomRight).name));
					}
					
					// CORRIDOR DOWN
					if (!maze.isWall(cellX, cellY) && !maze.isWall(cellX, cellY+1) && !maze.isWallBetween(cellX, cellY, cellX, cellY+1)) {
						int roomDown = rooms.get(new Tuple2(x, y+1));					
						dungeon.corridors.add(GeneratorUtils.generateCorridor(roomNumber, roomDown, GeneratorUtils.roomId(roomNumber).name + " == [" + x + "," + y + "] -- link RIGHT -- [" + x + "," + (y+1) + "] == " + GeneratorUtils.roomId(roomDown).name));
					}				
				}
			}
			
			// SAVE IT!
			
			String mazeDesc = maze.getDescriptionCompactWithRooms(); 
			
			String comment = "Maze (Width x Height): " + width + " x " + height + Const.NEW_LINE;
			comment += "Extra junctions: " + extraJunctions + Const.NEW_LINE;
			comment +=  mazeDesc;				
			comment += "Requires: Rooms" + roomsRequired + ".xml"; 
			
			write(targetFile, dungeon, DungeonLoaderXML.class, comment);
			
			config.log.info("Generated maze (#rooms = " + roomsRequired + ") ---v" + Const.NEW_LINE + mazeDesc);
		}
	}

}
