package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;

/**
 * Manipulation with Slim MONSTER representation from {@link SlimState#monsters}.
 * 
 * Room:  ALIVE BIT|MONSTER REF|AT ROOM REF|00000000000
 *               1b|         7b|        13b|        11b
 * @author Jimmy
 *
 */
public class SlimMonster {
	
	public static final int ALIVE_BIT = 1 << 31;
	public static final int ALIVE_CLEAR_BIT = ~ALIVE_BIT;
	
	public static final int MONSTER_REF_POS = SlimBase.ROOM_REF_BITS + 11 /*padding*/;
	public static final int MONSTER_REF_MASK = ((int)Math.pow(2,  SlimBase.MONSTER_REF_BITS)-1) << MONSTER_REF_POS;
	public static final int MONSTER_REF_CLEAR_MASK = ~MONSTER_REF_MASK;
	
	public static final int ROOM_REF_POS = 11 /*padding*/;
	public static final int ROOM_REF_MASK = ((int)Math.pow(2,  SlimBase.ROOM_REF_BITS)-1) << ROOM_REF_POS;
	public static final int ROOM_REF_CLEAR_MASK = ~ROOM_REF_MASK;
			
	public static boolean isAlive(int monster) {
		return (monster & ALIVE_BIT) > 0;
	}
	
	public static int setAlive(int monster) {
		return monster | ALIVE_BIT; 
	}
	
	public static int clearAlive(int monster) {
		return monster & ALIVE_CLEAR_BIT; 
	}
	
	public static int getMonsterRef(int monster) {
		return monster & MONSTER_REF_MASK >> MONSTER_REF_POS;
	}
	
	public static int setMonsterRef(int monster, int monsterRef) {
		return (monster & MONSTER_REF_CLEAR_MASK) | (monsterRef << MONSTER_REF_POS);
	}
	
	public static int getRoomRef(int monster) {
		return monster & ROOM_REF_MASK >> ROOM_REF_POS;
	}
	
	public static int setRoomRef(int monster, int roomRef) {
		return (monster & ROOM_REF_CLEAR_MASK) | (roomRef << ROOM_REF_POS);
	}
	
	public static int fromSimState(SimState state, Monster monster) {
		int result = 0;
		if (monster.alive) result = setAlive(result);
		result = setMonsterRef(result, state.ids.monsterRef(monster));
		result = setRoomRef(result, state.ids.roomRef(monster.atRoom)); // auto-handles monster.atRoom == null -> 0		
		
		return result;
	}
	
	public static String toString(int monster) {
		return "SlimMonster[" + Integer.toBinaryString(monster) + "\n"
		     + "            [1b] isAlive = " + isAlive(monster) + " | [" + SlimBase.MONSTER_REF_BITS + "b] monsterRef = " + getMonsterRef(monster) + " | [" + SlimBase.ROOM_REF_BITS + "b] roomRef = " + getRoomRef(monster) + " | [11b] 00000000000" + "\n"
		     + "]"; 
	}
	
}
