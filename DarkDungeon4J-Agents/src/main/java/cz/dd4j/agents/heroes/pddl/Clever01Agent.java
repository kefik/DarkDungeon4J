package cz.dd4j.agents.heroes.pddl;

import com.sun.istack.internal.NotNull;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class Clever01Agent extends PDDLAgentBase {

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
            System.out.println("Reactive action");
            List<Command> availableActions = actionsGenerator.generateFor(hero);
            return availableActions.stream().min(Comparator.comparingInt(this::evaluateCommand)).orElse(null);
        } else if (shouldReplan()) {
            currentPlan = plan();
        }

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

    private int evaluateCommand(@NotNull Command cmd) {

        if (cmd.isType(EAction.MOVE)) { //for moves, the value is the dang of the target room
            return dang(dungeon.rooms.get(cmd.target.id));
        }

        return 0;
    }

}
