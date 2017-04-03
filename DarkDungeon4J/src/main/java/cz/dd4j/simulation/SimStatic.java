package cz.dd4j.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.simulation.actions.instant.IHeroInstantActionExecutor;
import cz.dd4j.simulation.actions.instant.IInstantActionExecutor;
import cz.dd4j.simulation.actions.instant.IMonsterInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.Action;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.features.Feature;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.HeroMindBody;
import cz.dd4j.simulation.data.state.MonsterMindBody;
import cz.dd4j.simulation.events.SimEventsTracker;
import cz.dd4j.simulation.events.SimEventsTracker.SimEventsHandlers;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.simulation.result.SimResultType;

public class SimStatic {
	
	// ========================
	// SIMULATION CONFIGURATION
	// ========================
	
	private SimStaticConfig config;
	
	// ========================
	// SIMULATION EVENTS
	// ========================
	
	private SimEventsTracker eventsTracker = new SimEventsTracker();
	
	// ===========
	// SIM RUNNING
	// ===========
	
	private long frameNumber;
	
	private long simulationStartMillis;
	
	private long currentTickMillis;
	
	private long timeDeltaMillis;
	
	private List<Room> roomsBFSOrdered;
	
	private List<Corridor> corridorsBFSOrdered;
	
	private List<Entity> entityScheduledNonMoveActions;
	
	private List<Entity> entityScheduledMoveActions;
	
	public SimStatic(SimStaticConfig config) {
		this.config = config;		
	}
	
	// ======
	// EVENTS
	// ======
	
	public SimEventsHandlers getEvents() {
		return eventsTracker.handlers();
	}
	
	// ======================
	// SIMULATION MAIN METHOD
	// ======================
	
	/**
	 * Starts the simulation synchronously.
	 * @return
	 */
	public SimResult simulate() {
		if (!config.isReady()) throw new RuntimeException("Simulation is not configured properly: " + config.getMissingInitDescription());
		
		prepareSimulation();		
		
		eventsTracker.event().simulationBegin(config.state);
		
		while (true) {			
			if (isEnd()) {
				SimResult result = end();
				eventsTracker.event().simulationEnd(result);
				return result;
			}
			
			++frameNumber;
			long lastTickMillis = currentTickMillis;
			currentTickMillis = System.currentTimeMillis();
			timeDeltaMillis = currentTickMillis - lastTickMillis;
			
			eventsTracker.event().simulationFrameBegin(frameNumber, currentTickMillis - simulationStartMillis);
						
			tick();
			
			eventsTracker.event().simulationFrameEnd(frameNumber);
		}
	}

	private void prepareSimulation() {
		frameNumber = 0;
		simulationStartMillis = System.currentTimeMillis();
		
		entityScheduledNonMoveActions = new ArrayList<Entity>();
		entityScheduledMoveActions    = new LinkedList<Entity>();		
		
		// ORDER ROOMS IN BFS MANNER
		dungeonBFSNumbering();
	}

	private void dungeonBFSNumbering() {
		roomsBFSOrdered = new ArrayList<Room>(config.state.dungeon.rooms.size());
		corridorsBFSOrdered = new ArrayList<Corridor>();
		
		// INIT BFS
		List<Room> queue = new LinkedList<Room>();		
		queue.add(config.state.dungeon.rooms.values().iterator().next());
		Set<Room> visited = new HashSet<Room>();

		// PERFORM BFS
		while (!queue.isEmpty()) {
			Room room = queue.remove(0);
			visited.add(room);
			
			roomsBFSOrdered.add(room);
			
			for (Corridor corridor : room.corridors) {
				Room other = corridor.getOtherRoom(room);
				if (visited.contains(other)) continue;
				corridorsBFSOrdered.add(corridor);
				queue.add(other);
			}
		}
		
		// DONE!		
	}

	private void tick() {
		gatherActions();		
		
		sortNonMoveActions();
		executeNonMoveActions();
		
		resolveConflictingMoveActions();
		executeMoveActions();		
	}

	// =================
	// GATHERING ACTIONS
	// =================
	
	private void gatherActions() {
		entityScheduledMoveActions.clear();
		entityScheduledNonMoveActions.clear();
		
		gatherActionsFromHeroes();
		gatherActionsFromMonsters();
		gatherActionsFromFeatures();
	}
	
	private void gatherActionsFromHeroes() {
		for (HeroMindBody hero : config.state.heroes.values()) {
			// OBSERVE
			hero.mind.observeBody(hero.body, currentTickMillis);
			hero.mind.observeDungeon(config.state.dungeon, true, currentTickMillis);			
			// GET ACT
			hero.body.action = hero.mind.act();
			if (hero.body.action != null) {
				hero.body.action.who = hero.body;				
			}
			// SUBSCRIBE
			subscribeAction(hero.body);
			// INFORM EVENT
			eventsTracker.event().actionSelected(hero.body, hero.body.action);
		}
	}
	
