package cz.dd4j.agents.heroes.pddl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.dd4j.agents.heroes.planners.NPlanCygwinExecutor;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.io.FileUtils;

import cz.dd4j.utils.Const;
import cz.dd4j.utils.config.AutoConfig;
import cz.dd4j.utils.config.Configurable;

@AutoConfig
public class NPlanCygwinAgent extends PDDLAgentBase {

	@Configurable
	protected File nplanFolder = new File("./nplan");
	
	@Configurable
	protected String nplanBatchFile = "nplan.bat";
	
	// =======
	// RUNTIME
	// =======
	
	protected File nplanWorkingDir;

	@Override
	public void prepareAgent() {
		super.prepareAgent();

		executor = new NPlanCygwinExecutor();
		executor.prepareEnvironment(agentWorkingDir);
		inputGenerator.setPddlNewLine(executor.getPddlNewLine());
		
		// ALTER TARGET FOR PROBLEM FILE GENERATION
		problemFile = getWorkingFile("nplan/problem.pddl");
		
		// MAKE WORKING DIRECTORY
		nplanWorkingDir = getWorkingFile("nplan");
		nplanWorkingDir.mkdirs();
		nplanWorkingDir.deleteOnExit();
		
		// COPY NPLAN
		copyNPlanFolder(nplanFolder, nplanWorkingDir);
		
		// COPY DOMAIN FILE
		File nplanDomainFile = getWorkingFile("nplan/domain.pddl");
		try {
			FileUtils.copyFile(domainFile, nplanDomainFile);
		} catch (IOException e) {
			throw new RuntimeException("Failed to copy domain file from '" + domainFile.getAbsolutePath() + "' into '" + nplanDomainFile.getAbsolutePath() + "'.", e);
		}
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
	
	@Override
	public void simulationEnded() {
		super.simulationEnded();
		FileUtils.deleteQuietly(nplanWorkingDir);
	}

}
