package cz.dd4j.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.actions.IActionsValidator;
import cz.dd4j.simulation.actions.instant.IInstantAction;
import cz.dd4j.simulation.actions.instant.InstantActions;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.events.SimEventsTracker;
import cz.dd4j.simulation.events.SimEventsTracker.SimEventsHandlers;
import cz.dd4j.simulation.exceptions.AgentException;
import cz.dd4j.simulation.exceptions.SimulationException;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.simulation.result.SimResultType;

public class SimStatic {
	
	// ========================
	// SIMULATION CONFIGURATION
	// ========================
	
	private SimStaticConfig config;

	private IActionsGenerator actionGenerator;
	
	private IActionsValidator actionValidator;
	
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
	
	private SimResult resultException;
	
	private SimResult simulationResult;
	
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
		
		try {
		
			prepareSimulation();
			
			startSimulation();
			
			while (true) {			
				if (isEnd()) {
					simulationResult = createSimulationResult();					
					return simulationResult;
				}
				
				++frameNumber;
				long lastTickMillis = currentTickMillis;
				currentTickMillis = System.currentTimeMillis();
				timeDeltaMillis = currentTickMillis - lastTickMillis;
				
				eventsTracker.event().simulationFrameBegin(frameNumber, currentTickMillis - simulationStartMillis);
							
				tick();
				
				eventsTracker.event().simulationFrameEnd(frameNumber);
			}
		} catch (SimulationException e1) {
			return simulationResult = exception(e1, SimResultType.SIMULATION_EXCEPTION);
		} catch (AgentException e2) {
			return simulationResult = exception(e2, SimResultType.AGENT_EXCEPTION);
		} catch (Exception e3) {
			return simulationResult = exception(e3, SimResultType.SIMULATION_EXCEPTION);
		} finally {
			endSimulation();
		}
	}

	private void prepareSimulation() {
		frameNumber = 0;
		simulationStartMillis = System.currentTimeMillis();
		resultException = null;
		simulationResult = null;
		
		entityScheduledNonMoveActions = new ArrayList<Entity>();
		entityScheduledMoveActions    = new LinkedList<Entity>();	
		
		actionGenerator = new InstantActions(config.actionExecutors);
		actionValidator = new InstantActions(config.actionExecutors);
		
		// ORDER ROOMS IN BFS MANNER
		dungeonBFSNumbering();
		
		// PREPARE AGENTS
		injectAgents();
		prepareAgents();
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
	
	private void injectAgents() {
		if (actionGenerator == null) throw new RuntimeException("Cannot agnet.setActionGenerator() as actionGenerator is null.");
		if (actionValidator == null) throw new RuntimeException("Cannot agnet.setActionValidator() as actionValidator is null.");
		
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			try {
				hero.mind.setActionGenerator(actionGenerator);
			} catch (Exception e) {
				throw new AgentException(hero, hero + " setActionGenerator() failed.", e);
			}
			try {
				hero.mind.setActionValidator(actionValidator);
			} catch (Exception e) {
				throw new AgentException(hero, hero + " setActionValidator() failed.", e);
			}
		}
		for (AgentMindBody<Monster, IMonsterAgent> monster : config.state.monsters.values()) {
			try {
				monster.mind.setActionGenerator(actionGenerator);
			} catch (Exception e) {
				throw new AgentException(monster, monster + " setActionGenerator() failed.", e);
			}	
			try {
				monster.mind.setActionValidator(actionValidator);
			} catch (Exception e) {
				throw new AgentException(monster, monster + " setActionValidator() failed.", e);
			}	
		}
		for (AgentMindBody<Feature, IFeatureAgent> feature : config.state.features.values()) {
			try {
				feature.mind.setActionGenerator(actionGenerator);
			} catch (Exception e) {
				throw new AgentException(feature, feature + " setActionGenerator() failed.", e);
			}	
			try {
				feature.mind.setActionValidator(actionValidator);
			} catch (Exception e) {
				throw new AgentException(feature, feature + " setActionValidator() failed.", e);
			}
		}
	}
	
	private void prepareAgents() {
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			try {
				hero.mind.prepareAgent();
			} catch (Exception e) {
				throw new AgentException(hero, hero + " prepareAgent() failed.", e);
			}
		}
		for (AgentMindBody<Monster, IMonsterAgent> monster : config.state.monsters.values()) {
			try {
				monster.mind.prepareAgent();
			} catch (Exception e) {
				throw new AgentException(monster, monster + " prepareAgent() failed.", e);
			}			
		}
		for (AgentMindBody<Feature, IFeatureAgent> feature : config.state.features.values()) {
			try {
				feature.mind.prepareAgent();
			} catch (Exception e) {
				throw new AgentException(feature, feature + " prepareAgent() failed.", e);
			}	
		}
	}
	
	private void startSimulation() {
		eventsTracker.event().simulationBegin(config.state);
		startAgents();
	}
	
	private void startAgents() {
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			try {
				hero.mind.simulationStarted();
			} catch (Exception e) {
				throw new AgentException(hero, hero + " simulationStarted() failed.", e);
			}
		}
		for (AgentMindBody<Monster, IMonsterAgent> monster : config.state.monsters.values()) {
			try {
				monster.mind.simulationStarted();
			} catch (Exception e) {
				throw new AgentException(monster, monster + " simulationStarted() failed.", e);
			}			
		}
		for (AgentMindBody<Feature, IFeatureAgent> feature : config.state.features.values()) {
			try {
				feature.mind.simulationStarted();
			} catch (Exception e) {
				throw new AgentException(feature, feature + " simulationStarted() failed.", e);
			}	
		}
	}
	
	private void endSimulation() {
		try {
			endAgents();
		} catch (Exception e) {			
		}
		try {
			eventsTracker.event().simulationEnd(simulationResult);
		} catch (Exception e) {			
		}		
	}
	
	private void endAgents() {
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			try {
				hero.mind.simulationEnded();
			} catch (Exception e) {
			}
		}
		for (AgentMindBody<Monster, IMonsterAgent> monster : config.state.monsters.values()) {
			try {
				monster.mind.simulationEnded();
			} catch (Exception e) {
			}			
		}
		for (AgentMindBody<Feature, IFeatureAgent> feature : config.state.features.values()) {
			try {
				feature.mind.simulationEnded();
			} catch (Exception e) {
			}	
		}
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
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			try {
				// OBSERVE
				hero.mind.observeBody(hero.body, currentTickMillis);
				hero.mind.observeDungeon(config.state.dungeon, true, currentTickMillis);			
				// ACT
				hero.body.action = hero.mind.act();
			} catch (Exception e) {
				throw new AgentException(hero, hero + " observe/act failed.", e);
			}
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
		for (AgentMindBody<Monster, IMonsterAgent> monster : config.state.monsters.values()) {
			if (!monster.body.alive) continue;
			try {
				// OBSERVE
				monster.mind.observeBody(monster.body, currentTickMillis);
				// ACT
				monster.body.action = monster.mind.act();
				if (monster.body.action != null) {
					monster.body.action.who = monster.body;
				}
			} catch (Exception e) {
				throw new AgentException(monster, monster + " observe/act failed.", e);
			}
			// SUBSCRIBE
			subscribeAction(monster.body);		
			// INFORM EVENT
			eventsTracker.event().actionSelected(monster.body, monster.body.action);
		}
	}
	
	private void gatherActionsFromFeatures() {
		// TICK ONLY FEATURES THAT ARE IN THE ROOM WITH A HERO
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			if (!hero.body.alive) continue;
			if (hero.body.atRoom == null) continue;
			if (hero.body.atRoom.feature == null) continue;
			if (!hero.body.atRoom.feature.alive) continue;
			// OBSERVE+ACT
			AgentMindBody<Feature, IFeatureAgent> feature = config.state.features.get(hero.body.atRoom.feature.id);
			try {
				feature.body.action = feature.mind.act(feature.body);
				if (feature.body.action != null) {
					feature.body.action.who = feature.body;
				}
			} catch (Exception e) {
				throw new AgentException(feature, feature + " act() failed.", e);
			}
			// SUBSCRIBE
			subscribeAction(feature.body);
			// INFORM EVENT
			eventsTracker.event().actionSelected(feature.body, feature.body.action);
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
		if (entityScheduledNonMoveActions.size() < 2) return;
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
		EEntity entityType = EH.getAs(entity.type, EEntity.class);		
		IInstantAction[] entityActions = config.actionExecutors[entityType.entityId];
		IInstantAction executor = entityActions[entity.action.type.id];
		boolean valid = executor != null && executor.isValid(entity, entity.action);
		if (valid) { 
			Command action = entity.action;
			eventsTracker.event().actionStarted(entity, entity.action);
			executor.run(entity, entity.action);
			eventsTracker.event().actionEnded(entity, action);
			checkDead(action.who);
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
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			if (hero.body.atCorridor != null && hero.body.atCorridor.monster != null) {
				// HERO IS IN A CORRIDOR WITH SOME MONSTER
				
				// TRIGGER AUTO-ATTACK ACTIONS
				if (hero.body.hand != null && hero.body.hand.type == EItem.SWORD) {
					// HERO AUTO-ATTACK
					
					// SAVE THE ACTION
					Command origHeroMoveAction = hero.body.action;
					
					// CREATE NEW ACTION
					hero.body.action = new Command(EAction.ATTACK, hero.body.atCorridor.monster);
					hero.body.action.who = hero.body;
					
					// PERFORM THE ACTION
					executeAction(hero.body);
					
					// RESTORE ORIG MOVE ACTION
					hero.body.action = origHeroMoveAction;
				} else {
					// MONSTER AUTO-ATTACK
					Monster monster = hero.body.atCorridor.monster;
					
					// SAVE THE ACTION
					Command origMonsterMoveAction = monster.action;
					
					// CREATE NEW ACTION
					monster.action = new Command(EAction.ATTACK, hero.body);
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
		return isVictory() || isLose() || isException();
	}

	private boolean isVictory() {
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			if (isHeroVictory(hero)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isHeroVictory(AgentMindBody<Hero, IHeroAgent> hero) {		
		return hero.body.alive && hero.body.atRoom != null && hero.body.atRoom.label != null && hero.body.atRoom.label == ERoomLabel.GOAL;
	}
	
	private SimResult createSimulationResult() {
		if (isVictory()) {
			return victory();
		}
		if (isLose()) {
			return lose();
		}
		if (isException()) {
			return resultException;
		}
		throw new RuntimeException("isVictory() is false, isLose() is false, isException() is false ...???");
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
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			if (isHeroVictory(hero)) {
				result.winner = hero;
				break;
			}
		}
		return result;
	}
	
	private boolean isLose() {
		for (AgentMindBody<Hero, IHeroAgent> hero : config.state.heroes.values()) {
			if (hero.body.alive) return false;
		}
		return true;
	}
	
	private SimResult lose() {
		SimResult result = newSimResult();		
		result.resultType = SimResultType.HEROES_LOSE;
		return result;
	}
	
	private boolean isException() {
		return resultException != null;
	}
	
	private SimResult exception(Throwable exception, SimResultType type) {
		SimResult result = newSimResult();		
		result.resultType = type;
		result.exception = exception;
		return resultException = result;
	}

}
