package cz.dd4j.simulation.data.state;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;

/**
 * Contains quick mappings between {@link Item}, {@link Entity}ies, {@link IAgent}s and {@link Room}s to {@link Id}s.
 * @author Jimmy
 *
 */
public class SimStateIds {

	/**
	 * Id -&gt; {@link IAgent} map for quick lookup up of the {@link IAgent} according to it's {@link Id}.
	 */
	private Map<Id, IAgent> id2Agent = new HashMap<Id, IAgent>();
	
	/**
	 * Id -&gt; {@link Entity} map for quick lookup up of the {@link Entity} according to it's {@link Id}.
	 */
	private Map<Id, Entity> id2Entity = new HashMap<Id, Entity>();
	
	/**
	 * Id -&gt; {@link Item} map for quick lookup up of the {@link Item} according to it's {@link Id}.
	 */
	private Map<Id, Item> id2Item = new HashMap<Id, Item>();
	
	/**
	 * Id -&gt; {@link Room} map for quick lookup up of the {@link Room} according to it's {@link Id}.
	 */
	private Map<Id, Room> id2Room = new HashMap<Id, Room>();
	
	/**
	 * Id -&gt; {@link IAgent} map for quick lookup up of the {@link IAgent} according to it's {@link Id}.
	 */
	private Map<IAgent, Id> agent2Id = new HashMap<IAgent, Id>();
	
	/**
	 * Id -&gt; {@link Entity} map for quick lookup up of the {@link Entity} according to it's {@link Id}.
	 */
	private Map<Entity, Id> entity2Id = new HashMap<Entity, Id>();
	
	/**
	 * Id -&gt; {@link Item} map for quick lookup up of the {@link Item} according to it's {@link Id}.
	 */
	private Map<Item, Id> item2Id = new HashMap<Item, Id>();
	
	/**
	 * Id -&gt; {@link Room} map for quick lookup up of the {@link Room} according to it's {@link Id}.
	 */
	private Map<Room, Id> room2Id = new HashMap<Room, Id>();
	
	/**
	 * Fixed order of existing {@link Room} {@link Id}s (ascending order).
	 */
	private List<Id> roomIds = new ArrayList<Id>();
	
	/**
	 * Fixed order of existing {@link IAgent} {@link Id}s (ascending order).
	 */
	private List<Id> agentIds = new ArrayList<Id>();
	
	/**
	 * Fixed order of existing {@link Entity} {@link Id}s (ascending order).
	 */
	private List<Id> entityIds = new ArrayList<Id>();
	
	/**
	 * Fixed order of existing {@link Item} {@link Id}s (ascending order).
	 */
	private List<Id> itemIds = new ArrayList<Id>();
	
	/**
	 * Fixed order of existing {@link Room}s, the same order as {@link #roomIds}.
	 */
	private List<Room> rooms = new ArrayList<Room>();
	
	/**
	 * Fixed order of existing {@link IAgent}s, the same order as {@link #agentIds}.
	 */
	private List<IAgent> agents = new ArrayList<IAgent>();
	
	/**
	 * Fixed order of existing {@link Entity}s, the same order as {@link #entityIds}.
	 */
	private List<Entity> entities = new ArrayList<Entity>();
	
	/**
	 * Fixed order of existing {@link Item}s, the same order as {@link #itemIds}.
	 */
	private List<Item> items = new ArrayList<Item>();
	
	
	private SimState state;
	
	public SimStateIds(SimState state) {
		this.state = state;
		
		// PROBE STATE	
		
		for (Id id : state.features.keySet()) {
			id2Agent.put(id, state.features.get(id).mind);
			agent2Id.put(state.features.get(id).mind, id);
			id2Entity.put(id, state.features.get(id).body);
			entity2Id.put(state.features.get(id).body, id);
			
			agentIds.add(id);
			entityIds.add(id);
		}
		
		for (Id id : state.monsters.keySet()) {
			id2Agent.put(id, state.monsters.get(id).mind);
			agent2Id.put(state.monsters.get(id).mind, id);
			id2Entity.put(id, state.monsters.get(id).body);
			entity2Id.put(state.monsters.get(id).body, id);
			
			agentIds.add(id);
			entityIds.add(id);
		}
		
		for (Id id : state.heroes.keySet()) {
			id2Agent.put(id, state.heroes.get(id).mind);
			agent2Id.put(state.heroes.get(id).mind, id);
			id2Entity.put(id, state.heroes.get(id).body);
			entity2Id.put(state.heroes.get(id).body, id);
			
			agentIds.add(id);
			entityIds.add(id);
		}
		
		for (Room room : state.dungeon.rooms.values()) {
			id2Room.put(room.id, room);
			room2Id.put(room, room.id);
			
			if (room.item != null) {
				id2Item.put(room.item.id, room.item);
				item2Id.put(room.item, room.item.id);
				
				itemIds.add(room.item.id);
			}	
			
			roomIds.add(room.id);
		}
		
		Comparator<Id> idCmp = new Comparator<Id>() {

			@Override
			public int compare(Id o1, Id o2) {
				return o1.id - o2.id;
			}
			
		};
		
		agentIds.sort(idCmp);
		entityIds.sort(idCmp);
		roomIds.sort(idCmp);
		itemIds.sort(idCmp);
		
		for (Id id : agentIds) {
			agents.add(id2Agent.get(id));
		}
		for (Id id : entityIds) {
			entities.add(id2Entity.get(id));
		}
		for (Id id : roomIds) {
			rooms.add(id2Room.get(id));
		}
		for (Id id : itemIds) {
			items.add(id2Item.get(id));
		}
	}
	
	// =======
	// GETTERS
	// =======
	
	public List<Id> agentIds() {
		return agentIds;	
	}
	
	public List<Id> entityIds() {
		return entityIds;	
	}
	
	public List<Id> roomIds() {
		return roomIds;	
	}
	
	public List<Id> itemIds() {
		return itemIds;	
	}
	
	public List<IAgent> agents() {
		return agents;	
	}
	
	public List<Entity> entities() {
		return entities;	
	}
	
	public List<Room> rooms() {
		return rooms;	
	}
	
	public List<Item> items() {
		return items;	
	}
	
	public IAgent agent(Id id) {
		return id2Agent.get(id);
	}
	
	public Id agentId(IAgent agent) {
		return agent2Id.get(agent);
	}
	
	public IFeatureAgent featureAgent(Id id) {
		return (IFeatureAgent) id2Agent.get(id);
	}
	
	public IMonsterAgent monsterAgent(Id id) {
		return (IMonsterAgent) id2Agent.get(id);
	}
	
	public IHeroAgent heroAgent(Id id) {
		return (IHeroAgent) id2Agent.get(id);
	}
	
	public Entity entity(Id id) {
		return id2Entity.get(id);
	}
	
	public Id entityId(Entity entity) {
		return entity.id;
	}
	
	public Feature feature(Id id) {
		return (Feature) id2Entity.get(id);
	}
	
	public Monster monster(Id id) {
		return (Monster) id2Entity.get(id);
	}
	
	public Hero hero(Id id) {
		return (Hero) id2Entity.get(id);
	}
	
	public Room room(Id id) {
		return id2Room.get(id);
	}
	
	public Id roomId(Room room) {
		return room.id;
	}
	
	public Item item(Id id) {
		return id2Item.get(id);
	}
	
	public Id itemId(Item item) {
		return item.id;
	}
	
}
