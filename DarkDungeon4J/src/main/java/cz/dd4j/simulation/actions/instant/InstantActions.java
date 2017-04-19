package cz.dd4j.simulation.actions.instant;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.actions.IActionsValidator;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;

public class InstantActions implements IActionsGenerator, IActionsValidator {

	/**
	 * [EEntity.id][EAction.id]
	 */
	private IInstantAction[][] actionExecutors;
	
	/**
	 * @param actionExecutors [EEntity.id][EAction.id]
	 */
	public InstantActions(IInstantAction[][] actionExecutors) {
		this.actionExecutors = actionExecutors;
	}

	@Override
	public List<Command> generateFor(Entity entity) {
		List<Command> result = new ArrayList<Command>();
				
		if (entity == null) return result;
		if (entity.type == null) return result;
		
		EEntity entityType = EH.getAs(entity.type, EEntity.class);
		
		if (entityType.entityId < 0 || entityType.entityId >= actionExecutors.length) return result;
		if (actionExecutors[entityType.entityId] == null) return result;
		
		for (IInstantAction action : actionExecutors[entityType.entityId]) {
			if (action == null) continue;
			action.generateActionsFor(entity, result);
		}
		
		return result;
	}
	
	@Override
	public List<Command> generateFor(Entity entity, EAction actionType) {
		List<Command> result = new ArrayList<Command>();
		
		if (entity == null) return result;
		if (entity.type == null) return result;
		
		EEntity entityType = EH.getAs(entity.type, EEntity.class);		
		
		if (entityType.entityId < 0 || entityType.entityId >= actionExecutors.length) return result;
		if (actionExecutors[entityType.entityId] == null) return result;
		
		if (actionType == null) return null;
		if (actionType.id < 0 || actionType.id >= actionExecutors[entityType.entityId].length) return result;
		if (actionExecutors[entityType.entityId][actionType.id] == null) return result;
		
		actionExecutors[entityType.entityId][actionType.id].generateActionsFor(entity, result);
		
		return result;
	}

	@Override
	public boolean isValid(Entity entity, Command action) {
		if (entity == null) return false;
		if (entity.type == null) return false;
		EEntity entityType = EH.getAs(entity.type, EEntity.class);
		
		if (action == null) return false;
		if (action.type == null) return false;		
		if (action.type.id < 0 || action.type.id > actionExecutors[entityType.entityId].length) return false;
		
		return actionExecutors[entityType.entityId][action.type.id].isValid(entity, action);		
	}
	
}
