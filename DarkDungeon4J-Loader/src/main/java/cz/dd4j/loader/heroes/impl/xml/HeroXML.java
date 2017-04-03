package cz.dd4j.loader.heroes.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.utils.config.ConfigXML;

@XStreamAlias("hero")
public class HeroXML {
	
	@XStreamAsAttribute
	public int id;
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAlias("heroFQCN")
	public String heroFQCN;
	
	@XStreamImplicit(itemFieldName="config")
	public List<ConfigXML> config;

}
