package cz.dd4j.loader.dungeon.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("hero")
public class HeroXML {
	
	@XStreamAsAttribute
	public int id;
	
	@XStreamAsAttribute
	public String name;

	@XStreamImplicit(itemFieldName="item")
	public List<ItemXML> inventory;
	
	@XStreamAlias(value="hand")
	public ItemXML hand;
}
