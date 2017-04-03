package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cz.dd4j.domain.ERoomLabel;

@XStreamAlias("room")
public class RoomXML {

	@XStreamAsAttribute
	public int id;
	
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
