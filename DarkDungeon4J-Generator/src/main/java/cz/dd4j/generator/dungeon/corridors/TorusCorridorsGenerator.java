package cz.dd4j.generator.dungeon.corridors;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.domain.EDungeonLabel;
import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.CorridorXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.config.ConfigXML;

public class TorusCorridorsGenerator extends GeneratorBase<CorridorsGeneratorConfig> {

	public TorusCorridorsGenerator(CorridorsGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info("TorusCorridorsGenerator.generate()");
		
		for (int i = config.roomsCountFrom; i <= config.roomsCountTo; ++i) {
			generate(i);
		}
		
		config.log.info("TorusCorridorsGenerator.generate(): DONE!");
	}

	private void generate(int roomCount) {
		File targetFile = config.target.getFile("/corridors/torus", "Torus" + roomCount + ".xml");
		
		config.log.info("TorusCorridorsGenerator.generate(" + roomCount + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		dungeon.corridors = new ArrayList<CorridorXML>();
		dungeon.labels = new ArrayList<ConfigXML>();
		
		// GENERATE GRID
		int width = (int)Math.ceil(Math.sqrt(roomCount));
		int height = (roomCount % width == 0 ? roomCount / width : roomCount / width + 1);
		
		dungeon.labels.add(new ConfigXML(EDungeonLabel.TOPOLOGY_TYPE, EDungeonLabel.TOPOLOGY_TYPE_VALUE_TORUS));
		dungeon.labels.add(new ConfigXML(EDungeonLabel.TOPOLOGY_ROOMS_COUNT, roomCount));
		dungeon.labels.add(new ConfigXML(EDungeonLabel.TOPOLOGY_ROOMS_WIDTH, width));
		dungeon.labels.add(new ConfigXML(EDungeonLabel.TOPOLOGY_ROOMS_HEIGHT, height));
		
		int currRoom = 1;
		for (int j = 1; j <= height; ++j) {
			if (currRoom > roomCount) break;
			for (int i = 1; i <= width; ++i) {			
				if (currRoom > roomCount) break;
				
				int roomRight = (i == width ? (j-1)*width+1 : currRoom+1);
				if (!isValidRoom(roomRight, roomCount)) roomRight = (j-1)*width+1;
				if (isValidRoom(roomRight, roomCount)) {
					if (roomRight+1 != currRoom) {
						dungeon.corridors.add(GeneratorUtils.generateCorridor(currRoom, roomRight, GeneratorUtils.roomId(currRoom).name + " == [" + i + "," + j + "] -- link RIGHT -- [" + ((roomRight-1) % width + 1) + "," + j + "] == " + GeneratorUtils.roomId(roomRight).name));
					}
				}
				
				int roomDown = (j == height ? i : currRoom+width);
				if (!isValidRoom(roomDown, roomCount)) roomDown = i;
				if (isValidRoom(roomDown, roomCount)) {
					if (roomDown + width != roomCount) {
						dungeon.corridors.add(GeneratorUtils.generateCorridor(currRoom, roomDown, GeneratorUtils.roomId(currRoom).name + " == [" + i + "," + j + "] -- link DOWN -- [" + i + "," + ((roomDown-1) / width + 1) + "] == " + GeneratorUtils.roomId(roomDown).name));
					}
				}	
				
				++currRoom;
			}
		}
		
		write(targetFile, dungeon, DungeonLoaderXML.class, "Torus (Width x Height): " + width + " x " + height + (width * height != roomCount ? " (incomplete)" : "") + Const.NEW_LINE + "Requires: Rooms" + roomCount + ".xml");
	}
	
	private boolean isValidRoom(int room, int roomCount) {
		return room >= 1 && room <= roomCount;
	}

	

}
