package cz.dd4j.loader.simstate.impl.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.loader.LoaderXML;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.dungeon.DungeonLoader;
import cz.dd4j.loader.simstate.ISimStateLoaderImpl;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;

public class SimStateLoaderXML extends LoaderXML<SimStateXML> implements ISimStateLoaderImpl {
	
	public SimStateLoaderXML() {
		super(SimStateXML.class);
	}

	@Override
	public SimState loadSimState(File xmlFile) {
		SimStateXML simStateXML = load(xmlFile);

		if (simStateXML.dungeons.size() == 0) {
			throw new RuntimeException("SimState does not contain any dungeon definition! File: " + xmlFile.getAbsolutePath());
		}
		
		return loadSimState(new File(xmlFile.getParent()), simStateXML);
	}
	
	public SimState loadSimState(File xmlFileDir, SimStateXML simStateXML) {
		List<File> dungeonXMLFiles = new ArrayList<File>(simStateXML.dungeons == null ? 0 : simStateXML.dungeons.size());
		List<File> agentsXMLFiles = new ArrayList<File>(simStateXML.agents == null ? 0 : simStateXML.agents.size());
		
		if (simStateXML.dungeons != null) {
			for (FileXML dungeon : simStateXML.dungeons) {
				dungeonXMLFiles.add(new File(xmlFileDir, dungeon.path));			
			}
		}
		
		if (simStateXML.agents != null) {
			for (FileXML agents : simStateXML.agents) {
				agentsXMLFiles.add(new File(xmlFileDir, agents.path));			
			}
		}
		
		return loadSimState(dungeonXMLFiles, agentsXMLFiles);
	}
	
