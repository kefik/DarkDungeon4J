package cz.dd4j.utils.files;

import java.io.File;

public interface IDirCrawlerCallback {
	
	public void start(File root);

	public boolean visitDir(File dir);

	public void visitFile(File file);

	public void end();
	
}
