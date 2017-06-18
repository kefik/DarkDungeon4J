package cz.dd4j.utils.files;

import java.io.File;

public abstract class DirCrawlerCallback implements IDirCrawlerCallback {
	
	protected File root; 
	
	public void start(File root) {
		this.root = root;
	}
	
	public boolean visitDir(File dir) {
		return true;
	}
	
	public abstract void visitFile(File file);
	
	public void end() {			
	}
	
}
