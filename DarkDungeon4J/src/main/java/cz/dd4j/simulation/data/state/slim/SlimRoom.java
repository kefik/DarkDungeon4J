package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;

/**
 * Manipulation with Slim ROOM representation from {@link SlimState#rooms}.
 * 
 * Room:  GOAL BIT|TRAP BIT|ROOM REF|HERO REF|MONSTER REF|ITEM REF
 *              1b|      1b|     13b|      3b|         7b|      7b
 * @author Jimmy
 *
 */
public class SlimRoom {
	
	public static final int GOAL_BIT = 1 << 31;
	public static final int GOAL_CLEAR_BIT = ~GOAL_BIT;
	public static final int TRAP_BIT = 1 << 30;
	public static final int TRAP_CLEAR_BIT = ~TRAP_BIT;
	

	public static final int ROOM_REF_POS = SlimBase.ITEM_REF_BITS + SlimBase.MONSTER_REF_BITS + SlimBase.HERO_REF_BITS;
	public static final int ROOM_REF_MASK = ((int)Math.pow(2,  SlimBase.ROOM_REF_BITS)-1) << ROOM_REF_POS;
	public static final int ROOM_REF_CLEAR_MASK = ~ROOM_REF_MASK;
	
	public static final int HERO_REF_POS = SlimBase.ITEM_REF_BITS + SlimBase.MONSTER_REF_BITS;
	public static final int HERO_REF_MASK = ((int)Math.pow(2,  SlimBase.HERO_REF_BITS)-1) << HERO_REF_POS;
	public static final int HERO_REF_CLEAR_MASK = ~HERO_REF_MASK;
	
	public static final int MONSTER_REF_POS = SlimBase.ITEM_REF_BITS;
	public static final int MONSTER_REF_MASK = ((int)Math.pow(2,  SlimBase.MONSTER_REF_BITS)-1) << MONSTER_REF_POS;
	public static final int MONSTER_REF_CLEAR_MASK = ~MONSTER_REF_MASK;
	
	public static final int ITEM_REF_POS = 0;
	public static final int ITEM_REF_MASK = ((int)Math.pow(2,  SlimBase.ITEM_REF_BITS)-1) << ITEM_REF_POS;
	public static final int ITEM_REF_CLEAR_MASK = ~ITEM_REF_MASK;

	public static boolean isGoal(int room) {
		return (room & GOAL_BIT) > 0;
	}
	
	public static int setGoal(int room) {
		return room | GOAL_BIT; 
	}
	
	public static int clearGoal(int room) {
		return room & GOAL_CLEAR_BIT; 
	}
	
	public static boolean isTrap(int room) {
		return (room & TRAP_BIT) > 0;
	}
	
	public static int setTrap(int room) {
		return room | TRAP_BIT; 
	}
	
	public static int clearTrap(int room) {
		return room & TRAP_CLEAR_BIT; 
	}
	
	public static int getRoomRef(int room) {
		return room & ROOM_REF_MASK >> ROOM_REF_POS;
	}
	
	public static int setRoomRef(int room, int roomRef) {
		return (room & ROOM_REF_CLEAR_MASK) | (roomRef << ROOM_REF_POS);
	}
	
	public static int getHeroRef(int room) {
		return room & HERO_REF_MASK >> HERO_REF_POS;
	}
	
	public static int setHeroRef(int room, int heroRef) {
		return (room & HERO_REF_CLEAR_MASK) | (heroRef << HERO_REF_POS);
	}
	
	public static int getMonsterRef(int room) {
		return room & MONSTER_REF_MASK >> MONSTER_REF_POS;
	}
	
	public static int setMonsterRef(int room, int monsterRef) {
		return (room & MONSTER_REF_CLEAR_MASK) | (monsterRef << MONSTER_REF_POS);
	}
	
	public static int getItemRef(int room) {
		return room & ITEM_REF_MASK >> ITEM_REF_POS;
	}
	
	public static int setItemRef(int room, int itemRef) {
		return (room & ITEM_REF_CLEAR_MASK) | (itemRef << ITEM_REF_POS);
	}
	
	public static int fromSimState(SimState state, Room room) {
		int result = 0;
		if (room.isGoalRoom()) result = setGoal(result);
		if (room.feature != null && room.feature.alive) result = setTrap(result);
		result = setRoomRef(result, state.ids.roomRef(room));
		result = setHeroRef(result, state.ids.heroRef(room.hero));          // auto-handles room.hero == null -> 0
		result = setMonsterRef(result, state.ids.monsterRef(room.monster)); // auto-handles room.monster == null -> 0
		result = setItemRef(result, state.ids.itemRef(room.item));          // auto-handles room.item == null -> 0
		
		return result;
	}
	
	public static String toString(int room) {
		return "SlimRoom[" + Integer.toBinaryString(room) + "\n"
			 + "         [1b] isGoal = " + isGoal(room) + " | [1b] isTrap = " + isTrap(room) + " | [" + SlimBase.ROOM_REF_BITS + "b] roomRef = " + getRoomRef(room) + " | [" + SlimBase.HERO_REF_BITS + "b] heroRef = " + getHeroRef(room) + " | [" + SlimBase.MONSTER_REF_BITS+"b] monsterRef = " + getMonsterRef(room) + " | [" + SlimBase.ITEM_REF_BITS + "b] itemRef = " + getItemRef(room) + "\n"
			 + "]"; 
	}
	
}
