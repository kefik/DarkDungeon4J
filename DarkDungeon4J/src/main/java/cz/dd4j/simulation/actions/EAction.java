package cz.dd4j.simulation.actions;

import cz.dd4j.agents.commands.CommandParam;

public enum EAction {
		
	//NONE(0), // REPRESENTED AS NULL
	ATTACK(1, 1, CommandParam.PARAM_HERO_OR_MONSTER_REQUIRED), 
	DISARM(2, 2, CommandParam.PARAM_FEATURE_REQUIRED),
	PICKUP(3, 3, CommandParam.PARAM_ITEM_REQUIRED),
	DROP(  4, 4, CommandParam.PARAM_ITEM_REQUIRED),	
	MOVE(  5, 5, CommandParam.PARAM_ROOM_REQUIRED);
	
	// MOSNTER utoci jen kdyz hero nema mec...
	
	public static final int LAST_ID = 5;
		
	public final int id;
	
	/**
	 * Lower number gets executed first!
	 */
	public final int priority;
	
	public final CommandParam[] params;
	
	private EAction(int id, int priority, CommandParam... params) {
		this.id = id;
		this.priority = priority;
		this.params = params;
	}
	
}
