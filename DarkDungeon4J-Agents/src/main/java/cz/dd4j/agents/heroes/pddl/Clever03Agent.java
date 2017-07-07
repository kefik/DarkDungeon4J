package cz.dd4j.agents.heroes.pddl;

import com.sun.istack.internal.NotNull;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class Clever03Agent extends PDDLAgentBase {

    @Configurable
    protected int dangerThreshold = 2;

    @Configurable
    protected int safeThreshold = 3;

    protected List<PDDLAction> currentPlan;
    private boolean reactiveActionTaken;
    private boolean reactiveEscape = false;

    @Override
    public void prepareAgent() {
        super.prepareAgent();
    }

    protected boolean shouldReplan() {

        if (reactiveActionTaken)
            return true;

        if (currentPlan == null || currentPlan.isEmpty()) //no plan or plan finished
            return true;

        PDDLAction action = currentPlan.get(0);
        return !actionValidator.isValid(hero, translateAction(action));

    }

    private Command getBestReactiveAction() {
        reactiveActionTaken = true;
        List<Command> availableActions = actionsGenerator.generateFor(hero);

        System.out.println("Reactive action");

        Command selectedAction = null;
        int bestVal = Integer.MIN_VALUE;
        for (Command c: availableActions) {
            int val = evaluateCommand(c);
            if (val > bestVal) {
                selectedAction = c;
                bestVal = val;
            }
        }

        return selectedAction;
    }

    @Override
    public Command act() {

        int dng = dang(hero.atRoom);
        if (dng == 0) {
            return null;
        }

        if (dng >= safeThreshold) {
            reactiveEscape = false;
        }

        if (reactiveEscape)
            return getBestReactiveAction();

        if (shouldReplan()) {
            currentPlan = plan();
        }

        if (currentPlan == null) { //no plan found in previous step
            return getBestReactiveAction();
        }

        Command cmd = translateAction(currentPlan.remove(0));
        if (evaluateCommand(cmd) <= dangerThreshold) { //this action would lead to danger state
            reactiveEscape = true;
            System.out.println("Planned action would be: " + cmd.toString() + "dang: " + evaluateCommand(cmd));
            return getBestReactiveAction();
        }

        return cmd;

    }
    private int evaluateCommand(@NotNull Command cmd) {

        return dangAfterAction(cmd);
    }

}
