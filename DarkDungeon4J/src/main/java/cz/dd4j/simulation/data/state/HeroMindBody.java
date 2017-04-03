package cz.dd4j.simulation.data.state;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;

public class HeroMindBody {

	public Hero body;
	public IHeroAgent mind;
	
	@Override
	public String toString() {
		return "HeroMindBody[body=" + body + "]";
	}
	
}
