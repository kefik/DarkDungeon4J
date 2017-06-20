package cz.dd4j.generator.adventure;

import cz.dd4j.generator.adventure.AdventureGenerator.AdventureContext;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;

public interface IAdventureFilter {
	
	public boolean isAccepted(AdventureContext ctx, SimStateXML state, AdventureGeneratorConfig config);

}