	public SimState loadSimState(List<File> dungeonXMLFiles, List<File> agentsXMLFiles) {
		
		Dungeon dungeon = new Dungeon();
		Agents<IMonsterAgent> monsters = new Agents<IMonsterAgent>();
		Agents<IFeatureAgent> features = new Agents<IFeatureAgent>();
				
		// LOAD DUNGEON FILES, ADDITIVELY BLEND LATER ONES OVER EARLIER ONES...
		DungeonLoader dungeonLoader = new DungeonLoader();		
		for (File dungeonXMLFile : dungeonXMLFiles) {
			Dungeon append = null;
			try {
				append = dungeonLoader.loadDungeon(dungeonXMLFile.getCanonicalFile());
			} catch (Exception e) {
				File cf = null;
				try {
					cf = dungeonXMLFile.getCanonicalFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				throw new RuntimeException("Failed to load: " + dungeonXMLFile.getAbsolutePath() + Const.NEW_LINE + "Canonical path: " + (cf == null ? "null" : cf.getAbsolutePath()), e);
			}
			try {
				blend(dungeon, append);
			} catch (Exception e) {
				throw new RuntimeException("Failed to blend: " + dungeonXMLFile.getAbsolutePath(), e);
			}
		}
		
		// LOAD AGENT FILES, ADDITIVELY BLEND LATER ONES OVER EARLIER ONES...
		AgentsLoader agentsLoader = new AgentsLoader();
		for (File agentsXMLFile : agentsXMLFiles) {
			Agents append = null;
			try {
				append = agentsLoader.loadAgents(agentsXMLFile.getCanonicalFile());
			} catch (Exception e) {
				File cf = null;
				try {
					cf = agentsXMLFile.getCanonicalFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				throw new RuntimeException("Failed to load: " + agentsXMLFile.getAbsolutePath() + Const.NEW_LINE + "Canonical path: " + (cf == null ? "null" : cf.getAbsolutePath()), e);
			}
			try {
				blend(monsters, features, append);
			} catch (Exception e) {
				throw new RuntimeException("Failed to blend: " + agentsXMLFile.getAbsolutePath(), e);
			}
		}
		
		// SET UP THE SimState
		SimState state = new SimState();
		
		state.dungeon = dungeon;
		
		// TINKER THE SimState
		
		// search for heroes and monsters and features...
		// ...create their MindBodies...
		for (Room room : state.dungeon.rooms.values()) {
			if (room.hero != null) {
				if (state.heroes.containsKey(room.hero.id)) throw new RuntimeException("There are more than one Hero[id=" + room.hero.id + "] within the state!");
				AgentMindBody<Hero, IHeroAgent> hero = new AgentMindBody<Hero, IHeroAgent>();
				hero.body = room.hero;
				hero.body.atRoom = room;
				state.heroes.put(hero.body.id, hero);					
			}
			if (room.monster != null) {
				if (state.monsters.containsKey(room.monster.id)) throw new RuntimeException("There are more than one Monster[id=" + room.monster.id + "] within the state!");
				AgentMindBody<Monster, IMonsterAgent> monster = new AgentMindBody<Monster, IMonsterAgent>();
				monster.body = room.monster;
				monster.body.atRoom = room;
				monster.mind = monsters.agents.get(monster.body.id);
				if (monster.mind == null) {
					throw new RuntimeException("Monster agent not specified for the Monster[id=" + monster.body.id +"].");
				}
				state.monsters.put(monster.body.id, monster);					
			}
			if (room.feature != null) {
				if (state.features.containsKey(room.feature.id)) throw new RuntimeException("There are more than one Feature[id=" + room.feature.id + "] within the state!");
				AgentMindBody<Feature, IFeatureAgent> feature = new AgentMindBody<Feature, IFeatureAgent>();
				feature.body = room.feature;
				feature.body.atRoom = room;
				feature.mind = features.agents.get(feature.body.id);
				if (feature.mind == null) {
					throw new RuntimeException("Feature agent not specified for the Feature[id=" + feature.body.id +"].");
				}
				state.features.put(feature.body.id, feature);	
			}
		}
		
		// WE'RE DONE!
		
		return state;
	}
	
	public static void blend(Dungeon target, Dungeon append) {
		for (Map.Entry<Id, Room> entry : append.rooms.entrySet()) {
			Room targetRoom = target.rooms.get(entry.getKey());
			if (targetRoom == null) {
				target.rooms.put(entry.getKey(), entry.getValue());
			} else {
				blend(target, targetRoom, entry.getValue());
			}
		}
	}
	
	private static void blend(Dungeon dungeon, Room targetRoom, Room append) {
		if (append.corridors != null) {
			for (Corridor corridor : append.corridors) {
				blend(dungeon, corridor);
			}
		}
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

	private static void blend(Dungeon targetDungeon, Corridor corridor) {
		Room targetRoom1 = targetDungeon.rooms.get(corridor.room1.id);
		Room targetRoom2 = targetDungeon.rooms.get(corridor.room2.id);
			
		corridor.room1 = targetRoom1;
		corridor.room2 = targetRoom2;
		
		boolean add;
		
		if (targetRoom1.corridors == null) targetRoom1.corridors = new ArrayList<Corridor>();
		add = true;
		for (Corridor other : targetRoom1.corridors) {
			if (other.equals(corridor)) {
				add = false;
				break;
			}
		}
		if (add) {
			targetRoom1.corridors.add(corridor);
		}
		
		if (targetRoom2.corridors == null) targetRoom2.corridors = new ArrayList<Corridor>();
		add = true;
		for (Corridor other : targetRoom2.corridors) {
			if (other.equals(corridor)) {
				add = false;
				break;
			}
		}
		if (add) {
			targetRoom2.corridors.add(corridor);
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
		for (Map.Entry<Id, Item> entry : append.inventory.getData().entrySet()) {
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
	}

	private static void blend(Monster target, Monster append) {		
	}

	public static void blend(Agents<IMonsterAgent> monsters, Agents<IFeatureAgent> features, Agents append) {
		for (Object entryObj : append.agents.entrySet()) {
			Map.Entry<Id, IAgent> entry = (Map.Entry<Id, IAgent>)entryObj;
			if (entry.getValue() instanceof IMonsterAgent) {
				monsters.agents.put(entry.getKey(), (IMonsterAgent)entry.getValue());
			}
			if (entry.getValue() instanceof IFeatureAgent) {
				features.agents.put(entry.getKey(), (IFeatureAgent)entry.getValue());
			}
		}
	}

}
