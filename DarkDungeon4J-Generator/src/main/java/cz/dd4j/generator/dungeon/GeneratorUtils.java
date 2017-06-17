package cz.dd4j.generator.dungeon;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.loader.dungeon.impl.xml.CorridorXML;
import cz.dd4j.loader.dungeon.impl.xml.FeatureXML;
import cz.dd4j.loader.dungeon.impl.xml.HeroXML;
import cz.dd4j.loader.dungeon.impl.xml.ItemXML;
import cz.dd4j.loader.dungeon.impl.xml.MonsterXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Id;

public class GeneratorUtils {

	public static Id roomId(int roomNumber) {
		return Id.get("room" + roomNumber);
	}
	
	public static RoomXML generateRoom(int roomId) {
		RoomXML result = new RoomXML();
		result.id = GeneratorUtils.roomId(roomId);
		return result;
	}
	
	public static CorridorXML generateCorridor(int roomId1, int roomId2) {
		return generateCorridor(roomId1, roomId2, null);
	}
	
	public static CorridorXML generateCorridor(int roomId1, int roomId2, String note) {
		CorridorXML result = new CorridorXML();
		result.room1Id = GeneratorUtils.roomId(roomId1);
		result.room2Id = GeneratorUtils.roomId(roomId2);
		result.note = note;
		return result;
	}
	
	public static Id monsterId(int monsterNumber) {
		return Id.get("monster" + monsterNumber);
	}

	public static MonsterXML generateMonster(int monsterId) {
		MonsterXML result = new MonsterXML();
		
		result.id = monsterId(monsterId);
		result.type = EEntity.MONSTER;
		result.name = "Monster" + monsterId;
		
		return result;
	}
	
	public static Id heroId(int heroNumber) {
		return Id.get("hero" + heroNumber);
	}
	
	public static HeroXML generateHero(int heroId) {
		HeroXML result = new HeroXML();
		
		result.id = heroId(heroId);
		result.name = "Hero" + heroId;
		
		return result;
	}
	
	public static Id trapId(int trapRoom) {
		return Id.get("hero" + trapRoom);
	}
	
	public static FeatureXML generateTrap(int trapRoom) {
		FeatureXML result = new FeatureXML();
		
		result.id = trapId(trapRoom);
		result.name = "Trap" + trapRoom;
		result.type = EFeature.TRAP;
		
		return result;
	}
	
	public static Id swordId(int swordNumber) {
		return Id.get("sword" + swordNumber);
	}

	public static ItemXML generateSword(int swordRoom) {
		ItemXML result = new ItemXML();
		
		result.id = swordId(swordRoom);
		result.name = "Sword" + swordRoom;
		result.type = EItem.SWORD;
		
		return result;
	}
	
}
