package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class Clever02Agent extends PDDLAgentBase {

    @Configurable
    protected int threshold = 2;

    protected List<PDDLAction> currentPlan;

    @Override
    public void prepareAgent() {
        super.prepareAgent();
    }

    protected boolean shouldReplan() {

        if (currentPlan == null || currentPlan.isEmpty()) //no plan or plan finished
            return true;

        PDDLAction action = currentPlan.get(0);
        return !actionValidator.isValid(hero, translateAction(action));

    }

    @Override
    public Command act() {

        int dng = dang(hero.atRoom);
        System.out.println("dang: " + dng);
        if (dng <= threshold) {
            currentPlan = plan("(and (alive)(has_sword))");
        } else if (shouldReplan()) {
            currentPlan = plan();
        }

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

}
