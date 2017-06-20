package cz.dd4j.generator.adventure;

import cz.dd4j.generator.adventure.AdventureGenerator.AdventureContext;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;

public interface IAdventureCallback {

	/**
	 * @param ctx context of the adventure generator, contains details about what we have generated into 'adventure'
	 * @param adventure generated adventure
	 * @param generator
	 * @param config the same as generator.config
	 */
	public void process(AdventureContext ctx, SimStateXML adventure, AdventureGenerator generator, AdventureGeneratorConfig config);
	
}
