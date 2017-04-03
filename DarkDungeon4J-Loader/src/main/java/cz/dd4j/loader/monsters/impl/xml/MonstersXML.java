package cz.dd4j.loader.monsters.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("monsters")
public class MonstersXML {
	
	@XStreamImplicit(itemFieldName="monster")
	public List<MonsterXML> monsters;

}
