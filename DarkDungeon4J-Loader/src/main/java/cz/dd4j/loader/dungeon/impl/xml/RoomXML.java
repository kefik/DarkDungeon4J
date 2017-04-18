package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cz.dd4j.domain.ERoomLabel;
import cz.dd4j.utils.Id;

@XStreamAlias("room")
public class RoomXML {

	@XStreamAsAttribute
	public Id id;
	
	@XStreamAsAttribute
	public String name;
	
	@XStreamAsAttribute
	public ERoomLabel label;
	
	public MonsterXML monster;
	
	public FeatureXML feature;
	
	public HeroXML hero;
	
	public ItemXML item;
	
	public String toString() {
		return "RoomXML[id=" + id + "]";
	}
	
	
}
