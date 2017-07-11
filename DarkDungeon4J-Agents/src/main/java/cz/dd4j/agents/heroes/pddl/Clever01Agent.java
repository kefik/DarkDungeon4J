package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;
import cz.dd4j.utils.csv.CSV;

import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class Clever01Agent extends PDDLAgentBase {

    @Configurable
    protected int dangerThreshold = 2;

    @Configurable
    protected int safeThreshold = 3;

    protected List<PDDLAction> currentPlan;
    private boolean reactiveActionTaken;
    private boolean reactiveEscape = false;

    protected int reactiveActions = 0;

    @Override
    public void prepareAgent() {
        super.prepareAgent();
    }

    private Monster dangerousMonster = null;

    protected boolean shouldReplan() {

        if (reactiveActionTaken)
            return true;

        if (currentPlan == null || currentPlan.isEmpty()) //no plan or plan finished
            return true;

        PDDLAction action = currentPlan.get(0);
        return !actionValidator.isValid(hero, translateAction(action));

    }

    private Command getBestReactiveAction() {
        reactiveActions++;
        reactiveActionTaken = true;
        List<Command> availableActions = actionsGenerator.generateFor(hero);

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
        if (dng == 0) //dead-end state
            return null;

        if (dng <= dangerThreshold && hero.atRoom.feature == null) {
            reactiveEscape = true;
            dangerousMonster = getClosestMonster(hero.atRoom);
        }

        if (dng >= safeThreshold) {
            reactiveEscape = false;
            currentPlan = plan(String.format("(and (alive)(has_sword)(not(monster_at %s))", dangerousMonster.atRoom.id.name));
            dangerousMonster = null;
        }

        if (reactiveEscape)
            return getBestReactiveAction();

        if (shouldReplan()) {
            currentPlan = plan();
            if (currentPlan == null) { //planner failed to produce plan
                return getBestReactiveAction();
            }
        }

        reactiveActionTaken = false;

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

    private int evaluateCommand(Command cmd) {

        return dangAfterAction(cmd);
    }

    @Override
    public List<String> getCSVHeaders() {
        List<String> headers = super.getCSVHeaders();
        headers.add("reactive_steps");
        return headers;
    }

    @Override
    public CSV.CSVRow getCSVRow() {
        CSV.CSVRow row = super.getCSVRow();
        row.add("reactive_steps", reactiveActions);
        return row;
    }

}
