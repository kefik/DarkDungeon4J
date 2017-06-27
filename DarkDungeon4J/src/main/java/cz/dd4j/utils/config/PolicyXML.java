package cz.dd4j.utils.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("policy")
public class PolicyXML {

	@XStreamImplicit(itemFieldName="config")
	public List<ConfigXML> values;
	
}
