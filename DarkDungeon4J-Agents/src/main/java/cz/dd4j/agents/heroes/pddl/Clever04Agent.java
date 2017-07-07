package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EElement;
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
public class Clever04Agent extends PDDLAgentBase {

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

        if (dang(hero.atRoom) == 0) {
            return null;
        }

        if (shouldReplan()) {
            currentPlan = plan();
        }

        if (currentPlan == null) {
            return null;
        }

        PDDLAction currentAction = currentPlan.remove(0);
        Command cmd = translateAction(currentAction);

        int dng = dangAfterAction(cmd);

        if (dng <= threshold) {
            if (cmd.isType(EAction.MOVE) && ((Room)cmd.target).feature != null) { //going to disable the trap, ignore danger
                return cmd;
            }
            List<PDDLAction> safePlan = null;
            if (cmd.isType(EAction.MOVE)) {
                Monster m = getClosestMonster((Room)cmd.target);
                safePlan = plan("(and (alive)(has_sword)(not(monster_at " + m.atRoom.id.toString() + ")))");
            } else {
                Monster m = getClosestMonster(hero.atRoom);
                safePlan = plan("(and (alive)(has_sword)(not(monster_at " + m.atRoom.id.toString() + ")))");
            }
            if (safePlan != null) {
                currentPlan = safePlan;
            }
        } else {
            return cmd;
        }

        if (currentPlan == null) {
            return null;
        }

        currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

}
