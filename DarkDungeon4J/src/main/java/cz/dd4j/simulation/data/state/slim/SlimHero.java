package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;

/**
 * Manipulation with Slim HERO representation from {@link SlimState#heroes}.
 * 
 * Room:  ALIVE BIT|HERO ID|AT ROOM REF|HAND ITEM ID|0
 *               1b|     3b|        13b|          7b|1b
 * @author Jimmy
 *
 */
public class SlimHero {
	
	public static final int ALIVE_BIT = 1 << 31;
	public static final int ALIVE_CLEAR_BIT = ~ALIVE_BIT;
	
	public static final int HERO_REF_POS = SlimBase.ROOM_REF_BITS + SlimBase.ITEM_REF_BITS + 1 /*padding*/;
	public static final int HERO_REF_MASK = ((int)Math.pow(2,  SlimBase.HERO_REF_BITS)-1) << HERO_REF_POS;
	public static final int HERO_REF_CLEAR_MASK = ~HERO_REF_MASK;
	
	public static final int ROOM_REF_POS = SlimBase.ITEM_REF_BITS + 1 /*padding*/;
	public static final int ROOM_REF_MASK = ((int)Math.pow(2,  SlimBase.ROOM_REF_BITS)-1) << ROOM_REF_POS;
	public static final int ROOM_REF_CLEAR_MASK = ~ROOM_REF_MASK;
	
	public static final int HAND_ITEM_REF_POS = 1 /*padding*/;
	public static final int HAND_ITEM_REF_MASK = ((int)Math.pow(2,  SlimBase.ITEM_REF_BITS)-1) << HAND_ITEM_REF_POS;
	public static final int HAND_ITEM_REF_CLEAR_MASK = ~HAND_ITEM_REF_MASK;
			
	public static boolean isAlive(int hero) {
		return (hero & ALIVE_BIT) > 0;
	}
	
	public static int setAlive(int hero) {
		return hero | ALIVE_BIT; 
	}
	
	public static int clearAlive(int hero) {
		return hero & ALIVE_CLEAR_BIT; 
	}
	
	public static int getHeroRef(int room) {
		return room & HERO_REF_MASK >> HERO_REF_POS;
	}
	
	public static int setHeroRef(int room, int heroRef) {
		return (room & HERO_REF_CLEAR_MASK) | (heroRef << HERO_REF_POS);
	}
	
	public static int getRoomRef(int room) {
		return room & ROOM_REF_MASK >> ROOM_REF_POS;
	}
	
	public static int setRoomRef(int room, int roomRef) {
		return (room & ROOM_REF_CLEAR_MASK) | (roomRef << ROOM_REF_POS);
	}
	
	public static int getHandItemRef(int room) {
		return room & HAND_ITEM_REF_MASK >> HAND_ITEM_REF_POS;
	}
	
	public static int setHandItemRef(int room, int itemRef) {
		return (room & HAND_ITEM_REF_CLEAR_MASK) | (itemRef << HAND_ITEM_REF_POS);
	}
	
	public static int fromSimState(SimState state, Hero hero) {
		int result = 0;
		if (hero.alive) result = setAlive(result);
		result = setHeroRef(result, state.ids.heroRef(hero)); 
		result = setRoomRef(result, state.ids.roomRef(hero.atRoom)); // auto-handles hero.atRoom == null -> 0
		result = setHandItemRef(result,  state.ids.itemRef(hero.hand)); // auto-handles hero.hand == null -> 0
		
		return result;
	}
	
	public static String toString(int hero) {
		return "SlimHero[" + Integer.toBinaryString(hero) + "\n"
		     + "         [1b] isAlive = " + isAlive(hero) + " | [10b] heroRef = " + getHeroRef(hero) + " | [10b] roomRef = " + getRoomRef(hero) + " | [10b] handItemRef = " + getHandItemRef(hero) + " | [1b] 0" + "\n"
		     + "]"; 
	}
	
}
