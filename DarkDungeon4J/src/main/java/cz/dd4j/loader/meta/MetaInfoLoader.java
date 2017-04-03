package cz.dd4j.loader.meta;

import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cz.dd4j.utils.xstream.XStreamLoader;

public class MetaInfoLoader extends XStreamLoader<MetaInfo> {

	private static Object mutex = new Object();
	
	private static MetaInfoLoader loader;
	
	public static MetaInfoLoader getInstance() {
		if (loader != null) return loader;
		synchronized(mutex) {
			if (loader != null) return loader;
			return loader = new MetaInfoLoader();
		}
	}
	
	private MetaInfoLoader() {
		super(MetaInfo.class);
	}
	
	/**
	 * Loads the ".meta" file for 'xmlFile'.
	 * Example:      xmlFile == "myDir/somefile.xml"
	 * Loaded file:             "myDir/somefile.xml.meta"
	 * 
	 * @param xmlFile
	 * @return MetaInfo or throws RuntimeException
	 */
	public MetaInfo loadMetaFor(File xmlFile) {
		return load(new File(xmlFile.getAbsolutePath() + ".meta"));
	}

}
