package cz.dd4j.ui.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

public class VisFile implements ISimEvents {

	public final static String WHO_SIMULATOR = "Simulator";
	
	protected File targetFile;
	protected boolean zip;
	
	protected OutputStream fileOut;
	protected PrintStream out;
	
	protected long frameNumber;
	
	protected int frameLength = 3;
	protected int whoLength = 9;
	protected int whatLength = 15;
	
	public String outputPrefix = "";	
	
	/**
	 * @param pathToTargetFile file where to output the log, if the file ends with ".zip" it will automatically produce a zip file
	 */
	public VisFile(String pathToTargetFile) {
		this(new File(pathToTargetFile), pathToTargetFile.toLowerCase().endsWith(".zip"));
	}
	
	/**
	 * @param targetFile
	 * @param zip if true, this will produce a zip file instead of 'txt'
	 */
	public VisFile(File targetFile, boolean zip) {
		if (targetFile.exists() && !targetFile.isFile()) {
			throw new RuntimeException("Cannot overwrite (as it is not a file): " + targetFile.getAbsolutePath());
		}
		this.targetFile = targetFile;
		this.zip = zip;
	}
	
	public VisFile(PrintStream out) {
		this.out = out;
	}
	
	private void log(String who, String what, String description) {
		if (frameNumber > Math.pow(10, frameLength)) frameLength = (int)(Math.ceil(Math.log10(frameNumber)));
		if (who.length() > whoLength) whoLength = who.length();
		if (what.length() > whatLength) whatLength = what.length();
		out.printf(outputPrefix + "[%" + frameLength + "d] {%" + whoLength + "s} (%" + whatLength + "s) %s", frameNumber, who, what, description);
		out.println();
		out.flush();
	}
	
	@Override
	public void simulationBegin(SimState state, SimStaticStats stats) {
		try {
			try {
				new File(targetFile.getAbsolutePath()).getParentFile().mkdirs();
			} catch (Exception e) {				
			}
			if (targetFile.exists()) targetFile.delete();
			if (zip) {
				fileOut = new ZipOutputStream(new FileOutputStream(targetFile));	 
				ZipEntry e;
				if (targetFile.getName().endsWith(".zip")) e = new ZipEntry(targetFile.getName().substring(0, targetFile.getName().length()-4) + ".txt");
				else e = new ZipEntry(targetFile.getName() + ".txt");
				((ZipOutputStream)fileOut).putNextEntry(e);
				
			} else {
				fileOut = new FileOutputStream(targetFile);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to open file '" + targetFile.getAbsolutePath() + "' for writing.", e);
		}
		out = new PrintStream(fileOut);
		
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
		try {
			out.flush();
		} catch (Exception e) {			
		}
		if (zip) {
			try {
				((ZipOutputStream)fileOut).closeEntry();
			} catch (IOException e) {				
			}
		}
		try {
			out.close();
		} catch (Exception e) {			
		}
		try {
			fileOut.close();
		} catch (Exception e) {
		}
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
