package cz.dd4j.loader.heroes.impl.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("heroes")
public class HeroesXML {
	
	@XStreamImplicit(itemFieldName="hero")
	public List<HeroXML> heroes;

}
