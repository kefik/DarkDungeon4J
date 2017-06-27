package cz.dd4j.generator.dungeon.items;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;


public class SwordsGenerator extends GeneratorBase<SwordsGeneratorConfig> {
	
	public SwordsGenerator(SwordsGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(sword1->" + config.swordCount + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int i = 1; i <= config.swordCount; ++i) {
			generate(i);
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int swordRoom) {
		File targetFile = config.target.getFile("/items", "Sword" + swordRoom + "-Room" + swordRoom + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(for room " + swordRoom + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		
		RoomXML room = GeneratorUtils.generateRoom(swordRoom);
		room.item = GeneratorUtils.generateSword(swordRoom);
		
		dungeon.rooms = new ArrayList<RoomXML>();
		dungeon.rooms.add(room);
		
		write(targetFile, dungeon, DungeonLoaderXML.class, room.item.name + Const.NEW_LINE + "Requires room: room" + swordRoom);
	}

}
