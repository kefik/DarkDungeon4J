package cz.dd4j.simulation.data.state.slim;

import cz.dd4j.simulation.data.state.SimStateIds;

public class SlimBase {
	
	/**
	 * How many bits are we using to store "room reference". In other words, how big {@link SimStateIds#rooms()} can be at max.
	 */
	public static final int ROOM_REF_BITS = 13;
	
	public static final int MAX_ROOMS = ((int)Math.pow(2, ROOM_REF_BITS)) - 1;
	
	/**
	 * How many bits are we using to store "monster reference". In other words, how big {@link SimStateIds#monsters()} can be at max.
	 */
	public static final int MONSTER_REF_BITS = 7;
	
	public static final int MAX_MONSTERS = ((int)Math.pow(2, MONSTER_REF_BITS)) - 1;
	
	/**
	 * How many bits are we using to store "hero reference". In other words, how big {@link SimStateIds#heroes()} can be at max.
	 */
	public static final int HERO_REF_BITS = 3;
	
	public static final int MAX_HEROES = ((int)Math.pow(2, HERO_REF_BITS)) - 1;
	
	/**
	 * How many bits are we using to store "item reference". In other words, how big {@link SimStateIds#items()} can be at max.
	 */
	public static final int ITEM_REF_BITS = 7;
	
	public static final int MAX_ITEMS = ((int)Math.pow(2, ITEM_REF_BITS)) - 1;

}
