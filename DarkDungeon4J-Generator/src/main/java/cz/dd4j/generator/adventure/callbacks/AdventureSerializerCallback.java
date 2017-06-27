package cz.dd4j.generator.adventure.callbacks;

import java.io.File;

import cz.dd4j.generator.adventure.IAdventureCallback;
import cz.dd4j.generator.adventure.impls.AdventureGeneratorBase;
import cz.dd4j.generator.adventure.impls.AdventureGeneratorConfigBase;
import cz.dd4j.loader.simstate.impl.xml.FileXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateLoaderXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;
import cz.dd4j.utils.Const;

public class AdventureSerializerCallback implements IAdventureCallback {

	@Override
	public void process(AdventureContext ctx, SimStateXML adventure, AdventureGeneratorBase generator, AdventureGeneratorConfigBase config) {
		int itemsCount = ctx.itemTypes.size();
		int trapsCount = ctx.trapTypes.size();
		int monstersCount = ctx.monstersCount;
		
		File targetFile = config.target.getFile("Adventure" + ctx.adventureNumber + ".xml");
		
		String comment = "ADVENTURE " + ctx.adventureNumber;
		comment += Const.NEW_LINE + "~~~~~~~~~~~~~~~~~";
		comment += Const.NEW_LINE + "#Rooms:        " + ctx.roomsCount;
		comment += Const.NEW_LINE + "Corridors:     " + newFileXML(ctx, ctx.corridorsFile).path;
		
		comment += Const.NEW_LINE + "#Items:        " + ctx.itemTypes.size();
		if (ctx.itemTypes.size() > 0) {
			comment += Const.NEW_LINE + "Item types:    ";
			for (int i = 0; i < ctx.itemTypes.size(); ++i) {
				if (i != 0) comment += ", ";
				comment += ctx.itemTypes.get(i);
				comment += " in room" + ctx.roomIds.get(i);
			}
		}
		
		comment += Const.NEW_LINE + "#Traps:        " + ctx.trapTypes.size();
		if (ctx.trapTypes.size() > 0) {
			comment += Const.NEW_LINE + "Trap types:    ";
			for (int i = 0; i < ctx.trapTypes.size(); ++i) {
				if (i != 0) comment += ", ";
				comment += ctx.trapTypes.get(i);
				comment += " in room" + ctx.roomIds.get(itemsCount+i);
			}
		}
		
		comment += Const.NEW_LINE + "#Monsters:     " + monstersCount;
		if (ctx.monsterTypes.size() > 0) {
			comment += Const.NEW_LINE + "Monster types: ";
			for (int i = 0; i < monstersCount; ++i) {
				if (i != 0) comment += ", ";
				comment += ctx.monsterTypes.get(i % ctx.monsterTypes.size());				
				comment += " in room" + ctx.roomIds.get(itemsCount+trapsCount+i);
			}
		}
		
		config.log.info("Generated adventure " + ctx.adventureNumber + "...");
		generator.write(targetFile, adventure, SimStateLoaderXML.class, comment);
	}
	
	private FileXML newFileXML(AdventureContext ctx, String file) {
		FileXML result = new FileXML();
		result.path = ctx.resultDirRootRelativePath + file;
		return result;
	}

}
