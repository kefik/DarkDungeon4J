package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.utils.config.AutoConfig;

import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class NaivePlanning02Agent extends PDDLAgentBase {

    protected List<PDDLAction> currentPlan;

    @Override
    public void prepareAgent() {
        super.prepareAgent();
    }

    public boolean shouldReplan() {
        if (currentPlan == null || currentPlan.isEmpty()) //no plan or plan finished
            return true;

        PDDLAction action = currentPlan.get(0);
        if (!actionValidator.isValid(hero, translateAction(action))) { //next action is not applicable
            return true;
        }

        return false;
    }

    @Override
    public Command act() {

        if (shouldReplan()) {
            currentPlan = plan();
        }

        if (currentPlan == null) {
            return null;
        }

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

}
