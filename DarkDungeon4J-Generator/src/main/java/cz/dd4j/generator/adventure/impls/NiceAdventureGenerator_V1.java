package cz.dd4j.generator.adventure.impls;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cz.dd4j.descriptor.DungeonPaths;
import cz.dd4j.generator.adventure.IAdventureCallback;
import cz.dd4j.generator.adventure.IAdventureFilter;
import cz.dd4j.loader.simstate.impl.xml.FileXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateLoaderXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.astar.IAStarView;
import cz.dd4j.utils.math.VariationsGenerator;

public class NiceAdventureGenerator_V1 extends AdventureGeneratorBase<NiceAdventureGeneratorConfig_V1>{
		
	private static final Random random = new Random(1);
	
	public NiceAdventureGenerator_V1(NiceAdventureGeneratorConfig_V1 config) {
		super(config);
	}
	
	@Override
	public void generate() {
		init();
				
		for (File corridorsFile : config.corridorsFiles) {
			if (!corridorsFile.getName().endsWith(".xml")) continue;
			generateForCorridors(corridorsFile);
		}
	}
	
	private void generateForCorridors(File corridorsFile) {
		// SETUP TARGET CORRIDORS FILE
		ctx.corridorsFile = corridorsFile.getPath().replaceAll("\\\\", "/");
		
		// SETUP TARGET ROOMS FILE
		ctx.roomsCount = readRequiredRooms(config.source.getFile(corridorsFile.getPath().replaceAll("\\\\", "/")));		
		ctx.roomsFile = config.roomsDir + "/Rooms" + ctx.roomsCount + ".xml";
		
		// SETUP TARGET HEROES FILE
		ctx.heroesFile = config.heroesDir + "/Hero1-Room1.xml";
		ctx.heroRoom = 1;
				
		// SETUP TARGET GOALS FILE
		if (corridorsFile.getName().contains("Torus")) {
			ctx.goalsFile = config.goalsDir + "/Goal-Room" + (ctx.roomsCount/2) + ".xml";
			ctx.goalRoom = ctx.roomsCount / 2;
		} else {
			ctx.goalsFile = config.goalsDir + "/Goal-Room" + ctx.roomsCount + ".xml";
			ctx.goalRoom = ctx.roomsCount;
		}
		
		// DETERMINE RESULT DIR ROOT RELATIVE PATH
		ctx.resultDirRootRelativePath = determineResultDirRootRelativePath();

		// GENERATE ROOM INDICES FOR PLACEMENTS				
		List<Integer> allRoomIndices = new ArrayList<Integer>(ctx.roomsCount);
		for (int i = 1; i <= ctx.roomsCount; ++i) {
			allRoomIndices.add(i);
		}
		
		// GENERATING ALL POSSIBILITIES!

		// FOR ALL COMBINANTIONS OF ITEMS/TRAPS/MONSTER COUNT
		Set<Integer> dangerCountsHistory = new HashSet<Integer>();
		for (double dangerDensity : config.dangerDensities) {
			// DANGER COUNT?
			int dangerCount  = (int)Math.floor(ctx.roomsCount * dangerDensity);
			
			// DO NOT GENERATE THE SAME SETUP TWICE
			if (dangerCountsHistory.contains(dangerCount)) continue;
			dangerCountsHistory.add(dangerCount);
			
			for (String monsterType : config.monsterTypes) {
				
				Set<Integer> trapsCountHistory = new HashSet<Integer>();							
				for (double trapMonsterRatio : config.trapMonsterRatios) {
					int trapsCount   = (int)Math.floor(dangerCount * trapMonsterRatio);
					int monsterCount = dangerCount - trapsCount;

					// DO NOT GENERATE THE SAME SETUP TWICE
					if (trapsCountHistory.contains(trapsCount)) continue;
					trapsCountHistory.add(trapsCount);					
					
					Set<Integer> swordsCountHistory = new HashSet<Integer>();					
					for (double swordDensity : config.swordDensities) {
						// DETERMINE CONCRETE NUMBERS
						int swordCount = Math.max(1, (int)Math.floor(ctx.roomsCount * swordDensity));
						
						// DO NOT GENERATE THE SAME SETUP TWICE
						if (swordsCountHistory.contains(swordCount)) continue;
						swordsCountHistory.add(swordCount);	
						
						// room1            -> starting position
						// room'roomsCount' -> goal position
									
						// PLACE ALL ITEMS/TRAPS/MONSTERS SOMEWHERE		
						// -- but make sure that at least one path to a sword is not blocked by a monster!
						while (true) {
							List<Integer> currentPlacement = getRandomPlacement(allRoomIndices, dangerCount+swordCount);
							
							if (!isAccepted(trapsCount, monsterCount, monsterType, swordCount, currentPlacement)) continue;
							
							boolean generated = generateForSettings(trapsCount, monsterCount, monsterType, swordCount, currentPlacement);
							
							// WE HAVE FOUND ONE POSSIBLE PLACEMENT...
							if (generated) {
								// => move onto the next setup
								break;
							}							
						}												
					}
				}
			}
		}
	}
	
