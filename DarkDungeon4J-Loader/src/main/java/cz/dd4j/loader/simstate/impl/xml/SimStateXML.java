package cz.dd4j.loader.simstate.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("simstate")
public class SimStateXML {

	@XStreamImplicit(itemFieldName="dungeon")
	public List<FileXML> dungeons;
	
	@XStreamImplicit(itemFieldName="monsters")
	public List<FileXML> monsters;
	
}
