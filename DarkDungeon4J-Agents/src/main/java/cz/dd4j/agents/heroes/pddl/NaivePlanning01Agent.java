package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.AutoConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Martin on 23-Jun-17.
 */
@AutoConfig
public class NaivePlanning01Agent extends PDDLAgentBase {

    protected List<PDDLAction> currentPlan;
    private boolean stateChanged = false;
    private List<Room> monsterRooms;
    private List<Room> oldMonsterRooms;

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

        if (stateChanged) {
            return true;
        }

        return false;
    }

    @Override
    public Command act() {

        if (shouldReplan()) {
            currentPlan = plan();
        }

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

    @Override
    protected void processDungeonFull(Dungeon dungeon) {
        super.processDungeonFull(dungeon);
        monsterRooms = dungeon.rooms.values().stream().filter(r -> r.monster != null).collect(Collectors.toList());
    }

    @Override
    protected void processDungeonUpdate(Dungeon dungeon) {
        oldMonsterRooms = new ArrayList<>(monsterRooms);

        super.processDungeonUpdate(dungeon);

        monsterRooms = dungeon.rooms.values().stream().filter(r -> r.monster != null).collect(Collectors.toList());

        if ((monsterRooms.size() == oldMonsterRooms.size() || monsterRooms.size() == oldMonsterRooms.size() - 1) &&
             (oldMonsterRooms.containsAll(monsterRooms))) {
            stateChanged = false;
        } else {
            stateChanged = true;
        }


    }

}
