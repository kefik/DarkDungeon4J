package cz.dd4j.generator.dungeon.rooms;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;


public class RoomsGenerator extends GeneratorBase<RoomsGeneratorConfig> {
	
	public RoomsGenerator(RoomsGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info("RoomsGenerator.generate()");
		
		for (int i = config.roomsCountFrom; i <= config.roomsCountTo; ++i) {
			generate(i);
		}
		
		config.log.info("RoomsGenerator.generate(): DONE!");
	}

	private void generate(int roomCount) {
		File targetFile = config.target.getFile("/rooms", "Rooms" + roomCount + ".xml");
		
		config.log.info("RoomsGenerator.generate(" + roomCount + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		dungeon.rooms = new ArrayList<RoomXML>(roomCount);
		
		for (int i = 1; i <= roomCount; ++i) {
			dungeon.rooms.add(GeneratorUtils.generateRoom(i));
		}
		
		write(targetFile, dungeon, DungeonLoaderXML.class, "#Rooms: " + roomCount + Const.NEW_LINE + "Room id format: " + GeneratorUtils.roomId(1) + ", " + GeneratorUtils.roomId(2) + ", ..., " + GeneratorUtils.roomId(roomCount));
	}

	

}
