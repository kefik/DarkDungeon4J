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
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> roomIds = new ArrayList<Id>();
	
	/**
	 * Room id -&gt; room reference (0-based index of the room within {@link #roomIds}.
	 */
	private Map<Id, Integer> roomId2RoomRef = new HashMap<Id, Integer>();	
	
	/**
	 * Fixed order of existing {@link IAgent} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> agentIds = new ArrayList<Id>();
	
	/**
	 * Agent id -&gt; agent reference (0-based index of the room within {@link #agentIds}.
	 */
	private Map<Id, Integer> agentId2AgentRef = new HashMap<Id, Integer>();	
	
	/**
	 * Fixed order of existing {@link IAgent} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> monsterIds = new ArrayList<Id>();
	
	/**
	 * Monster id -&gt; monster reference (0-based index of the room within {@link #monsterIds}.
	 */
	private Map<Id, Integer> monsterId2MonsterRef = new HashMap<Id, Integer>();	
	
	/**
	 * Fixed order of existing {@link IAgent} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id. 
	 */
	private List<Id> heroIds = new ArrayList<Id>();
	
	/**
	 * Hero id -&gt; hero reference (0-based index of the room within {@link #heroIds}.
	 */
	private Map<Id, Integer> heroId2HeroRef = new HashMap<Id, Integer>();			
	
	/**
	 * Fixed order of existing {@link IAgent} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> featureIds = new ArrayList<Id>();
	
	/**
	 * Feature id -&gt; feature reference (0-based index of the room within {@link #featureIds}.
	 */
	private Map<Id, Integer> featureId2FeatureRef = new HashMap<Id, Integer>();
	
	/**
	 * Fixed order of existing {@link Entity} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> entityIds = new ArrayList<Id>();
	
	/**
	 * Entity id -&gt; entity reference (0-based index of the room within {@link #entityIds}.
	 */
	private Map<Id, Integer> entityId2EntityRef = new HashMap<Id, Integer>();
	
	/**
	 * Fixed order of existing {@link Item} {@link Id}s (ascending order).
	 * Starts with "null", i.e., [0] == null id.
	 */
	private List<Id> itemIds = new ArrayList<Id>();
	
	/**
	 * Item id -&gt; item reference (0-based index of the room within {@link #itemIds}.
	 */
	private Map<Id, Integer> itemId2ItemRef = new HashMap<Id, Integer>();
	
	/**
	 * Fixed order of existing {@link Room}s, the same order as {@link #roomIds}.
	 * [0] == null !
	 */
	private List<Room> rooms = new ArrayList<Room>();
	
	/**
	 * Fixed order of existing {@link IAgent}s, the same order as {@link #agentIds}.
	 * [0] == null !
	 */
	private List<IAgent> agents = new ArrayList<IAgent>();
	
	/**
	 * Fixed order of existing {@link Entity}s, the same order as {@link #entityIds}.
	 * [0] == null !
	 */
	private List<Entity> entities = new ArrayList<Entity>();
	
	/**
	 * Fixed order of existing {@link Monster}s, the same order as {@link #monsterIds}.
	 * [0] == null !
	 */
	private List<Monster> monsters = new ArrayList<Monster>();
	
	/**
	 * Fixed order of existing {@link Hero}s, the same order as {@link #heroIds}.
	 * [0] == null !
	 */
	private List<Hero> heroes = new ArrayList<Hero>();
	
	/**
	 * Fixed order of existing {@link Feature}s, the same order as {@link #featureIds}.
	 * [0] == null !
	 */
	private List<Feature> features = new ArrayList<Feature>();
	
	/**
	 * Fixed order of existing {@link Item}s, the same order as {@link #itemIds}.
	 * [0] == null !
	 */
	private List<Item> items = new ArrayList<Item>();
	
	
	private SimState state;
	
	public SimStateIds(SimState state) {
		this.state = state;
		
		// INITIAL NULLs
		
		rooms.add(null);
		agents.add(null);
		entities.add(null);
		monsters.add(null);
		heroes.add(null);
		features.add(null);
		items.add(null);
		
		// PROBE STATE	
		
		for (Id id : state.features.keySet()) {
			id2Agent.put(id, state.features.get(id).mind);
			agent2Id.put(state.features.get(id).mind, id);
			id2Entity.put(id, state.features.get(id).body);
			entity2Id.put(state.features.get(id).body, id);
			
			features.add(state.features.get(id).body);
			
			agentIds.add(id);
			featureIds.add(id);
			entityIds.add(id);
		}
		
		for (Id id : state.monsters.keySet()) {
			id2Agent.put(id, state.monsters.get(id).mind);
			agent2Id.put(state.monsters.get(id).mind, id);
			id2Entity.put(id, state.monsters.get(id).body);
			entity2Id.put(state.monsters.get(id).body, id);
			
			monsters.add(state.monsters.get(id).body);
			
			agentIds.add(id);
			monsterIds.add(id);
			entityIds.add(id);
		}
		
		for (Id id : state.heroes.keySet()) {
			id2Agent.put(id, state.heroes.get(id).mind);
			agent2Id.put(state.heroes.get(id).mind, id);
			id2Entity.put(id, state.heroes.get(id).body);
			entity2Id.put(state.heroes.get(id).body, id);
			
			heroes.add(state.heroes.get(id).body);
			
			agentIds.add(id);
			heroIds.add(id);
			entityIds.add(id);
		}
		
		for (Room room : state.dungeon.rooms.values()) {
			id2Room.put(room.id, room);
			room2Id.put(room, room.id);
			
			rooms.add(room);
			
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
		
		roomIds.sort(idCmp);
		entityIds.sort(idCmp);		
		agentIds.sort(idCmp);
		monsterIds.sort(idCmp);
		heroIds.sort(idCmp);
		featureIds.sort(idCmp);
		itemIds.sort(idCmp);
		
		int index;
		
		index = 0;
		for (Id id : roomIds) {
			rooms.add(id2Room.get(id));
			roomId2RoomRef.put(id, index++);
		}
		
		index = 0;
		for (Id id : entityIds) {
			entities.add(id2Entity.get(id));
			entityId2EntityRef.put(id, index++);
		}
		
		index = 0;
		for (Id id : agentIds) {
			agents.add(id2Agent.get(id));
			agentId2AgentRef.put(id, index++);
		}
		
		index = 0;
		for (Id id : monsterIds) {
			monsterId2MonsterRef.put(id, index++);
		}
		
		index = 0;
		for (Id id : heroIds) {
			heroId2HeroRef.put(id, index++);
		}
		
		index = 0;
		for (Id id : featureIds) {
			featureId2FeatureRef.put(id, index++);
		}
				
		index = 0;
		for (Id id : itemIds) {
			items.add(id2Item.get(id));
			itemId2ItemRef.put(id, index++);
		}
		
		// SORT ROOMs
		Comparator<Room> roomCmp = new Comparator<Room>() {

			@Override
			public int compare(Room o1, Room o2) {
				return roomRef(o1) - roomRef(o2);
			}
			
		};
		rooms.sort(roomCmp);
		
		// SORT AGENTs
		Comparator<IAgent> agentCmp = new Comparator<IAgent>() {

			@Override
			public int compare(IAgent o1, IAgent o2) {
				return agentRef(o1) - agentRef(o2);
			}
			
		};
		agents.sort(agentCmp);
		
		// SORT ENTITYies
		Comparator<Entity> entityCmp = new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				return entityRef(o1) - entityRef(o2);
			}
			
		};
		entities.sort(entityCmp);
		
		// SORT MONSTERS
		Comparator<Monster> monsterCmp = new Comparator<Monster>() {

			@Override
			public int compare(Monster o1, Monster o2) {
				return monsterRef(o1) - monsterRef(o2);
			}
			
		};
		monsters.sort(monsterCmp);
		
		// SORT HEROES
		Comparator<Hero> heroCmp = new Comparator<Hero>() {

			@Override
			public int compare(Hero o1, Hero o2) {
				return heroRef(o1) - heroRef(o2);
			}
			
		};
		heroes.sort(heroCmp);
		
		// SORT FEATURES
		Comparator<Feature> featureCmp = new Comparator<Feature>() {

			@Override
			public int compare(Feature o1, Feature o2) {
				return featureRef(o1) - featureRef(o2);
			}
			
		};
		features.sort(featureCmp);
		
		// SORT ITEMs
		Comparator<Item> itemCmp = new Comparator<Item>() {

			@Override
			public int compare(Item o1, Item o2) {
				return itemRef(o1) - itemRef(o2);
			}
			
		};
		items.sort(itemCmp);
	}
	
	// =======
	// GETTERS
	// =======
	
	public List<Id> agentIds() {
		return agentIds;	
	}
	
	public List<Id> monsterIds() {
		return monsterIds;	
	}
	
	public List<Id> heroIds() {
		return heroIds;	
	}
	
	public List<Id> featureIds() {
		return featureIds;	
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
	
	public List<Monster> monsters() {
		return monsters;	
	}
	
	public List<Hero> heroes() {
		return heroes;	
	}
	
	public List<Feature> features() {
		return features;	
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
	
	public int agentRef(IAgent agent) {
		if (agent == null) return 0;
		return agentRef(agentId(agent));
	}
	
	public int agentRef(Id id) {
		if (id == null) return 0;
		return agentId2AgentRef.get(id);
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
	
	public int entityRef(Entity entity) {
		if (entity == null) return 0;
		return entityRef(entity.id);
	}
	
	public int entityRef(Id id) {
		if (id == null) return 0;
		return entityId2EntityRef.get(id);
	}
	
	public Feature feature(Id id) {
		return (Feature) id2Entity.get(id);
	}
	
	public int featureRef(Feature feature) {
		if (feature == null) return 0;
		return featureRef(feature.id);
	}
	
	public int featureRef(Id id) {
		if (id == null) return 0;
		return featureId2FeatureRef.get(id);
	}
	
	public Monster monster(Id id) {
		return (Monster) id2Entity.get(id);
	}
	
	public int monsterRef(Monster monster) {
		if (monster == null) return 0;
		return monsterRef(monster.id);
	}
	
	public int monsterRef(Id id) {
		if (id == null) return 0;
		return monsterId2MonsterRef.get(id);
	}
	
	public Hero hero(Id id) {
		return (Hero) id2Entity.get(id);
	}
	
	public int heroRef(Hero hero) {
		if (hero == null) return 0;
		return heroRef(hero.id);
	}
	
	public int heroRef(Id id) {
		if (id == null) return 0;
		return heroId2HeroRef.get(id);
	}
	
	public Room room(Id id) {
		return id2Room.get(id);
	}
	
	public Id roomId(Room room) {
		return room.id;
	}
	
	public int roomRef(Room room) {
		if (room == null) return 0;
		return roomRef(room.id);
	}
	
	public int roomRef(Id id) {
		if (id == null) return 0;
		return roomId2RoomRef.get(id);
	}
	
	public Item item(Id id) {
		return id2Item.get(id);
	}
	
	public Id itemId(Item item) {
		return item.id;
	}
	
	public int itemRef(Item item) {
		if (item == null) return 0;
		return itemRef(item.id);
	}
	
	public int itemRef(Id id) {
		if (id == null) return 0;
		return itemId2ItemRef.get(id);
	}
	
}