	private List<Integer> getRandomPlacement(List<Integer> roomIds, int count) {
		List<Integer> result = new ArrayList<Integer>(count);
		
		List<Integer> rooms = new ArrayList<Integer>();
		rooms.addAll(roomIds);
		
		while (count > 0) {
			int next = random.nextInt(rooms.size());
			result.add(rooms.remove(next));
			--count;
		}
		
		return result;		
	}
	
	/**
	 * We need to check if there is at least one path that leads from "start" to some "sword" that is not blocked by "any monster".
	 * @param trapsCount
	 * @param monsterCount
	 * @param monsterType
	 * @param swordCount
	 * @param roomIds
	 * @return
	 */
	private boolean isAccepted(int trapsCount, int monsterCount, String monsterType, int swordCount, List<Integer> roomIds) {
		final Set<String> monsterRoomIds = new HashSet<String>();
		for (int i = 0; i < monsterCount; ++i) {
			monsterRoomIds.add("room" + roomIds.get(trapsCount+swordCount+i));
		}
		
		SimStateXML simStateXML = new SimStateXML();
		simStateXML.dungeons = new ArrayList<FileXML>(2);
		simStateXML.dungeons.add(newFileXML(ctx.roomsFile));
		simStateXML.dungeons.add(newFileXML(ctx.corridorsFile));
		
		SimStateLoaderXML simStateLoader = new SimStateLoaderXML();
		SimState simState = simStateLoader.loadSimState(config.target.dir, simStateXML, true);
		
		Room heroRoom = simState.dungeon.rooms.get(Id.get("room1"));
		
		DungeonPaths paths = new DungeonPaths(simState.dungeon);
		
		IAStarView<Room> noMonstersView = new IAStarView<Room>() {
			@Override
			public boolean isOpened(Room node) {
				return !monsterRoomIds.contains(node.id.name);
			}
		};
		
		for (int i = 0; i < swordCount; ++i) {
			Room swordRoom = simState.dungeon.rooms.get(Id.get("room" + (roomIds.get(i))));
			if (paths.hasPath(heroRoom, swordRoom, noMonstersView)) return true;
		}
		
		// NO non-monster PATH BETWEEN HERO AND SWORDS :(
		return false;
	}

	private boolean generateForSettings(int trapsCount, int monsterCount, String monsterType, int swordCount, List<Integer> roomIds) {
		List<String> itemTypes = new ArrayList<String>(swordCount);		
		for (int i = 0; i < swordCount; ++i) {
			itemTypes.add("Sword");
		}
		
		List<String> trapTypes = new ArrayList<String>(trapsCount);
		for (int i = 0; i < trapsCount; ++i) {
			trapTypes.add("Trap");
		}
		
		List<String> monsterTypes = new ArrayList<String>(monsterCount);
		for (int i = 0; i < monsterCount; ++i) {
			monsterTypes.add(monsterType);
		}
		
		return generateForSettings(itemTypes, trapTypes, monsterTypes, monsterCount, roomIds);
	}

