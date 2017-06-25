package cz.dd4j.generator.dungeon.goals;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.dungeon.GeneratorUtils;
import cz.dd4j.loader.dungeon.impl.xml.DungeonLoaderXML;
import cz.dd4j.loader.dungeon.impl.xml.DungeonXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Const;


public class GoalsGenerator extends GeneratorBase<GoalsGeneratorConfig> {
	
	public GoalsGenerator(GoalsGeneratorConfig config) {
		super(DungeonXML.class, config);
	}

	@Override
	public void generate() {
		config.log.info(getClass().getSimpleName() + ".generate(goal1->" + config.roomsCount + "): generating...");
		
		config.log.info(getClass().getSimpleName() + ".generate()");
		
		for (int i = 1; i <= config.roomsCount; ++i) {
			generate(i);
		}
		
		config.log.info(getClass().getSimpleName() + ".generate(): DONE!");
	}

	private void generate(int goalRoom) {
		File targetFile = config.target.getFile("/goals", "Goal-Room" + goalRoom + ".xml");
		
		config.log.info(getClass().getSimpleName() + ".generate(goal for room " + goalRoom + "): generating...");
		
		DungeonXML dungeon = new DungeonXML();
		
		RoomXML room = GeneratorUtils.generateRoom(goalRoom);
		room.label = ERoomLabel.GOAL;
		
		dungeon.rooms = new ArrayList<RoomXML>();
		dungeon.rooms.add(room);
		
		write(targetFile, dungeon, DungeonLoaderXML.class, "GOAL" + Const.NEW_LINE + "Requires room: room" + goalRoom);
	}

}
