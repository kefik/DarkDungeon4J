package cz.dd4j.agents.heroes.planners;

import cz.dd4j.agents.heroes.pddl.PDDLAction;
import cz.dd4j.utils.Const;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 21-Jun-17.
 */
public abstract class AbstractPlannerExecutor {

    protected File problemFile;

    public abstract List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException;
    public abstract void prepareEnvironment(File agentWorkingDir);

    public File getProblemFile() {
        return this.problemFile;
    }

    public String getPddlNewLine() {
        return Const.NEW_LINE;
    }

    protected List<PDDLAction> parseLines(String lines) {
        if (lines == null || lines.trim().length() == 0) {
            return null;
        }

        String[] parts = lines.split("\n");
        List<PDDLAction> result = new ArrayList<PDDLAction>(parts.length);
        for (String line : parts) {
            if (line.endsWith("\r")) line = line.substring(0, line.length()-1);
            PDDLAction action = PDDLAction.parseSOL(line);
            if (action != null) {
                result.add(action);
            }
        }

        return result;
    }
}
