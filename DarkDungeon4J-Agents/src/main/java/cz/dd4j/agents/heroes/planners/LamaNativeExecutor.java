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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 21-Jun-17.
 */
@AutoConfig
public class LamaNativeExecutor extends AbstractPlannerExecutor {

    @Configurable
    protected File fdDir = new File("./fast-downward/");

    protected File agentWorkingDir;
    protected File lamaWorkingDir;

    public String getPddlNewLine() {
        return Const.NEW_LINE_LINUX;
    }

    @Override
    public List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException {
        //python fast-downward.py --alias lama-first --plan-file plan.SOL ../DarkDungeon-alt.pddl ../problem.pddl

        File resultFile = new File(lamaWorkingDir, "plan.sol");

        FileUtils.copyFile(domainFile, new File(lamaWorkingDir, "domain.pddl"));
        FileUtils.copyFile(problemFile, new File(lamaWorkingDir, "problem.pddl"));

        Map<String, String> config = new HashMap<String, String>();
        config.put("domain", "domain.pddl");
        config.put("problem", "problem.pddl");
        config.put("result", "plan.sol");

        CommandLine commandLine = new CommandLine("bash");
        commandLine.addArgument(new File(fdDir, "lama.sh").getCanonicalPath());
        commandLine.addArgument("${domain}");
        commandLine.addArgument("${problem}");
        commandLine.addArgument("${result}");
        commandLine.setSubstitutionMap(config);

        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(lamaWorkingDir);
        executor.setExitValue(0);

        // SYNC EXECUTION
        try {
            executor.execute(commandLine);
        } catch (ExecuteException e) {
            // FAILED TO EXECUTE THE PLANNER
            // => cannot be distinguished from "no plan exists"
//            String path = lamaWorkingDir.getCanonicalPath();
//            File crashDir = new File("lamaCrashes/" + path.substring(path.length() - 40, path.length()));
//            FileUtils.copyDirectory(lamaWorkingDir, crashDir);
//            System.err.print(crashDir.getCanonicalPath());
//            e.printStackTrace();
            return null;
        }

        // NOW RESULT FILE SHOULD BE READY
        if (!resultFile.exists()) {
            // TODO: logging
            // nplan failed to produce results
            System.out.println("plan not found");
            return null;
        }

        // PROCESS RESULT
        String resultLines = FileUtils.readFileToString(resultFile);

        // add step numbers, lama does not include them
        String[] lines = resultLines.split("[\n\r]+");
        StringBuilder resLines = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].contains(";"))
                resLines.append(i).append(" : ").append(lines[i]).append("\n");
        }

        resultLines = resLines.toString();

        //rename both moves to "move"
        resultLines = resultLines.replaceAll("move[12]", "move");

        System.out.println("PLAN");
        System.out.println(resultLines);

        FileUtils.deleteQuietly(new File(lamaWorkingDir, "output.sas"));
        resultFile.delete();

        // PARSE LINES AS PDDL ACTION
        return parseLines(resultLines);
    }

    @Override
    public void prepareEnvironment(File agentWorkingDir) {
        this.agentWorkingDir = agentWorkingDir;
        lamaWorkingDir = new File(this.agentWorkingDir, "fd");
        lamaWorkingDir.mkdirs();
        lamaWorkingDir.deleteOnExit();

        try {
            File prepareFile = new File(fdDir, "prepare.sh");
            FileUtils.copyFile(prepareFile, new File(lamaWorkingDir, "prepare.sh"));

            CommandLine commandLine = new CommandLine("bash");
            commandLine.addArgument("prepare.sh");
            commandLine.addArgument(fdDir.getAbsolutePath());

            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(lamaWorkingDir);
            executor.execute(commandLine);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