	private void gatherActionsFromMonsters() {
		for (MonsterMindBody monster : config.state.monsters.values()) {
			if (!monster.body.alive) continue;			
			// OBSERVE+ACT
			monster.body.action = monster.mind.act(monster.body.atRoom, monster.body.atCorridor);
			if (monster.body.action != null) {
				monster.body.action.who = monster.body;
			}
			// SUBSCRIBE
			subscribeAction(monster.body);		
			// INFORM EVENT
			eventsTracker.event().actionSelected(monster.body, monster.body.action);
		}
	}
	
	private void gatherActionsFromFeatures() {
		// TICK ONLY FEATURES THAT ARE IN THE ROOM WITH A HERO
		for (HeroMindBody hero : config.state.heroes.values()) {
			if (!hero.body.alive) continue;
			if (hero.body.atRoom == null) continue;
			if (hero.body.atRoom.feature == null) continue;
			if (!hero.body.atRoom.feature.alive) continue;
			// OBSERVE+ACT
			hero.body.atRoom.feature.action = hero.body.atRoom.feature.act();
			if (hero.body.atRoom.feature.action != null) {
				hero.body.atRoom.feature.action.who = hero.body.atRoom.feature;
			}
			// SUBSCRIBE
			subscribeAction(hero.body.atRoom.feature);
			// INFORM EVENT
			eventsTracker.event().actionSelected(hero.body.atRoom.feature, hero.body.atRoom.feature.action);
		}
	}
	
	private void subscribeAction(Entity entity) {
		if (entity.action == null) return;
		if (entity.action.type == EAction.MOVE) {
			entityScheduledMoveActions.add(entity);
		} else {
			entityScheduledNonMoveActions.add(entity);
		}		
	}
	
	// ===============
	// SORTING ACTIONS
	// ===============
	
