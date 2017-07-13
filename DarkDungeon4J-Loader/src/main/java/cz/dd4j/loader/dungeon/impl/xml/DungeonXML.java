package cz.dd4j.loader.dungeon.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.domain.EDungeonLabel;

@XStreamAlias("dungeon")
public class DungeonXML {

	@XStreamImplicit(itemFieldName="dungeonLabel")
	public List<EDungeonLabel> labels;
	
	@XStreamImplicit(itemFieldName="room")
	public List<RoomXML> rooms;
	
	@XStreamImplicit(itemFieldName="corridor")
	public List<CorridorXML> corridors;
	
}
