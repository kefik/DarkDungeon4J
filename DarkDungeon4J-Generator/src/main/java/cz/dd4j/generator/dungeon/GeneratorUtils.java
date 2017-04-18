package cz.dd4j.generator.dungeon;

import cz.dd4j.loader.dungeon.impl.xml.CorridorXML;
import cz.dd4j.loader.dungeon.impl.xml.RoomXML;
import cz.dd4j.utils.Id;

public class GeneratorUtils {

	public static Id roomId(int roomNumber) {
		return Id.get("Room" + roomNumber);
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
	
}