	private static Comparator<Entity> comparator = new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			return o1.action.type.priority - o2.action.type.priority;
		}
		
	};
	
	private void sortNonMoveActions() {
		if (entityScheduledMoveActions.size() < 2) return;
		Collections.sort(entityScheduledNonMoveActions, comparator);
	}
	
	// ================================
	// RESOLVE CONFLICTING MOVE ACTIONS
	// ================================
	
	private Set<Room> heroesMoveTo = new HashSet<Room>();
	private Set<Room> monstersMoveTo = new HashSet<Room>();
	
	private void resolveConflictingMoveActions() {
		if (entityScheduledMoveActions.size() < 2) return;
		Collections.shuffle(entityScheduledMoveActions);
		
		heroesMoveTo.clear();
		monstersMoveTo.clear();
		
		Iterator<Entity> iter = entityScheduledMoveActions.iterator();
		while (iter.hasNext()) {
			Entity entity = iter.next();
			if (!entity.alive) {
				// ENTITY ALREADY DEAD
				// => remove scheduled action
				iter.remove();
				continue;
			}
			if (!(entity.action.target instanceof Room)) {
				// INVALID ACTION...
				// => let action execution to resolve this
				continue;
			}
			if (entity.isA(EEntity.HERO)) {
				if (heroesMoveTo.contains(entity.action.target)) {
					// ACTION CONFLICT
					// => INVALIDATE
					invalidateAction(entity);
					iter.remove();
				} else {
					heroesMoveTo.add((Room)entity.action.target);
				}
			} else
			if (entity.isA(EEntity.MONSTER)) {
				if (monstersMoveTo.contains(entity.action.target)) {
					// ACTION CONFLICT
					// => INVALIDATE
					invalidateAction(entity);
					iter.remove();
				} else {
					monstersMoveTo.add((Room)entity.action.target);
				}
			} else {
				// NEITHER A HERO, NOR MONSTER
				// => INVALID ACTION
				iter.remove();
				invalidateAction(entity);
			}
		}
	}
	
	private void invalidateAction(Entity entity) {
		eventsTracker.event().actionInvalid(entity, entity.action);
		entity.action = null;
	}

	// =======================
	// EXECUTING ACTIONS
	// =======================
	
	/**
	 * Tries to execute {@link Entity#action}.
	 * @param entity
	 * @return NULL == no action planned or entity dead, TRUE == action executed, FALSE == action invalid
	 */
	private Boolean executeAction(Entity entity) {
		if (!entity.alive) return null;
		if (entity.action == null) return null;		
		IInstantActionExecutor[] entityActions = config.actionExecutors[entity.type.id];
		IInstantActionExecutor executor = entityActions[entity.action.type.id];
		boolean valid = executor != null && executor.isValid(entity, entity.action);
		if (valid) { 
			Action action = entity.action;
			eventsTracker.event().actionStarted(entity, entity.action);
			executor.run(entity, entity.action);
			eventsTracker.event().actionEnded(entity, action);
			checkDead(action.target);
			return true;
		} else {
			invalidateAction(entity);
			return false;
		}
	}
	
	// ==========================
	// EXECUTING NON-MOVE ACTIONS
	// ==========================
	
	private void executeNonMoveActions() {
		for (Entity entity : entityScheduledNonMoveActions) {
			executeAction(entity);
		}
	}
	
	// ======================
	// EXECUTING MOVE ACTIONS
	// ======================
	
	private void executeMoveActions() {
		if (entityScheduledMoveActions.isEmpty()) return;
		
		// MOVE IS DONE IN TWO PHASES
		
		// 1. move to corridors
		for (Entity entity : entityScheduledMoveActions) {
			executeAction(entity);
		}
		// entities now in corridors...
		
		// 2. check hero x monster clashes within corridors
		checkHeroesInCorridors();
		
		// 3. move out of corridors
		for (Entity entity : entityScheduledMoveActions) {
			executeAction(entity);
		}
	}
	
	private void checkHeroesInCorridors() {
		for (HeroMindBody hero : config.state.heroes.values()) {
			if (hero.body.atCorridor != null && hero.body.atCorridor.monster != null) {
				// HERO IS IN A CORRIDOR WITH SOME MONSTER
				
				// TRIGGER AUTO-ATTACK ACTIONS
				if (hero.body.hand != null && hero.body.hand.type == EItem.SWORD) {
					// HERO AUTO-ATTACK
					
					// SAVE THE ACTION
					Action origHeroMoveAction = hero.body.action;
					
					// CREATE NEW ACTION
					hero.body.action = new Action(EAction.ATTACK, hero.body.atCorridor.monster);
					hero.body.action.who = hero.body;
					
					// PERFORM THE ACTION
					executeAction(hero.body);
					
					// RESTORE ORIG MOVE ACTION
					hero.body.action = origHeroMoveAction;
				} else {
					// MONSTER AUTO-ATTACK
					Monster monster = hero.body.atCorridor.monster;
					
					// SAVE THE ACTION
					Action origMonsterMoveAction = monster.action;
					
					// CREATE NEW ACTION
					monster.action = new Action(EAction.ATTACK, hero.body);
					monster.action.who = monster;
					
					// PERFORM THE ACTION
					executeAction(monster);
					
					// RESTORE ORIG MOVE ACTION
					monster.action = origMonsterMoveAction;
				}
			}
		}		
	}

	// ============
	// ELEMENT DEAD
	// ============
	
	private void checkDead(Element element) {
		if (element == null) return;
		if (element instanceof Hero) {
			checkHeroDead((Hero)element);
		} else
		if (element instanceof Monster) {
			checkMonsterDead((Monster)element);
		} else			
		if (element instanceof Feature) {
			checkFeatureDead((Feature)element);
		}
	}
	
	private void checkHeroDead(Hero hero) {
		if (hero.alive) return;
		config.state.heroes.remove(hero.id);
		hero.atRoom.hero = null;
		eventsTracker.event().elementDead(hero);
	}
	
	private void checkMonsterDead(Monster monster) {
		if (monster.alive) return;
		config.state.monsters.remove(monster.id);
		monster.atRoom.monster = null;
		eventsTracker.event().elementDead(monster);
	}
	
	private void checkFeatureDead(Feature feature) {
		if (feature.alive) return;
		feature.atRoom.feature = null;
		eventsTracker.event().elementDead(feature);
	}

	// ========================
	// SIMULATION END UTILITIES
	// ========================

	private boolean isEnd() {
		return isVictory() || isLose();
	}

	private boolean isVictory() {
		for (HeroMindBody hero : config.state.heroes.values()) {
			if (isHeroVictory(hero)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isHeroVictory(HeroMindBody hero) {		
		return hero.body.alive && hero.body.atRoom != null && hero.body.atRoom.label != null && hero.body.atRoom.label == ERoomLabel.GOAL;
	}
	
	private SimResult end() {
		if (isVictory()) {
			return victory();
		}
		if (isLose()) {
			return lose();
		}
		throw new RuntimeException("isVictory() is false, isLose() is false, ...???");
	}
	
	private SimResult newSimResult() {
		SimResult result = new SimResult();
		result.frameNumber = frameNumber;
		result.simTimeMillis = System.currentTimeMillis() - simulationStartMillis;
		return result;
	}

	private SimResult victory() {
		SimResult result = newSimResult();		
		result.resultType = SimResultType.HERO_WIN;
		for (HeroMindBody hero : config.state.heroes.values()) {
			if (isHeroVictory(hero)) {
				result.winner = hero;
				break;
			}
		}
		return result;
	}
	
	private boolean isLose() {
		for (HeroMindBody hero : config.state.heroes.values()) {
			if (hero.body.alive) return false;
		}
		return true;
	}
	
	private SimResult lose() {
		SimResult result = newSimResult();		
		result.resultType = SimResultType.HEROES_LOSE;
		return result;
	}

}
