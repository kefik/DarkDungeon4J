package cz.dd4j.agents.heroes.planners;

import cz.dd4j.agents.heroes.pddl.PDDLAction;
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
public class NPlanCygwinExecutor extends AbstractPlannerExecutor {

    @Configurable
    protected File nplanFolder = new File("./nplan");

    @Configurable
    protected String nplanBatchFile = "nplan.bat";

    File problemFile;

    File nplanWorkingDir;
    File domainFile;


    @Override
    public List<PDDLAction> execPlanner(File domainFile, File problemFile) throws IOException {
        // ./nplan.bat domain.pddl problem.pddl plan.SOL

        File resultFile = new File(nplanWorkingDir, "plan.SOL");

        FileUtils.copyFile(domainFile, new File(nplanWorkingDir, "domain.pddl"));
        FileUtils.copyFile(problemFile, new File(nplanWorkingDir, "problem.pddl"));

        Map<String, String> config = new HashMap<String, String>();
        config.put("domain", "domain.pddl");
        config.put("problem", "problem.pddl");
        config.put("result", "plan.SOL");

        CommandLine commandLine = new CommandLine("cmd.exe");
        commandLine.addArgument("/C");
        commandLine.addArgument(nplanBatchFile);
        commandLine.addArgument("${domain}");
        commandLine.addArgument("${problem}");
        commandLine.addArgument("${result}");
        commandLine.setSubstitutionMap(config);

        final Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(nplanWorkingDir);
        executor.setExitValue(0);

        // SYNC EXECUTION
        try {
            executor.execute(commandLine);
        } catch (Exception e) {
            // FAILED TO EXECUTE THE PLANNER
            // => cannot be distinguished from "no plan exists"
            return null;
        }

        // NOW RESULT FILE SHOULD BE READY
        if (!resultFile.exists()) {
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

        // ALTER TARGET FOR PROBLEM FILE GENERATION
        problemFile = new File(agentWorkingDir, "nplan/problem.pddl");

        // MAKE WORKING DIRECTORY
        nplanWorkingDir = new File(agentWorkingDir, "nplan");
        nplanWorkingDir.mkdirs();
        nplanWorkingDir.deleteOnExit();

        // COPY NPLAN
        copyNPlanFolder(nplanFolder, nplanWorkingDir);

        // COPY DOMAIN FILE
//        File nplanDomainFile = new File(agentWorkingDir,"nplan/domain.pddl");
//        try {
//            FileUtils.copyFile(domainFile, nplanDomainFile);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to copy domain file from '" + domainFile.getAbsolutePath() + "' into '" + nplanDomainFile.getAbsolutePath() + "'.", e);
//        }
    }

    private void copyNPlanFolder(File nplanFolder, File nplanWorkingDir) {
        try {
            for (File file : nplanFolder.listFiles()) {
                if (file.isFile()) {
                    FileUtils.copyFileToDirectory(file, nplanWorkingDir);
                } else
                if (file.isDirectory()) {
                    FileUtils.copyDirectoryToDirectory(file, nplanWorkingDir);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy directory '" + nplanFolder.getAbsolutePath() + "' into '" + nplanWorkingDir.getAbsolutePath() + "'.", e);
        }
    }
}
