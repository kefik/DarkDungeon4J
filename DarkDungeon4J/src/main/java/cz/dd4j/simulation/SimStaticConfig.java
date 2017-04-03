package cz.dd4j.simulation;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.domain.DomainInit;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.instant.IInstantActionExecutor;
import cz.dd4j.simulation.data.agents.actions.EAction;
import cz.dd4j.simulation.data.agents.heroes.Heroes;
import cz.dd4j.simulation.data.state.HeroMindBody;
import cz.dd4j.simulation.data.state.SimState;

public class SimStaticConfig {
	
	static {
		DomainInit.init();
	}

	// ============
	// CONFIG STATE
	// ============
	
	private boolean simStateBound = false;
	private boolean[] entityActionsBound = new boolean[EEntity.LAST_ID+1];
	private boolean heroesBound = false;
	
	// ====================
	// ACTION CONFIGURATION
	// ====================
	
	// DO NOT MODIFY FROM THE OUTSIDE!
	// Always use bind...() methods!
	
	/**
	 * [EEntity.id][EAction.id]
	 */
	public IInstantActionExecutor[][] actionExecutors;
	
	// =========
	// SIM STATE
	// =========
	
	public SimState state;
	
	public SimStaticConfig() {
		for (int i = 0; i < entityActionsBound.length; ++i) {
			entityActionsBound[i] = false;
		}
	}
	
	public boolean isReady() {
		return simStateBound && isEntitiesActionsBound() && heroesBound;
	}
	
	public boolean isEntitiesActionsBound() {
		if (actionExecutors == null) return false;
		for (EEntity entity : EH.enums(EEntity.class)) {
			if (!isEntityActionsBound(entity)) return false;
		}
		return true;
	}
	
	public boolean isEntityActionsBound(EEntity entity) {
		if (actionExecutors == null) return false;
		return entityActionsBound[entity.id];
	}

	public String getMissingInitDescription() {
		String desc = "";
		
		desc += (simStateBound ? "" : "SimState not bound! Call bindSimState(...) first. ");
		desc += (isEntityActionsBound(EEntity.HERO) ? "" : "Hero action executors not bound! Call bindEntityActions(...) for EEntity.HERO first. ");
		desc += (isEntityActionsBound(EEntity.MONSTER) ? "" : "Monster action executors not bound! Call bindEntityActions(...) for EEntity.MONSTER first. ");
		desc += (isEntityActionsBound(EEntity.FEATURE) ? "" : "Feature action executors not bound! Call bindEntityActions(...) for EEntity.FEATURE first. ");
		desc += (heroesBound ? "" : "Hero agents not bound! Call bindHeroes(...) first. ");
		
		if (desc.length() == 0) return "ALL OK!";
		
		return desc;
	}
	
	// =======================
	// ONE-TIME INITIALIZATION
	// =======================
	
	public void bindSimState(SimState state) {
		if (simStateBound) throw new RuntimeException("You cannot bindSimState() twice!");
		
		this.state = state;		
		
		simStateBound = true;
	}
	
	private void ensureActionExecutors() {
		if (actionExecutors == null) {
			this.actionExecutors = new IInstantActionExecutor[EEntity.LAST_ID+1][EAction.LAST_ID+1];
		}
	}
	
	public void bindActions(EEntity type, IInstantActionExecutor... actionExecutors) {
		if (entityActionsBound[type.id]) throw new RuntimeException("You cannot bindActions() for EEntity." + EH.getEnumObject(type).name + " twice!");
		
		ensureActionExecutors();
		
		for (IInstantActionExecutor executor : actionExecutors) {
			this.actionExecutors[type.id][executor.getType().id] = executor;
		}
		
		this.entityActionsBound[type.id] = true;
	}
	
	public void bindHeroes(Heroes heroes) {
		if (heroesBound) throw new RuntimeException("You cannot bindHeroes() twice!");
		
		for (HeroMindBody hero : state.heroes.values()) {
			if (!heroes.heroes.containsKey(hero.body.id)) {
				throw new RuntimeException("Cannot bind mind into hero body for Hero[id=" + hero.body.id + "], " + hero.body.id + " not found in 'heroes'.");
			}
			hero.mind = heroes.heroes.get(hero.body.id);
		}
		
		heroesBound = true;		
	}
	
}
