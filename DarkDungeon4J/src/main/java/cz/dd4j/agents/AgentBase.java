package cz.dd4j.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.actions.IActionsGenerator;
import cz.dd4j.simulation.actions.IActionsValidator;
import cz.dd4j.simulation.data.dungeon.elements.entities.Entity;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.IConfigurable;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class AgentBase implements IAgent, IConfigurable {

	private final EEntity agentType;
	
	protected Random random;
	protected IActionsGenerator actionsGenerator;
	protected IActionsValidator actionValidator;
	protected IAgentLog log;
	

	public AgentBase(EEntity agentType) {
		this.agentType = agentType;
	}

	@Override
	public EEntity getAgentType() {
		return agentType;
	}
	
	// =============
	// CONFIGURATION
	// =============
	
	@Override
	public void setRandom(Random random) {
		this.random = random;
	}
	
	@Override
	public void setActionGenerator(IActionsGenerator actionGenerator) {
		this.actionsGenerator = actionGenerator;
	}
	
	@Override
	public void setActionValidator(IActionsValidator actionValidator) {
		this.actionValidator = actionValidator;
	}
	
	@Override
	public void setLog(IAgentLog log) {
		this.log = log;
	}
	
	@Override
	public void configure(ConfigMap config) {
		if (getClass().isAnnotationPresent(AutoConfig.class)) config.autoConfig(this);
	}
	
	// =====================
	// SIMULATION LIFE-CYCLE
	// =====================
	
	@Override
	public void prepareAgent() {
	}
	
	@Override
	public void simulationStarted() {
	}
	
	@Override
	public void agentDead() {
	}
	
	@Override
	public void simulationEnded() {		
	}
	
	// ===========
	// AGENT UTILS
	// ===========
	
	/**
	 * Generates random action for the entity given its 'body' state using {@link #actionsGenerator} provided by the simulation through {@link #setActionGenerator(IActionsGenerator)}.
	 * @param body
	 * @param noActionAllowed whether to allow use of "no-action"; even if false may return null if no action is possible
	 * @return
	 */
	protected Command getRandomAction(Entity body, boolean noActionAllowed) {
		return getRandomAction(actionsGenerator.generateFor(body), noActionAllowed);
	}
	
	/**
	 * Generates random action of given 'actionType' for the entity given its 'body' state using {@link #actionsGenerator} provided by the simulation through {@link #setActionGenerator(IActionsGenerator)}.
	 * @param body
	 * @param actionType
	 * @param noActionAllowed whether to allow use of "no-action"; even if false may return null if no action of 'actionType' is possible
	 * @return
	 */
	protected Command getRandomAction(Entity body, EAction actionType, boolean noActionAllowed) {
		return getRandomAction(actionsGenerator.generateFor(body, actionType), noActionAllowed);
	}
	
	/**
	 * Returns random action from 'actions'. Also generates "no-actions", does not need to b
	 * @param actions
	 * @param noActionAllowed whether to allow use of "no-action"; even if false may return null if no action is possible
	 * @return
	 */
	protected Command getRandomAction(List<Command> actions, boolean noActionAllowed) {
		if (actions == null || actions.size() == 0) return null;
		if (noActionAllowed) {
			int actionIndex = random.nextInt(actions.size()+1);
			if (actionIndex == actions.size()) return null;
			return actions.get(actionIndex);
		} else {
			return actions.get(random.nextInt(actions.size()));
		}
	}


	/**
	 * To be overridden by subclasses if required, so far, empty implementation.
	 * If overridden, the subclass should use super.getCSVHeaders() first, add own details and return it then.
	 */
	@Override
	public List<String> getCSVHeaders() {
		return new ArrayList<String>();
	}

	/**
	 * To be overridden by subclasses if required, so far, empty implementation.
	 * If overridden, the subclass should use super.getCSVRow() first, add own details and return it then.
	 */
	@Override
	public CSVRow getCSVRow() {
		return new CSVRow();
	}
	
	/**
	 * Report agent-custom log.
	 * @param level
	 * @param msg
	 */
	public void log(Level level, String msg) {
		if (log != null) log.log(level, msg);
	}

}
