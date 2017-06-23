package cz.dd4j.agents.heroes.planners;

import cz.dd4j.agents.heroes.pddl.PDDLAction;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.StreamHandler;

/**
 * Created by Martin on 21-Jun-17.
 */
public class NPlanDockerExecutor extends AbstractPlannerExecutor {

    File nplanWorkingDir;

    @Override
    public List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException {

        FileUtils.copyFile(domainFile, new File(nplanWorkingDir, "domain.pddl"));
        FileUtils.copyFile(problemFile, new File(nplanWorkingDir, "problem.pddl"));

        File resultFile = new File(nplanWorkingDir, "plan.SOL");

        Map<String, String> config = new HashMap<String, String>();
        config.put("workingDir", nplanWorkingDir.getAbsolutePath());
        config.put("domainFile", "domain.pddl");
        config.put("problemFile", "problem.pddl");

        //docker run -v ${PWD}:/data --rm mpilat/mpc DarkDungeon.pddl p1.pddl -o p1.SOL
        CommandLine commandLine = new CommandLine("docker");
        commandLine.addArgument("run");
        commandLine.addArgument("-v");
        commandLine.addArgument("${workingDir}:/data");
        commandLine.addArgument("--rm");
        commandLine.addArgument("nplan");
        commandLine.addArgument("${domainFile}");
        commandLine.addArgument("${problemFile}");
        commandLine.addArgument("-o");
        commandLine.addArgument("plan.SOL");
        commandLine.addArgument("-Q");
        commandLine.setSubstitutionMap(config);

        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(nplanWorkingDir);
        executor.setExitValue(0);

        // SYNC EXECUTION
        executor.execute(commandLine);

        // NOW RESULT FILE SHOULD BE READY
        if (!resultFile.exists()) {
            // TODO: logging
            // nplan failed to produce results
            return null;
        }

        // PROCESS RESULT
        String resultLines = FileUtils.readFileToString(resultFile);

        // DELETE INTERMEDIATE FILE
        resultFile.delete();

        // PARSE LINES AS PDDL ACTION
        return parseLines(resultLines);

    }

    @Override
    public void prepareEnvironment(File agentWorkingDir) {

        nplanWorkingDir = new File(agentWorkingDir, "nplan");
        nplanWorkingDir.mkdirs();

    }
}
