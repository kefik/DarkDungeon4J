package cz.dd4j.loader.simstate.impl.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.loader.dungeon.DungeonLoader;
import cz.dd4j.loader.monsters.MonstersLoader;
import cz.dd4j.loader.simstate.ISimStateLoaderImpl;
import cz.dd4j.simulation.data.agents.monsters.Monsters;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.features.Feature;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.HeroMindBody;
import cz.dd4j.simulation.data.state.MonsterMindBody;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.utils.xstream.XStreamLoader;

public class SimStateLoaderXML extends XStreamLoader<SimStateXML> implements ISimStateLoaderImpl {
	
	public SimStateLoaderXML() {
		super(SimStateXML.class);
	}

	@Override
	public SimState loadSimState(File xmlFile) {
		SimStateXML simStateXML = load(xmlFile);
		
		if (simStateXML.dungeons.size() == 0) {
			throw new RuntimeException("SimState does not contain any dungeon definition! File: " + xmlFile.getAbsolutePath());
		}
		
		List<File> dungeonXMLFiles = new ArrayList<File>(simStateXML.dungeons.size());
		List<File> monstersXMLFiles = new ArrayList<File>(simStateXML.monsters.size());
		
		for (FileXML dungeon : simStateXML.dungeons) {
			dungeonXMLFiles.add(new File(xmlFile.getParent(), dungeon.path));			
		}
		
		for (FileXML monstes : simStateXML.monsters) {
			monstersXMLFiles.add(new File(xmlFile.getParent(), monstes.path));			
		}
		
		return loadSimState(dungeonXMLFiles, monstersXMLFiles);
	}
	
	public SimState loadSimState(List<File> dungeonXMLFiles, List<File> agentsXMLFiles) {
		
		Dungeon dungeon = new Dungeon();
		Monsters agents = new Monsters();
				
		// LOAD DUNGEON FILES, ADDITIVELY BLEND LATER ONES OVER EARLIER ONES...
		DungeonLoader dungeonLoader = new DungeonLoader();		
		for (File dungeonXMLFile : dungeonXMLFiles) {
			Dungeon append = dungeonLoader.loadDungeon(dungeonXMLFile);
			blend(dungeon, append);
		}
		
		// LOAD AGENT FILES, ADDITIVELY BLEND LATER ONES OVER EARLIER ONES...
		MonstersLoader agentsLoader = new MonstersLoader();
		for (File agentsXMLFile : agentsXMLFiles) {
			Monsters append = agentsLoader.loadAgents(agentsXMLFile);
			blend(agents, append);
		}
		
		// SET UP THE SimState
		SimState state = new SimState();
		
		state.dungeon = dungeon;
		
		// TINKER THE SimState
		
		// search for heroes and monsters...
		// ...create their MindBodies...
		for (Room room : state.dungeon.rooms.values()) {
			if (room.hero != null) {
				if (state.heroes.containsKey(room.hero.id)) throw new RuntimeException("There are more than one Hero[id=" + room.hero.id + "] within the state!");
				HeroMindBody hero = new HeroMindBody();
				hero.body = room.hero;
				hero.body.atRoom = room;
				state.heroes.put(hero.body.id, hero);					
			}
			if (room.monster != null) {
				if (state.monsters.containsKey(room.monster.id)) throw new RuntimeException("There are more than one Monster[id=" + room.hero.id + "] within the state!");
				MonsterMindBody monster = new MonsterMindBody();
				monster.body = room.monster;
				monster.body.atRoom = room;
				monster.mind = agents.monsters.get(monster.body.id);
				if (monster.mind == null) {
					throw new RuntimeException("Monster agent not specified for the Monster[id=" + monster.body.id +"].");
				}
				state.monsters.put(monster.body.id, monster);					
			}
			if (room.feature != null) {
				room.feature.atRoom = room;
			}
		}
		
		// WE'RE DONE!
		
		return state;
	}
	
	public static void blend(Dungeon target, Dungeon append) {
		for (Map.Entry<Integer, Room> entry : append.rooms.entrySet()) {
			Room targetRoom = target.rooms.get(entry.getKey());
			if (targetRoom == null) {
				target.rooms.put(entry.getKey(), entry.getValue());
			} else {
				blend(targetRoom, entry.getValue());
			}
		}
	}
	
	private static void blend(Room targetRoom, Room append) {
		if (append.label != null) {
			targetRoom.label = append.label;
		}
		if (targetRoom.monster == null) {
			targetRoom.monster = append.monster;
		} else
		if (append.monster != null) {
			blend(targetRoom.monster, append.monster);
		}
		if (targetRoom.feature == null) {
			targetRoom.feature = append.feature;
		} else
		if (append.feature != null) {
			blend(targetRoom.feature, append.feature);
		}
		if (targetRoom.hero == null) {
			targetRoom.hero = append.hero;
		} else
		if (append.hero != null) {
			blend(targetRoom.hero, append.hero);
		}
		if (targetRoom.item == null) {
			targetRoom.item = append.item;
		} else
		if (append.item != null) {
			blend(targetRoom.item, append.item);
		}
	}

	private static void blend(Item target, Item append) {
		if (append.type != null) {
			throw new RuntimeException("Cannot overwrite existing item! " + target + " <- " + append);
		}
	}

	private static void blend(Hero target, Hero append) {
		if (target.hand == null) {
			target.hand = append.hand;
		} else 
		if (append.hand != null) {
			blend(target.hand, append.hand);
		}
		for (Map.Entry<Integer, Item> entry : append.inventory.getData().entrySet()) {
			Item targetItem = target.inventory.get(entry.getKey());
			if (targetItem == null) {
				target.inventory.add(entry.getValue());
			} else
			if (entry.getValue() != null) {
				blend(targetItem, entry.getValue());
			}
		}		 
	}

	private static void blend(Feature target, Feature append) {
		throw new RuntimeException("Features cannot be blended! Clash in Feature[id=" + target.id + "].");
	}

	private static void blend(Monster target, Monster append) {		
	}

	public static void blend(Monsters target, Monsters append) {
		for (Map.Entry<Integer, IMonsterAgent> entry : append.monsters.entrySet()) {
			target.monsters.put(entry.getKey(), entry.getValue());
		}
	}

}
