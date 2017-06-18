package cz.dd4j.loader.simstate.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("file")
public class FileXML {
	
	@XStreamAsAttribute
	public String path;
	
	@Override
	public String toString() {
		return "FileXML[path=" + path + "]";
	}

}
