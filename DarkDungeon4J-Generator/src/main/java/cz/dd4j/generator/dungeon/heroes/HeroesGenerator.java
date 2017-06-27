package cz.dd4j.generator.dungeon.heroes;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.HeroXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;


public class HeroesGenerator extends GeneratorBase<HeroesGeneratorConfig> {
	
	public HeroesGenerator(HeroesGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(hero1->" + config.heroesCount + " into room1->" + config.roomsCount + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int heroId = 1; heroId <= config.heroesCount; ++heroId) {
			for (int roomId = 1; roomId <= config.roomsCount; ++roomId) {
				generate(heroId, roomId);	
			}
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int heroId, int roomId) {
		File targetFile = config.target.getFile("/heroes", "Hero" + heroId + "-Room" + roomId + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(hero " + heroId + " for room " + roomId + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		
		RoomXML room = GeneratorUtils.generateRoom(roomId);
		room.hero = GeneratorUtils.generateHero(heroId);
		
		dungeon.rooms = new ArrayList<RoomXML>();
		dungeon.rooms.add(room);
		
		write(targetFile, dungeon, DungeonLoaderXML.class, room.hero.name + Const.NEW_LINE + "Requires room: room" + roomId);
	}

}
