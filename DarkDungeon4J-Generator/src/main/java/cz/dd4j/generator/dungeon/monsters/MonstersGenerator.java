package cz.dd4j.generator.dungeon.monsters;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;


public class MonstersGenerator extends GeneratorBase<MonstersGeneratorConfig> {
	
	public MonstersGenerator(MonstersGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(monster1->" + config.monstersCount + " into room1->" + config.roomsCount + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int monsterId = 1; monsterId <= config.monstersCount; ++monsterId) {
			for (int roomId = 1; roomId <= config.roomsCount; ++roomId) {
				generate(monsterId, roomId);	
			}
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int monsterId, int roomId) {
		File targetFile = config.target.getFile("/monsters", "Monster" + monsterId + "-Room" + roomId + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(monster " + monsterId + " for room " + roomId + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		
		RoomXML room = GeneratorUtils.generateRoom(roomId);
		room.monster = GeneratorUtils.generateMonster(monsterId);
		
		dungeon.rooms = new ArrayList<RoomXML>();
		dungeon.rooms.add(room);
		
		write(targetFile, dungeon, DungeonLoaderXML.class, room.monster.name + Const.NEW_LINE + "Requires room: room" + roomId);
	}

}
