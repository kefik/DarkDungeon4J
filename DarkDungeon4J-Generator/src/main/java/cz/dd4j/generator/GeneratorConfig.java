package cz.dd4j.generator;

import java.io.File;
import java.util.logging.Logger;

public class GeneratorConfig {

	public File targetDir;
	
	public String filePrefix;
	
	public Logger log;
	
	/**
	 * Result File("targetDir/dirSuffix/filePrefix-fileName")
	 * @param dirSuffix
	 * @param fileName
	 * @return
	 */
	public File getTargetFile(String dirSuffix, String fileName) {
		return new File(targetDir.getAbsolutePath() + "/" + dirSuffix + "/" + (filePrefix == null ? "" : filePrefix + "-") + fileName);
	}
	
	public void assign(GeneratorConfig from) {
		if (from.targetDir != null) targetDir = from.targetDir;
		if (from.filePrefix != null) filePrefix = from.filePrefix;
		if (from.log != null) log = from.log;
	}
	
}
