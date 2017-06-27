package cz.dd4j.ui.console;

import java.io.PrintStream;

import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.events.ISimEvents;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.ExceptionToString;

public class VisConsole implements ISimEvents {

	public final String WHO_SIMULATOR = "Simulator";
	
	protected final PrintStream out;
	
	protected long frameNumber;
	
	protected int frameLength = 3;
	protected int whoLength = 9;
	protected int whatLength = 15;
	
	public String outputPrefix = "";
	
	public VisConsole() {
		out = System.out;
	}
	
	public VisConsole(PrintStream out) {
		this.out = out;
	}
	
	private void log(String who, String what, String description) {
		if (frameNumber > Math.pow(10, frameLength)) frameLength = (int)(Math.ceil(Math.log10(frameNumber)));
		if (who.length() > whoLength) whoLength = who.length();
		if (what.length() > whatLength) whatLength = what.length();
		out.printf(outputPrefix + "[%" + frameLength + "d] {%" + whoLength + "s} (%" + whatLength + "s) %s", frameNumber, who, what, description);
		out.println();
		//out.println("[" + frameNumber + "] {" + who + "} (" + what + ") " + description);
	}
	
	@Override
	public void simulationBegin(SimState state, SimStaticStats stats) {
		frameNumber = 0;
		log(WHO_SIMULATOR, "SimBegin", "Simulation begins.");
		log(WHO_SIMULATOR, "SimBegin", "ID:   " + state.config.id);
		log(WHO_SIMULATOR, "SimBegin", "DESC: " + state.config.description);
	}

	@Override
	public void simulationFrameBegin(SimState state, SimStaticStats stats) {
		this.frameNumber = stats.frameNumber;
		log(WHO_SIMULATOR, "SimFrameBegin", "Simulation frame " + frameNumber + " begun, sim time " + stats.simMillis() + "ms.");
		for (AgentMindBody<Hero, IHeroAgent> agent : state.heroes.values()) {
			if (agent.body.alive) {
				log(WHO_SIMULATOR, "SimFrameBegin", "  +-- HERO    at " + agent.body.atRoom + (agent.body.hand != null ? " with " + agent.body.hand : "") + ": " + agent.mind + ", " + agent.body);
			}
		}
		for (AgentMindBody<Monster, IMonsterAgent> agent : state.monsters.values()) {
			if (agent.body.alive) {
				log(WHO_SIMULATOR, "SimFrameBegin", "  +-- MONSTER at " + agent.body.atRoom + ": " + agent.mind + ", " + agent.body);
			}
		}
		for (AgentMindBody<Feature, IFeatureAgent> agent : state.features.values()) {
			if (agent.body.alive) {
				log(WHO_SIMULATOR, "SimFrameBegin", "  +-- FEATURE at " + agent.body.atRoom + ": " + agent.mind + ", " + agent.body);
			}
		}
	}
	
	public String getName(Element who) {
		String result = who.getDescription();
		if (result != null) return result;
		if (who.name == null) return who.getClass().getSimpleName() + "-" + who.id;
		return who.name + "-" + who.id;
	}
	
	protected void actionPerforming(String state, Element who, Command what) {
		String description = "";
		if (what != null) {
			description += what;
		} else {
			description = "NO-ACTION";
		}
		log(getName(who), state, description);
	}
	
	@Override
	public void actionSelected(Element who, Command what) {
		if (what == null) return;
		actionPerforming("ACTION-SELECTED", who, what);
	}
	
	@Override
	public void actionStarted(Element who, Command what) {
		actionPerforming("ACTION-STARTED", who, what);
	}

	@Override
	public void actionEnded(Element who, Command what) {
		actionPerforming("ACTION-ENDED", who, what);
	}
	
	@Override
	public void actionInvalid(Element who, Command what) {
		actionPerforming("ACTION-INVALID", who, what);
	}

	@Override
	public void elementCreated(Element element) {
		log(getName(element), "CREATED", "");
	}
	
	@Override
	public void elementDead(Element element) {
		log(getName(element), "DESTROYED", "");
	}

	@Override
	public void simulationFrameEnd(SimStaticStats stats) {
		log(WHO_SIMULATOR, "SimFrameEnd", "Simulation frame " + stats.frameNumber + " ended.");
	}

	@Override
	public void simulationEnd(SimResult result, SimStaticStats stats) {
		log(WHO_SIMULATOR, "SimEnd", "Simulation ended in frame " + result.frameNumber + ", time " + result.simTimeMillis + "ms.");
		log(WHO_SIMULATOR, "SimEndResult", getResultDescription(result));
	}

	private String getResultDescription(SimResult result) {
		switch (result.resultType) {
		case AGENT_EXCEPTION: return result.resultType + "[Agent code exception.]" + Const.NEW_LINE + ExceptionToString.process(result.exception);
		case HERO_WIN: return result.resultType + "[" + getName(result.winner.body) + "]";
		case HEROES_LOSE: return result.resultType + "[All heroes are dead.]";
		case SIMULATION_EXCEPTION: return result.resultType + "[Simulation code exception.]";
		default:
			return result.resultType + "[UNKNOWN RESULT]";
		}
	}

	

	

}
