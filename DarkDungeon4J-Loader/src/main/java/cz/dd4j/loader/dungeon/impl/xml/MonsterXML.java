package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cz.dd4j.domain.EEntity;
import cz.dd4j.utils.Id;

@XStreamAlias("monster")
public class MonsterXML {
	
	@XStreamAsAttribute
	public Id id;
	
	@XStreamAsAttribute
	public String name;

	@XStreamAsAttribute
	public EEntity type;
	
}