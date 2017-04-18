package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cz.dd4j.utils.Id;

@XStreamAlias("corridor")
public class CorridorXML {

	@XStreamAsAttribute
	public Id room1Id;
	
	@XStreamAsAttribute
	public Id room2Id;
	
	@XStreamAsAttribute
	public String note;
	
	public String toString() {
		return "CorridorXML[room1Id=" + room1Id + ",room2Id=" + room2Id + ",note='" + note + "']";
	}
	
}
