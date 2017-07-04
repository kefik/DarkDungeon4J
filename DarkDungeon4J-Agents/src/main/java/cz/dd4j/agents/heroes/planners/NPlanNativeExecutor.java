package cz.dd4j.agents.heroes.planners;

import cz.dd4j.agents.heroes.pddl.PDDLAction;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 21-Jun-17.
 */
@AutoConfig
public class NPlanNativeExecutor extends AbstractPlannerExecutor {

    @Configurable
    protected File nplanDir = new File("./nplan/");

    protected File agentWorkingDir;
    protected File nplanWorkingDir;

    protected File nplanWorkingFile;

    public String getPddlNewLine() {
        return Const.NEW_LINE_LINUX;
    }

    @Override
    public List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException {

        File resultFile = new File(nplanWorkingDir, "plan.SOL");

        FileUtils.copyFile(domainFile, new File(nplanWorkingDir, "domain.pddl"));
        FileUtils.copyFile(problemFile, new File(nplanWorkingDir, "problem.pddl"));

        Map<String, String> config = new HashMap<String, String>();
        config.put("domain", "domain.pddl");
        config.put("problem", "problem.pddl");
        config.put("result", resultFile.getCanonicalPath());

        CommandLine commandLine = new CommandLine(new File(nplanDir, "nplan").getCanonicalPath());
        commandLine.addArgument("${domain}");
        commandLine.addArgument("${problem}");
        commandLine.addArgument("-o");
        commandLine.addArgument("${result}");
        commandLine.addArgument("-Q");
        commandLine.setSubstitutionMap(config);

        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(nplanWorkingDir);
        executor.setExitValue(0);

        // SYNC EXECUTION
        try {
            executor.execute(commandLine);
        } catch (ExecuteException e) {
            // FAILED TO EXECUTE THE PLANNER
            // => cannot be distinguished from "no plan exists"
            if (e.getExitValue() == 139) { //segfault in planner, seems to mean the plan does not exist
                String path = nplanWorkingDir.getCanonicalPath();
                File crashDir = new File("nplanSegfault/" + path.substring(path.length() - 40, path.length()));
                FileUtils.copyDirectory(nplanWorkingDir, crashDir);
                return null;

            }

            String path = nplanWorkingDir.getCanonicalPath();
            File crashDir = new File("nplanCrashes/" + path.substring(path.length() - 40, path.length()));
            FileUtils.copyDirectory(nplanWorkingDir, crashDir);
            System.err.print(crashDir.getCanonicalPath());
            e.printStackTrace();
            return null;
        }

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
//        try {
//            FileUtils.copyDirectory(nplanDir, nplanWorkingDir);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to copy nplan directory from '" + nplanDir.getAbsolutePath() + "' into '" + nplanWorkingFile.getAbsolutePath() + "'.", e);
//        }
//        nplanWorkingFile.deleteOnExit();
    }
}