	private boolean generateForSettings(List<String> itemTypes, List<String> trapTypes, List<String> monsterTypes, int monstersCount, List<Integer> roomIds) {
		ctx.itemTypes = itemTypes;
		ctx.trapTypes = trapTypes;
		ctx.monsterTypes = monsterTypes;
		ctx.monstersCount = monstersCount;
		ctx.roomIds = roomIds;
		
		int itemsCount = itemTypes.size();
		int trapsCount = trapTypes.size();	
		
		// RESULT
		SimStateXML result = new SimStateXML();
		
		// DUNGEON
		
		result.dungeons = new ArrayList<FileXML>(4 + itemTypes.size() + trapTypes.size() + monsterTypes.size());
		result.dungeons.add(newFileXML(ctx.roomsFile));
		result.dungeons.add(newFileXML(ctx.corridorsFile));
		result.dungeons.add(newFileXML(ctx.goalsFile));
		result.dungeons.add(newFileXML(ctx.heroesFile));		
		
		// ADD ITEMS
		for (int i = 0; i < itemTypes.size(); ++i) {
			int roomIdIndex = 0 + i;
			int roomId = roomIds.get(roomIdIndex);
			
			String file = config.itemsDir + "/" + itemTypes.get(i) + roomId + "-Room" + roomId + ".xml";
			result.dungeons.add(newFileXML(file));			
		}
		
		// ADD TRAPS
		Set<Integer> trapRooms = new HashSet<Integer>();
		for (int i = 0; i < trapTypes.size(); ++i) {
			int roomIdIndex = itemsCount + i;
			int roomId = roomIds.get(roomIdIndex);
			trapRooms.add(roomId);
			
			String file = config.trapsDir + "/Trap" + (i+1) + "-Room" + roomId + ".xml";
			result.dungeons.add(newFileXML(file));			
		}
		
		// ADD MONSTERS
		for (int i = 0; i < monstersCount; ++i) {
			int roomIdIndex = itemsCount + trapsCount + i;
			int roomId = roomIds.get(roomIdIndex);
			
			if (roomId == 1) {
				// cannot place monster into the hero-starting room!
				return false;
			}
			if (trapRooms.contains(roomId)) {
				// cannot place monster into a room with a trap!
				return false;
			}
			
			String file = config.monstersDir + "/Monster" + (i+1) + "-Room" + roomId + ".xml";
			result.dungeons.add(newFileXML(file));			
		}
		
		// AGENTS
		result.agents = new ArrayList<FileXML>(trapTypes.size() + monsterTypes.size());
		
		// ADD TRAP AGENTS
		for (int i = 0; i < trapTypes.size(); ++i) {
			String file = config.agentTrapsDir + "/trap" + (i+1) + "-" + trapTypes.get(i) + ".xml";
			result.agents.add(newFileXML(file));			
		}
		
		// ADD MONSTER AGENTS
		for (int i = 0; i < monstersCount; ++i) {
			String file = config.agentMonstersDir + "/monster" + (i+1) + "-" + monsterTypes.get(i % monsterTypes.size()) + ".xml";
			result.agents.add(newFileXML(file));			
		}

		// SimStateXML READY!
		// => CHECK THE FILTERS
		if (config.filters != null && config.filters.length > 0) {
			for (IAdventureFilter filter : config.filters) {
				if (!filter.isAccepted(ctx, result, config)) return false;
			}
		}
		
		// FILTERS PASSED!
		// => CALLBACK TIME!
		
		if (config.callback != null && config.callback.length > 0) {
			for (IAdventureCallback callback : config.callback) {
				callback.process(ctx, result, this, config);
			}
		}
		
		// RAISE ADVENTURE NUMBER
		++ctx.adventureNumber;
		
		return true;
	}
	
}
