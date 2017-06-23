package cz.dd4j.agents.heroes.planners;

import cz.dd4j.agents.heroes.pddl.PDDLAction;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 21-Jun-17.
 */
@AutoConfig
public class NPlanNativeExecutor extends AbstractPlannerExecutor {

    @Configurable
    protected File nplanFile = new File("./nplan/nplan");

    protected File agentWorkingDir;
    protected File nplanWorkingDir;

    protected File nplanWorkingFile;

    @Override
    public List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException {

        File resultFile = new File(nplanWorkingDir, "plan.SOL");

        FileUtils.copyFile(domainFile, new File(nplanWorkingDir, "domain.pddl"));
        FileUtils.copyFile(domainFile, new File(nplanWorkingDir, "problem.pddl"));

        Map<String, String> config = new HashMap<String, String>();
        config.put("domain", "domain.pddl");
        config.put("problem", "problem.pddl");

        CommandLine commandLine = new CommandLine("nplan");
        commandLine.addArgument("${domain}");
        commandLine.addArgument("${problem}");
        commandLine.addArgument("-o");
        commandLine.addArgument(resultFile.getAbsolutePath());
        commandLine.addArgument("-Q");
        commandLine.setSubstitutionMap(config);

        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(nplanWorkingDir);
        executor.setExitValue(1);

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
        this.agentWorkingDir = agentWorkingDir;
        nplanWorkingDir = new File(this.agentWorkingDir, "nplan");
        nplanWorkingDir.mkdirs();
        nplanWorkingDir.deleteOnExit();

        // TRY TO COPY OUR PLANNER
        nplanWorkingFile = new File(nplanWorkingDir, "nplan");
        try {
            FileUtils.copyFile(nplanFile, nplanWorkingFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy nplan planner from '" + nplanFile.getAbsolutePath() + "' into '" + nplanWorkingFile.getAbsolutePath() + "'.", e);
        }
        nplanWorkingFile.deleteOnExit();
    }
}
