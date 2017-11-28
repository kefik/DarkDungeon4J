package cz.dd4j.loader.dungeon.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import cz.dd4j.domain.EDungeonLabel;
import cz.dd4j.utils.config.ConfigMap;
import cz.dd4j.utils.config.ConfigXML;

@XStreamAlias("dungeon")
public class DungeonXML {

	/**
	 * When configuring, use {@link EDungeonLabel#id} as identifiers.
	 */
	@XStreamImplicit(itemFieldName="label")
	public List<ConfigXML> labels;
	
	@XStreamImplicit(itemFieldName="room")
	public List<RoomXML> rooms;
	
	@XStreamImplicit(itemFieldName="corridor")
	public List<CorridorXML> corridors;
	
}
