package cz.dd4j.agents.heroes.pddl;

import com.sun.istack.internal.NotNull;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.entities.features.Trap;
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
        return availableActions.stream().max(Comparator.comparingInt(this::evaluateCommand)).orElse(null);
    }

    @Override
    public Command act() {

        int dng = dang(hero.atRoom);
        if (dng == 0) //dead-end state
            return null;

        if (dng <= dangerThreshold) {
            reactiveEscape = true;
        }

        if (dng >= safeThreshold) {
            reactiveEscape = false;
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

    private int evaluateCommand(@NotNull Command cmd) {

        // room with trap, anything except disarm is dead-end, disarm is distance to closest monster - 1 (monster can move)
        if (hero.atRoom.feature != null) {
            if (cmd.isType(EAction.DISARM)) {
                // this is technically not correct (see comment above), but leads to always selecting DISARM action
                return 1;
            }
            else
                return 0;
        }

        // room with monster
        if (hero.atRoom.monster != null) {
            if (hero.hand == null) {
                return 0;
            }
            Monster m = hero.atRoom.monster;
            hero.atRoom.monster = null;
            int val = evaluateCommand(cmd);
            hero.atRoom.monster = m;
            return val;
        }

        //for moves, the value is the distance to closest monster from the target room - 1 (the monsters can move)
        if (cmd.isType(EAction.MOVE)) {
            return getClosestMonsterDistance(dungeon.rooms.get(cmd.target.id)) - 1;
        }

        //for sword dropping, the value is the distance to closest monster from the current room - 1 (monsters move)
        if (cmd.isType(EAction.DROP)) {
            return getClosestMonsterDistance(hero.atRoom) - 1;
        }

        //after picking up sword (in a room without trap), the hero is safe
        if (cmd.isType(EAction.PICKUP)) {
            return Integer.MAX_VALUE;
        }

        return 0;
    }

}
