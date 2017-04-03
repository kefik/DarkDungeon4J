package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cz.dd4j.domain.EItem;

@XStreamAlias("item")
public class ItemXML {
	
	@XStreamAsAttribute
	public int id;
	
	@XStreamAsAttribute
	public String name;

	@XStreamAsAttribute
	public EItem type;
	
}
