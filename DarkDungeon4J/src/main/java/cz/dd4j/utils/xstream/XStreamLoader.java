package cz.dd4j.utils.xstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

public abstract class XStreamLoader<RESULT> {

	protected XStream xstream;
	
	protected Class<RESULT> classToLoad;
	
	public XStreamLoader(Class<RESULT> classToLoad) {
		this.classToLoad = classToLoad;
		xstream = init();		
	}
	
	protected XStream init() {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.alias(classToLoad.getAnnotation(XStreamAlias.class).value(), classToLoad);
		return xstream;

	}
	public RESULT load(File xmlFile) {
		if (xmlFile == null) {
			throw new IllegalArgumentException("'xmlFile' can't be null!");
		}
		try {
			return load(new FileInputStream(xmlFile));
		} catch (Exception e) {
			throw new RuntimeException("Could not load file: " + xmlFile.getAbsolutePath(), e);
		}
	}
	
	public RESULT load(InputStream xmlStream) {
		if (xmlStream == null) {
			throw new IllegalArgumentException("'xmlStream' can't be null!");
		}		
		
		Object obj = xstream.fromXML(xmlStream);
		try {
			xmlStream.close();
		} catch (IOException e) {
		}
		if (obj == null || !classToLoad.isAssignableFrom(obj.getClass())) {
			throw new RuntimeException("Stream didn't contain an xml with " + classToLoad.getSimpleName());
		}
		
		@SuppressWarnings("unchecked")
		RESULT result = (RESULT)obj;
		
		postprocess(result);
		
		return result;
	}
	
	protected void postprocess(RESULT result) {
	}
	
}
