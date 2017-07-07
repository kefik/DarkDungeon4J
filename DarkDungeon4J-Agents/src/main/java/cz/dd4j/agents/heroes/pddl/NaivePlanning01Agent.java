package cz.dd4j.agents.heroes.pddl;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.config.AutoConfig;

import java.util.ArrayList;
import java.util.List;

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

        if (hero.atRoom.monster != null && hero.hand == null)
            return null;

        if (shouldReplan()) {
            currentPlan = plan();
        }

        if (currentPlan == null)
            return null;

        PDDLAction currentAction = currentPlan.remove(0);
        return translateAction(currentAction);
    }

    @Override
    protected void processDungeonFull(Dungeon dungeon) {
        super.processDungeonFull(dungeon);
        monsterRooms = new ArrayList<Room>();
        for (Room r: dungeon.rooms.values()) {
            if (r.monster != null) {
                monsterRooms.add(r);
            }
        }
    }

    @Override
    protected void processDungeonUpdate(Dungeon dungeon) {
        oldMonsterRooms = new ArrayList<Room>(monsterRooms);

        super.processDungeonUpdate(dungeon);

        monsterRooms = new ArrayList<Room>();
        for (Room r: dungeon.rooms.values()) {
            if (r.monster != null) {
                monsterRooms.add(r);
            }
        }

        if ((monsterRooms.size() == oldMonsterRooms.size() || monsterRooms.size() == oldMonsterRooms.size() - 1) &&
             (oldMonsterRooms.containsAll(monsterRooms))) {
            stateChanged = false;
        } else {
            stateChanged = true;
        }


    }

}
