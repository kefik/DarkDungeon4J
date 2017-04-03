package cz.dd4j.simulation.data.dungeon.elements.entities;

import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

/**
 * Represents BODY of the entity.
 * 
 * @author Jimmy
 */
public class Entity extends Element {
	
	/**
	 * Current action an entity is executing.
	 */
	public Action action;
	
	/**
	 * If {@link #alive}, exactly one of {@link #atRoom} and {@link #atCorridor} is always non-null.
	 */
	public Room atRoom;
	
	/**
	 * If {@link #alive}, exactly one of {@link #atRoom} and {@link #atCorridor} is always non-null.
	 */
	public Corridor atCorridor;
	
	/**
	 * Whether the entity is alive.
	 */
	public boolean alive = true;
	
	public Entity(EEntity type) {
		super(type);
	}
	
}
