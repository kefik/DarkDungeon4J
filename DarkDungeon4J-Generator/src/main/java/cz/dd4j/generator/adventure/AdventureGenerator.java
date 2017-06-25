package cz.dd4j.generator.adventure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.loader.simstate.impl.xml.FileXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.collection.CombinationsGenerator;
import cz.dd4j.utils.files.DirCrawler;
import cz.dd4j.utils.files.DirCrawlerCallback;

public class AdventureGenerator extends GeneratorBase<AdventureGeneratorConfig>{
	
	public static class AdventureContext {
		
		public int adventureNumber;
		
		public int roomsCount;
		
		public String roomsFile;
		
		public String heroesFile;
		
		public String goalsFile;
		
		public String corridorsFile;
		
		public String resultDirRootRelativePath;
		
		public List<String> allMonsterTypes = new ArrayList<String>();
		
		public List<String> allTrapTypes = new ArrayList<String>();
		
		public List<String> allItemTypes = new ArrayList<String>();

		public List<String> itemTypes;

		public List<String> trapTypes;

		public List<String> monsterTypes;
		
		public int monstersCount;

		public List<Integer> roomIds;
		
		public void reset() {
			adventureNumber = 0;
			roomsCount = 0;
			roomsFile = null;
			heroesFile = null;
			goalsFile = null;
			corridorsFile = null;		
			resultDirRootRelativePath = null;
			allMonsterTypes.clear();
			allTrapTypes.clear();
			allItemTypes.clear();
		}
		
	}

	private static final Pattern ROOMS_PATTERN = Pattern.compile("Requires: Rooms([0-9]*)\\.xml"); 
	
	private static final Pattern MONSTER_AGENT_PATTERN = Pattern.compile("monster[0-9]*-([^.]*)\\.xml$");
	
	private static final Pattern TRAP_AGENT_PATTERN = Pattern.compile("trap[0-9]*-([^.]*)\\.xml$");
	
	private static final Pattern ITEM_PATTERN = Pattern.compile("([a-zA-Z_]*)[0-9]*-Room[0-9]*\\.xml$");
	
	private AdventureContext ctx = new AdventureContext();	
	
	public AdventureGenerator(AdventureGeneratorConfig config) {
		super(SimStateXML.class, config);
	}
	
	private void reset() {
		ctx.reset();
	}

	@Override
	public void generate() {
		reset();
		
		probeMonsterTypes();
		probeTrapTypes();
		probeItemTypes();
		
		File corridorsDir = config.target.getDir(config.corridorsDir);
		
		File[] corridorFiles = corridorsDir.listFiles();
		Arrays.sort(corridorFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for (File corridorsFile : corridorFiles) {
			if (!corridorsFile.getName().endsWith(".xml")) continue;
			generateForCorridors(corridorsFile);
			
			System.out.println("GENERATING FOR 1 CORRIDORS ONLY!");
			return; // for now, just generate one
		}
	}
	
	private void probeMonsterTypes() {
		ctx.allMonsterTypes.clear();
		File dir = config.target.getDir(config.agentMonstersDir);
		probeTypes(MONSTER_AGENT_PATTERN, 1, dir, ctx.allMonsterTypes);
	}

	private void probeTrapTypes() {
		ctx.allTrapTypes.clear();
		File agentTrapsDir = config.target.getDir(config.agentTrapsDir);
		probeTypes(TRAP_AGENT_PATTERN, 1, agentTrapsDir, ctx.allTrapTypes);
	}
	
	private void probeItemTypes() {
		ctx.allItemTypes.clear();
		File dir = config.target.getDir(config.itemsDir);
		probeTypes(ITEM_PATTERN, 1, dir, ctx.allItemTypes);
	}
	

	private void probeTypes(final Pattern pattern, final int typeInMatcherGroup, File rootDir, final List<String> output) {
		final Set<String> types = new HashSet<String>();
		
		DirCrawler.crawl(rootDir, new DirCrawlerCallback() {
			
			@Override
			public void visitFile(File file) {
				
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					String type = matcher.group(typeInMatcherGroup);
					types.add(type);
				}
			}
			
		});		
		
		output.addAll(types);
		Collections.sort(output);		
	}

