package cz.dd4j.agents.heroes.planners;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.agents.heroes.pddl.InputFiles;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.data.dungeon.Dungeon;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 21-Jun-17.
 */
@AutoConfig
public class PDDLInputGenerator {

    @Configurable
    protected File domainFile = new File("./DarkDungeon.pddl");

    @Configurable
    protected String domainName = "DarkDungeon";

    private String pddlNewLine = Const.NEW_LINE;
    private File agentWorkingDir;
    private StringBuffer pddlStaticPartCache = new StringBuffer();

    public void setPddlNewLine(String pddlNewLine) {
        this.pddlNewLine = pddlNewLine;
    }

    public PDDLInputGenerator(File workingDir) {
        agentWorkingDir = workingDir;
    }


    public void prepareStaticPart(Dungeon dungeon, String domainName, List<Room> roomsWithSword) {

        pddlStaticPartCache.setLength(0);

        StringBuffer sb = pddlStaticPartCache;

        //(define (problem p1)
        sb.append("(define (problem p1)");
        sb.append(pddlNewLine);

        //(:domain DarkDungeon)
        sb.append("(:domain " + domainName + ")");
        sb.append(pddlNewLine);
        sb.append(pddlNewLine);

        //(:objects r1 r2 r3 r4 - room
        //          s - sword)
        sb.append("(:objects");
        for (Room room : dungeon.rooms.values()) {
            sb.append(" ");
            sb.append(room.id.name);
        }
        sb.append(" - room");
        sb.append(pddlNewLine);

        sb.append("          ");
        boolean atLeastOneSword = false;
        for (Room room : roomsWithSword) {
            if (room.item != null && EH.isA(room.item.type, EItem.SWORD)) {
                sb.append(" ");
                sb.append(room.item.id.name);
                atLeastOneSword = true;
            }
        }
        if (atLeastOneSword) {
            sb.append(" - sword)");
        } else {
            sb.append(")");
        }
        sb.append(pddlNewLine);
        sb.append(pddlNewLine);

        //(:init
        sb.append("(:init");
        sb.append(pddlNewLine);
        sb.append(pddlNewLine);

        // GRAPH
        //    (connected r1 r2)
        //    ...

        for (Room room : dungeon.rooms.values()) {
            for (Corridor corridor : room.corridors) {
                Room other = corridor.getOtherRoom(room);
                sb.append("    (connected " + room.id.name + " " + other.id.name + ")");
                sb.append(pddlNewLine);
            }
        }
    }


    public InputFiles generateFiles(Hero hero,
                                    Map<Id, Monster> monsters,
                                    Map<Id, Feature> features,
                                    List<Room> roomsWithSword,
                                    List<Room> goalRooms,
                                    File problemFile,
                                    File domainFile) {
        if (goalRooms.size() > 0) {
            String goal =  "(and (alive)(hero_at " + goalRooms.get(0).id.name + "))";
            return generateFiles(hero, monsters, features, roomsWithSword, goal, problemFile, domainFile);
        } else {
            throw new RuntimeException("goalRooms.size() == 0, invalid");
        }

    }

    public InputFiles generateFiles(Hero hero,
                                    Map<Id, Monster> monsters,
                                    Map<Id, Feature> features,
                                    List<Room> roomsWithSword,
                                    String goal,
                                    File problemFile,
                                    File domainFile) {
        InputFiles inputs = new InputFiles();
        inputs.domainFile = domainFile;

        inputs.problemFile = generateProblemFile(hero, monsters, features, roomsWithSword, goal, problemFile);

        return inputs;
    }


    protected File generateProblemFile(Hero hero,
                                       Map<Id, Monster> monsters,
                                       Map<Id, Feature> features,
                                       List<Room> roomsWithSword,
                                       String goal,
                                       File problemFile) {
        if (problemFile == null) {
            problemFile = getWorkingDir("problem.pddl");
        }
        FileOutputStream outStream = null;
        PrintWriter print = null;
        try {
            outStream = new FileOutputStream(problemFile);
            print = new PrintWriter(outStream);
            generateProblem(hero, monsters, features, roomsWithSword, goal, print);
        } catch (Exception e) {
            if (print != null) {
                try {
                    print.close();
                } catch (Exception e1) {
                    print = null;
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception e2) {
                    outStream = null;
                }
            }
            try {
                problemFile.delete();
            } catch (Exception e3) {
            }
            throw new RuntimeException("Failed to generate PDDL problem file into: " + problemFile.getAbsolutePath(), e);
        } finally {
            if (print != null) {
                try {
                    print.close();
                } catch (Exception e) {
                    print = null;
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception e) {
                    outStream = null;
                }
            }
        }
        return problemFile;
    }

    protected File getWorkingDir(String name) {
        return new File(agentWorkingDir, name);
    }

    void generateProblem(Hero hero,
                         Map<Id, Monster> monsters,
                         Map<Id, Feature> features,
                         List<Room> roomsWithSword,
                         String goal,
                         PrintWriter print) throws IOException {
        //(define (problem p1)
        print.print(pddlStaticPartCache.toString());
        print.print(pddlNewLine);

        //    (alive)
        if (hero.alive) {
            print.print("    (alive)");
            print.print(pddlNewLine);
        }

        //    (has_sword)
        if (hero.hand != null && EH.isA(hero.hand.type, EItem.SWORD)) {
            print.print("    (has_sword)");
            print.print(pddlNewLine);
        } else {
            print.print("    (empty_handed)");
            print.print(pddlNewLine);
        }

        // ENTITIES
        //    (monster_at r2)
        //    (trap_at r3)
        //    (sword_at r1)
        //    (hero_at r1)

        // monsters...
        for (Monster monster : monsters.values()) {
            if (monster.alive && monster.atRoom != null) {
                print.print("    (monster_at " + monster.atRoom.id.name + ")");
                print.print(pddlNewLine);
            }
        }
        // traps
        for (Feature feature : features.values()) {
            if (feature.alive && EH.isA(feature.type, EFeature.TRAP) && feature.atRoom != null) {
                print.print("    (trap_at " + feature.atRoom.id.name + ")");
                print.print(pddlNewLine);
            }
        }
        // swords
        for (Room room : roomsWithSword) {
            print.print("    (sword_at " + room.id.name + ")");
            print.print(pddlNewLine);
        }
        // hero
        if (hero.atRoom != null) {
            print.print("    (hero_at " + hero.atRoom.id.name + ")");
            print.print(pddlNewLine);
        } else {
            throw new RuntimeException("hero.atRoom is null, invalid");
        }

        //)
        print.print(")");
        print.print(pddlNewLine);

        print.print("");
        print.print(pddlNewLine);

        //(:goal (and (alive)(hero_at r4)))
        if (goal != null) {
            print.print("(:goal " + goal + ")");
        }

        print.print("");
        print.print(pddlNewLine);
        //)
        print.print(")");
        print.print(pddlNewLine);
    }

}
