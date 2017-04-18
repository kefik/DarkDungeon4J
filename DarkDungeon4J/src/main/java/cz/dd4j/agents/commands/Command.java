package cz.dd4j.agents.commands;

import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

/**
 * Command that is issued for {@link Entity} to be performed.
 * @author Jimmy
 */
public class Command {

	public final EAction type;
	
	/**
	 * Filled in by the simulator; cannot be abused.
	 */
	public Element who;
	
	public final Element target;
	
	public Command(EAction type) {
		this.type = type;
		this.target = null;
	}
	
	public Command(EAction type, Element target) {
		this.type = type;
		this.target = target;
	}
		
	public boolean isType(EAction type) {
		return this.type == type; 
	}
	
	@Override
	public String toString() {
		if (type == null) return "Action[type=null]";		
		return "Action[" + type + ",who=" + (who == null ? "null" : who.getDescription()) + ",target=" + target + "]";
	}
	
}
