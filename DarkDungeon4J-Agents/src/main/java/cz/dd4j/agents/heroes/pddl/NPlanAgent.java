package cz.dd4j.agents.heroes.pddl;

import java.io.File;
import java.io.IOException;

import cz.dd4j.agents.heroes.planners.NPlanNativeExecutor;
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
		executor = new NPlanNativeExecutor();
		executor.prepareEnvironment(agentWorkingDir);
		inputGenerator.setPddlNewLine(executor.getPddlNewLine());
	}

}
