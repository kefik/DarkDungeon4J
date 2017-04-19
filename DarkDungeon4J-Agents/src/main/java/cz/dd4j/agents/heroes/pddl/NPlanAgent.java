package cz.dd4j.agents.heroes.pddl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;

import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

@AutoConfig
public class NPlanAgent extends PDDLAgentBase {

	@Configurable
	protected File nplanFile = new File("./nplan/nplan");
	
	protected File nplanWorkingDir;
	
	protected File nplanWorkingFile;
	
	@Override
	public void prepareAgent() {
		super.prepareAgent();
		
		nplanWorkingDir = getWorkingFile("nplan");
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
	
	@Override
	protected List<PDDLAction> execPlanner(File domainFile, File problemFile) throws Exception {
		// ./nplan domain.pddl problem.pddl -o plan.SOL -Q
		// SEE: https://www.overleaf.com/9055139bwywfzfxppbk
		
		File resultFile = new File(nplanWorkingDir, "plan.SOL");
		
		Map<String, String> config = new HashMap<String, String>();
		config.put("domain", domainFile.getAbsolutePath());
		config.put("problem", problemFile.getAbsolutePath());
		
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

}
