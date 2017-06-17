package cz.dd4j.loader.dungeon.impl.xml;

import java.io.File;
import java.util.HashMap;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.loader.LoaderXML;
import cz.dd4j.loader.dungeon.IDungeonLoaderImpl;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.entities.features.Trap;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;

public class DungeonLoaderXML extends LoaderXML<DungeonXML> implements IDungeonLoaderImpl {

	public DungeonLoaderXML() {
		super(DungeonXML.class);		
	}

	@Override
	public Dungeon loadDungeon(File xmlFile) {
		DungeonXML dungeonXML = load(xmlFile);		
		return loadDungeon(dungeonXML);
	}
	
	public Dungeon loadDungeon(DungeonXML dungeonXML) {
		Dungeon result = new Dungeon();
		result.rooms = new HashMap<Id, Room>(); 
		
		if (dungeonXML.rooms != null) {
			for (RoomXML room : dungeonXML.rooms) {
				createRoom(result, room);
			}
		}
		if (dungeonXML.corridors != null) {
			for (CorridorXML corridor : dungeonXML.corridors) {
				createCorridor(result, corridor);
			}
		}
		
		return result;
	}

	protected void createRoom(Dungeon dungeon, RoomXML room) {
		Room result = new Room();
		
		result.id      = room.id;
		result.label   = room.label;
		result.feature = createFeatureInRoom(room);
		result.hero    = createHeroInRoom(room);
		result.item    = createItemInRoom(room);
		result.monster = createMonsterInRoom(room);
		
		dungeon.rooms.put(result.id, result);
	}
	
	// =======
	// MONSTER
	// =======

	private Monster createMonsterInRoom(RoomXML room) {
		if (room.monster == null) return null;
		
		Monster result = createMonster(room.monster);
		
		return result;
	}

	private Monster createMonster(MonsterXML monster) {
		if (monster.type == null) return null;
		
		Monster result;
		
		if (monster.type == EEntity.MONSTER) {
			result = new Monster();
			result.id = monster.id;
			result.name = monster.name;
			return result;
		}
		
		throw new RuntimeException("Unhandled monster type: " + monster.type);
	}

	// ====
	// ITEM
	// ====
	
	private Item createItemInRoom(RoomXML room) {
		if (room.item == null) return null;
		
		Item result = createItem(room.item);
		
		return result;
	}
		
	private Item createItem(ItemXML item) {
		if (item.type == null) return null;
		
		Item result = new Item(item.type);
		
		result.id = item.id;
		result.name = item.name;
		
		return result;
	}
	
	// ====
	// HERO
	// ====

	private Hero createHeroInRoom(RoomXML room) {
		if (room.hero == null) return null;
		
		Hero result = new Hero();
		
		result.id = room.hero.id;
		result.name = room.hero.name;
		
		if (room.hero.inventory != null) {
			for (ItemXML inventory : room.hero.inventory) {
				Item item = createItem(inventory);
				if (item != null) {
					result.inventory.add(item);
				}
			}
		}
		
		if (room.hero.hand != null) {
			result.hand = createItem(room.hero.hand);
		}
		
		return result;
	}
	
	// =======
	// FEATURE
	// =======


	private Feature createFeatureInRoom(RoomXML room) {
		if (room.feature == null) return null;
		
		if (room.feature.type == EFeature.TRAP) {
			Trap trap = new Trap();
			
			trap.id = room.feature.id;
			trap.name = room.feature.name;
			
			return trap;
		}
		
		throw new RuntimeException("Unhandled Room[id=" + room.id + "] feature: " + room.feature);
	}

	protected void createCorridor(Dungeon dungeon, CorridorXML corridor) {
		Room room1 = dungeon.rooms.get(corridor.room1Id);
		Room room2 = dungeon.rooms.get(corridor.room2Id);
		
		Corridor result = new Corridor(room1, room2);
		
		room1.corridors.add(result);
		room2.corridors.add(result);
	}

}
