package cz.dd4j.utils.config;

import java.io.File;

public class DirLocation {

	public File dir;
	
	public String filePrefix;
	
	public DirLocation() {
		dir = new File("./");
	}
	
	public DirLocation(File dir) {
		this.dir = dir;
	}
	
	public void ensureDir() {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Failed to create folder: " + dir.getAbsolutePath());
			}
		}
		if (dir.isDirectory()) return;		
		throw new RuntimeException("Failed to create folder, there is already non-dir file at: " + dir.getAbsolutePath());
	}
	
	/**
	 * Result File("folder/filePrefix-fileName")
	 * @param extraDir
	 * @param fileName
	 * @return
	 */
	public File getFile(String fileName) {
		return getFile(null, fileName);
	}
	
	/**
	 * Result File("targetDir/extraDir/filePrefix-fileName")
	 * @param extraDir
	 * @param fileName
	 * @return
	 */
	public File getFile(String extraDir, String fileName) {
		return new File(dir.getAbsolutePath() + "/" + (extraDir == null ? "" : extraDir + "/") + (filePrefix == null ? "" : filePrefix + "-") + fileName);
	}
	
	/**
	 * Result File("targetDir/dir")
	 * @param dir
	 * @return
	 */
	public File getDir(String dir) {
		return new File(this.dir.getAbsolutePath() + "/" + dir);
	}
	
}

