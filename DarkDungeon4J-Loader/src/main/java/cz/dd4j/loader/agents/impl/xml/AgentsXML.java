package cz.dd4j.loader.agents.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("agents")
public class AgentsXML {
	
	@XStreamImplicit(itemFieldName="agent")
	public List<AgentXML> agents;

}
