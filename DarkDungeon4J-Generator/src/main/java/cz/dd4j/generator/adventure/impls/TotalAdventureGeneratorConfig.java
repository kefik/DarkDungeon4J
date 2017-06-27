package cz.dd4j.generator.adventure.impls;

import cz.dd4j.generator.adventure.Range;

public class TotalAdventureGeneratorConfig extends AdventureGeneratorConfigBase {

	public Range monsters = new Range(1,1);
	public boolean monstersOfTheSameType = true;
	
	public Range items = new Range(1,1);
	
	public Range traps = new Range(1,1);
	
}