	private void generateForCorridors(File corridorsFile) {
		// SETUP TARGET CORRIDORS FILE
		ctx.corridorsFile = config.corridorsDir + "/" + corridorsFile.getName();
		
		// SETUP TARGET ROOMS FILE
		ctx.roomsCount = readRequiredRooms(corridorsFile);		
		ctx.roomsFile = config.roomsDir + "/Rooms" + ctx.roomsCount + ".xml";
		
		// SETUP TARGET HEROES FILE
		ctx.heroesFile = config.heroesDir + "/Hero1-Room1.xml";
				
		// SETUP TARGET GOALS FILE
		ctx.goalsFile = config.goalsDir + "/Goal-Room" + ctx.roomsCount + ".xml";
		
		// DETERMINE RESULT DIR ROOT RELATIVE PATH
		ctx.resultDirRootRelativePath = determineResultDirRootRelativePath();

		// GENERATE ROOM INDICES FOR PLACEMENTS
				
		List<Integer> allRoomIndices = new ArrayList<Integer>(ctx.roomsCount);
		for (int i = 1; i <= ctx.roomsCount; ++i) {
			allRoomIndices.add(i);
		}
		
		// GENERATING ALL POSSIBILITIES!

		// FOR ALL COMBINANTIONS OF ITEMS/TRAPS/MONSTER COUNT
		for (int itemsCount : config.items) {
			for (int trapsCount : config.traps) {
				for (int monstersCount : config.monsters) {
					// GO THROUGH ALL COMBINATIONS OF ITEM
					CombinationsGenerator<String> itemTypeNonUniqueCombinations = new CombinationsGenerator<String>(itemsCount, ctx.allItemTypes, false, false);
					// GO THROUGH ALL COMBINATIONS OF TRAPS
					CombinationsGenerator<String> trapTypeNonUniqueCombinations = new CombinationsGenerator<String>(trapsCount, ctx.allTrapTypes, false, false);
					// GO THROUGH ALL COMBINATIONS OF MONSTERS					
					CombinationsGenerator<String> monsterTypeNonUniqueCombinations;
					if (config.monstersOfTheSameType) monsterTypeNonUniqueCombinations = new CombinationsGenerator<String>(1,             ctx.allMonsterTypes, false, false);
					else                              monsterTypeNonUniqueCombinations = new CombinationsGenerator<String>(monstersCount, ctx.allMonsterTypes, false, false);
					
					for (List<String> itemTypes : itemTypeNonUniqueCombinations) {
						for (List<String> trapTypes : trapTypeNonUniqueCombinations) {
							for (List<String> monsterTypes : monsterTypeNonUniqueCombinations) {
								
								// room1            -> starting position
								// room'roomsCount' -> goal position
								
								// PLACE ALL ITEMS/TRAPS/MONSTERS SOMEWHERE								
								CombinationsGenerator<Integer> roomIndicesUniqueVariations = new CombinationsGenerator<Integer>(itemsCount+trapsCount+monstersCount, allRoomIndices, true, true);								
								for (List<Integer> roomIndices : roomIndicesUniqueVariations) {
									generateForSettings(itemTypes, trapTypes, monsterTypes, monstersCount, roomIndices);									
								}
							}
						}
					}
					
				}
			}
		}
	}

	private String determineResultDirRootRelativePath() {
		if (config.resultDir == null || config.resultDir.length() == 0 || config.resultDir.equals(".")) return "";
		
		String result = "";
		
		String[] parts = config.resultDir.split("/");
		for (int i = 0; i < parts.length; ++i) {
			result += "../";
		}
		
		return result;
	}

	private void generateForSettings(List<String> itemTypes, List<String> trapTypes, List<String> monsterTypes, int monstersCount, List<Integer> roomIds) {
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
				// cannot place monster in the starting room!
				return;
			}
			if (trapRooms.contains(roomId)) {
				// cannot place monster into the room with the trap!
				return;
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
				if (!filter.isAccepted(ctx, result, config)) return;
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
	}
	
	private FileXML newFileXML(String file) {
		FileXML result = new FileXML();
		result.path = ctx.resultDirRootRelativePath + file;
		return result;
	}

	private int readRequiredRooms(File corridorsFile) {
		FileInputStream stream = null;
		BufferedReader reader = null;
		try {
			stream = new FileInputStream(corridorsFile);
			reader = new BufferedReader(new InputStreamReader(stream));
			while (reader.ready()) {
				String line = reader.readLine();
				Matcher matcher = ROOMS_PATTERN.matcher(line);
				if (matcher.find()) {
					return Integer.parseInt(matcher.group(1));
				}
			}
		} catch (Exception e) {	
			throw new RuntimeException("Failed to open file: " + corridorsFile.getAbsolutePath());
		} finally {
			if (reader != null) {				
				try { reader.close(); } catch (Exception e) {}
			}
			if (stream != null) {				
				try { stream.close(); } catch (Exception e) {}
			}
		}
				
		throw new RuntimeException("Failed to find the required number of rooms in file: " + corridorsFile.getAbsolutePath());
	}
	
}
