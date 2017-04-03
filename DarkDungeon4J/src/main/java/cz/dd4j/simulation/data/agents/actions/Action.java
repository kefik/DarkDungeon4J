package cz.dd4j.simulation.data.agents.actions;

import cz.dd4j.simulation.data.dungeon.Element;

public class Action {

	public final EAction type;
	
	/**
	 * Filled in by the simulator; cannot be abused.
	 */
	public Element who;
	
	public final Element using;
	
	public final Element target;
	
	public Action(EAction type) {
		this.type = type;
		this.using = null;
		this.target = null;
	}
	
	public Action(EAction type, Element target) {
		this.type = type;
		this.using = null;
		this.target = target;
	}
	
	public Action(EAction type, Element target, Element using) {
		this.type = type;
		this.using = using;
		this.target = target;
	}
	
	public boolean isType(EAction type) {
		return this.type == type; 
	}
	
	@Override
	public String toString() {
		if (type == null) return "Action[type=null]";		
		return "Action[" + type + ",who=" + (who == null ? "null" : who.getDescription()) + (type.requiresTarget ? ",target=" + target : "") + (type.requiresUsing ? ",using=" + using : "") + "]";
	}
	
}
