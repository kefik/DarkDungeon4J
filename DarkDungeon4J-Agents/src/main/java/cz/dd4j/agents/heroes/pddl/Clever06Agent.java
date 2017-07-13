package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;
import cz.dd4j.utils.csv.CSV;

import java.util.List;

/**
 * Created by Martin on 22-Jun-17.
 */
@AutoConfig
public class Clever06Agent extends PDDLAgentBase {

    @Configurable
    protected int dangerThreshold = 2;

    @Configurable
    protected int safeThreshold = 3;

    protected List<PDDLAction> currentPlan;
    private boolean reactiveActionTaken;
    private boolean reactiveEscape = false;

    protected boolean removingDanger;

    protected int reactiveActions = 0;

    protected Monster dangerousMonster = null;

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
        reactiveActions++;
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

        if (removingDanger && currentPlan != null) {
            if (currentPlan.isEmpty()) {
                System.out.println("Danger removed");
                removingDanger = false;
            } else {
                Command act = translateAction(currentPlan.remove(0));
                if (evaluateCommand(act) > 0) {
                    return act;
                } else {
                    System.out.println("Greater danger found");
                    dangerousMonster = getClosestMonster(hero.atRoom);
                    reactiveEscape = true;
                    removingDanger = false;
                }
            }
        }

        if (reactiveEscape && dng >= safeThreshold) {
            reactiveEscape = false;
            currentPlan = plan(String.format("(and (alive)(has_sword)(not(monster_at %s)))", dangerousMonster.atRoom.id.name));
            dangerousMonster = null;
            if (currentPlan != null && !currentPlan.isEmpty()) {
                reactiveActionTaken = false;
                removingDanger = true;
                return translateAction(currentPlan.remove(0));
            }
        }

        if (reactiveEscape)
            return getBestReactiveAction();

        if (shouldReplan()) {
            currentPlan = plan();
            removingDanger = false;
        }

        if (currentPlan == null) { //no plan found in previous step
            return getBestReactiveAction();
        }

        Command cmd = translateAction(currentPlan.remove(0));
        if (evaluateCommand(cmd) <= dangerThreshold && !(cmd.isType(EAction.MOVE) && ((Room)cmd.target).feature != null)) { //this action would lead to danger state
            reactiveEscape = true;
            if (cmd.isType(EAction.MOVE))
                dangerousMonster = getClosestMonster((Room) cmd.target);
            else
                dangerousMonster = getClosestMonster(hero.atRoom);
            System.out.println("Planned action would be: " + cmd.toString() + " dang: " + evaluateCommand(cmd));
            return getBestReactiveAction();
        }

        reactiveActionTaken = false;
        return cmd;

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
