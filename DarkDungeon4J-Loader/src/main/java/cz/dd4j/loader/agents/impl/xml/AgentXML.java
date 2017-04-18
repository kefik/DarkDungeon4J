package cz.dd4j.loader.agents.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.ConfigXML;

@XStreamAlias("agent")
public class AgentXML {
	
	@XStreamAsAttribute
	public Id id;
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAlias("agentFQCN")
	public String agentFQCN;
	
	@XStreamImplicit(itemFieldName="config")
	public List<ConfigXML> config;

}
