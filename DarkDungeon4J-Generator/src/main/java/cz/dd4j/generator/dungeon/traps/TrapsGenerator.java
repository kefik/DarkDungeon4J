package cz.dd4j.generator.dungeon.traps;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.FeatureXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;


public class TrapsGenerator extends GeneratorBase<TrapsGeneratorConfig> {
	
	public TrapsGenerator(TrapsGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(trap1->" + config.trapsCount + " into room1->" + config.roomsCount + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int trapId = 1; trapId <= config.trapsCount; ++trapId) {
			for (int roomId = 1; roomId <= config.roomsCount; ++roomId) {
				generate(trapId, roomId);
			}
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int trapId, int roomId) {
		File targetFile = config.getTargetFile("/traps", "Trap" + trapId + "-Room" + roomId + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(trap " + trapId + " for room " + roomId + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		
		RoomXML room = GeneratorUtils.generateRoom(roomId);
		room.feature = GeneratorUtils.generateTrap(trapId);
		
		dungeon.rooms = new ArrayList<RoomXML>();
		dungeon.rooms.add(room);
		
		write(targetFile, dungeon, DungeonLoaderXML.class, room.feature.name + Const.NEW_LINE + "Requires room: room" + roomId);
	}

}
