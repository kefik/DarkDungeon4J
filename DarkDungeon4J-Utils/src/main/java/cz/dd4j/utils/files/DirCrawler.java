package cz.dd4j.utils.files;

import java.io.File;

public class DirCrawler {
	
	public static void crawl(File dir, IDirCrawlerCallback callback) {
		if (callback == null) return;		
		if (dir == null) return;
		
		callback.start(dir);
		
		if (!dir.isDirectory()) {
			callback.visitFile(dir);
			callback.end();
			return;
		}
		
		visitDir(dir, callback);
		
		callback.end();		
	}

	private static void visitDir(File dir, IDirCrawlerCallback callback) {
		if (!callback.visitDir(dir)) return;		
		for (File child : dir.listFiles()) {
			if (child.isDirectory()) {
				visitDir(child, callback);
			} else {
				callback.visitFile(child);
			}
		}
	}

}
