package cz.dd4j.loader.dungeon.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("corridor")
public class CorridorXML {

	@XStreamAsAttribute
	public int room1Id;
	
	@XStreamAsAttribute
	public int room2Id;
	
	public String toString() {
		return "CorridorXML[room1Id=" + room1Id + ",room2Id=" + room2Id + "]";
	}
	
}
