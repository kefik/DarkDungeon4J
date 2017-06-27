package cz.dd4j.utils.config;

import java.util.logging.Logger;

public class GenericConfig {

	public Logger log;
	
	public DirLocation target = new DirLocation();
	
	public void assign(GenericConfig from) {
		if (from == null) return;
		if (from.target != null) {
			if (from.target.dir != null) target.dir = from.target.dir;
			if (from.target.filePrefix != null) target.filePrefix = from.target.filePrefix;
		}
		if (from.log != null) log = from.log;
	}
	
}
