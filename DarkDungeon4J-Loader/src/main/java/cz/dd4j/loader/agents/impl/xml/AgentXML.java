package cz.dd4j.loader.agents.impl.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.utils.Id;
import cz.dd4j.utils.config.ConfigXML;

@XStreamAlias("agent")
public class AgentXML {
	
	/**
	 * Parameter-less constructor required for XStream deserialization
	 */
	public AgentXML() {		
	}
	
	/**
	 * Used for generator.
	 * @param fqcn
	 * @param configs
	 */
	public AgentXML(String name, String fqcn, ConfigXML... configs) {
		this.name = name;
		this.agentFQCN = fqcn;
		if (configs != null && configs.length > 0) {
			config = new ArrayList<ConfigXML>(configs.length);
			for (ConfigXML config : configs) {
				this.config.add(config);
			}
		}
	}	
	
	@XStreamAsAttribute
	public Id id;
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAlias("agentFQCN")
	public String agentFQCN;
	
	@XStreamImplicit(itemFieldName="config")
	public List<ConfigXML> config;
	
	@Override
	public String toString() {
		return "AgentXML[id=" + id + ",name=" + name + ",agentFQCN=" + agentFQCN + ",#config=" + (config == null ? "null" : config.size()) + "]";
	}

}
