package cz.dd4j.simulation.data.dungeon.elements.places;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.domain.EPlace;
import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.utils.astar.graph.ILink;
import cz.dd4j.utils.astar.graph.INode;

/**
 * Every room within the dungeon is defined by:
 * <ol>
 *   <li>{@link Place}</li>
 *   <li>0-1 {@link Monster} (may be null, if not present)</li>
 *   <li>0-1 {@link Feature} (may be null, if not present)</li>
 *   <li>0-1 {@link Hero} (may be null, if not present)</li>
 *   <li>0-1 {@link Item} (may be null, if not present)</li>
 * </ol>
 * And is connected with other rooms by the means of {@link #corridors}, {@link Corridor}.
 * 
 * @author Jimmy
 */
public class Room extends Place implements INode<Room> {

	public ERoomLabel label;
	
	public Monster monster;
	
	public Feature feature;
	
	public Hero hero;
	
	public Item item;
	
	public List<Corridor> corridors = new ArrayList<Corridor>();
	
	/**
	 * Whether this room is visible by the hero; i.e., whether the hero
	 * has UP-TO-DATE information about this room.
	 */
	public boolean visible;
	
	public Room() {
		super(EPlace.ROOM);
	}
	
	public boolean isGoalRoom() {
		return EH.isA(label, ERoomLabel.GOAL);
	}
	
	@Override
	public String toString() {
		return "Room[id=" + id + "]";
	}

	@Override
	public Collection<ILink<Room>> getLinks() {
		return (List<ILink<Room>>)(List)corridors;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (o == this) {
			return true;
		}

		if (!(o instanceof Room)) {
			return false;
		}

		Room other = (Room) o;
		return other.id.name.equals(this.id.name);
	}
	
}