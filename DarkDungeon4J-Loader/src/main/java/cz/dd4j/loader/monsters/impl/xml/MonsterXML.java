package cz.dd4j.loader.monsters.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.utils.config.ConfigXML;

@XStreamAlias("monster")
public class MonsterXML {
	
	@XStreamAsAttribute
	public int id;
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAlias("monsterFQCN")
	public String monsterFQCN;
	
	@XStreamImplicit(itemFieldName="config")
	public List<ConfigXML> config;

}
