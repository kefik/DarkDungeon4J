package cz.dd4j.agents.replay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.dd4j.agents.IAgent;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.events.ISimEvents;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.ui.log.VisFile;
import cz.dd4j.utils.Id;

public class Replay implements ISimEvents {
	
	private static final Pattern ACTION_SELECTED_PATTERN = Pattern.compile("ACTION-SELECTED\\) Action\\[([^,]*),who=([^.]*),target=(.*)\\]");

	private File replayFile;
	private boolean zip;
	private SimState simState;
	
	/**
	 * Mock agents for the {@link #simState}, contains all three kinds of entities: {@link IFeatureAgent}, {@link IHeroAgent} and {@link IMonsterAgent}.
	 */
	public Map<Id, IAgent> replayAgents = new HashMap<Id, IAgent>();
	
	/**
	 * {@link IHeroAgent} subset of {@link #replayAgents}.
	 */
	public Agents<IHeroAgent> replayHeroAgents = new Agents<IHeroAgent>();
	
	/**
	 * Id -&gt; Item map, serves for quick lookup of the item id.
	 */
	public Map<Id, Item> itemCatalogue = new HashMap<Id, Item>();
	
	// =================
	// SIM RUNTIME STUFF
	// =================
	
	private InputStream replayInput;
	private InputStreamReader replayStreamReader;
	private BufferedReader replayReader;
	private int replayLine = 0;
	private int frameNumber = 0;
	private Map<Id, Command> agentActions = new HashMap<Id, Command>();

	/**
	 * @param simState which simState the replay is for
	 * @param replayFile replay file containing actions of agents (as produced by {@link VisFile}); if ends with ".zip" the "ZIP compression" is automatically assumed + we will use the first zip entry as "replay file" assuming it is a text file, this is compatible with {@link VisFile}
	 */
	public Replay(SimState simState, String replayFile) {
		this(simState, new File(replayFile), replayFile.toLowerCase().endsWith(".zip"));
	}
	
	/**
	 * @param simState which simState the replay is for
	 * @param replayFile replay file containing actions of agents (as produced by {@link VisFile})
	 * @param zip whether the file is using zip compression
	 */
	public Replay(SimState simState, File replayFile, boolean zip) {
		this.simState = simState;
		this.replayFile = replayFile;
		this.zip = zip;
		
		initReplayAgents();
	}
	
	private void initReplayAgents() {
		// CREATE FEATURE AGENTS
		for (Id id : simState.features.keySet()) {
			replayAgents.put(id, new FeatureAgentReplay(this));
		}
		// CREATE MONSTER AGENTS
		for (Id id : simState.monsters.keySet()) {
			replayAgents.put(id, new MonsterAgentReplay(this));
		}
		// CREATE HERO AGENTS
		for (Id id : simState.heroes.keySet()) {
			IHeroAgent heroAgent = new HeroAgentReplay(this);
			replayAgents.put(id, heroAgent);
			replayHeroAgents.agents.put(id, heroAgent);
		}
		
		// INJECT INTO 'simState' + INITIALIZE itemCatalogue
		for (Room room : simState.dungeon.rooms.values()) {
			if (room.monster != null) {
				simState.monsters.put(room.monster.id, new AgentMindBody<Monster, IMonsterAgent>(room.monster, (IMonsterAgent)replayAgents.get(room.monster.id)));
			}
			if (room.feature != null) {				
				simState.features.put(room.feature.id, new AgentMindBody(room.feature, (IFeatureAgent)replayAgents.get(room.feature.id)));
			}
			if (room.item != null) {
				itemCatalogue.put(room.item.id, room.item);
			}
		}
	}

	// ==============
	// REPLAY RUNTIME
	// ==============
	
	public Command getAgentAction(Id agentId) {
		return agentActions.get(agentId);
	}

	@Override
	public void simulationBegin(SimState state, SimStaticStats stats) {
		// OPEN THE FILE FOR READING
		try {
			replayInput = new FileInputStream(replayFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not open replay file at: " + replayFile.getAbsolutePath(), e);
		}
		
		if (zip) {
			replayInput = new ZipInputStream(replayInput);
			try {
				ZipEntry entry = ((ZipInputStream)replayInput).getNextEntry();
			} catch (IOException e) {
				throw new RuntimeException("Did not find a file within the zip file at: " + replayFile.getAbsolutePath(), e);
			}
		}
		
		replayStreamReader = new InputStreamReader(replayInput);
		replayReader = new BufferedReader(replayStreamReader);
		frameNumber = 0;
		replayLine = 0;
	}

	@Override
	public void simulationFrameBegin(SimState state, SimStaticStats stats) {
		++this.frameNumber;
		
		// PARSE ACTIONS FOR AGENTS
		agentActions.clear();
		
		String line;
		
		try {
			line = replayReader.readLine();
			++replayLine;
		} catch (IOException e) {
			throw new RuntimeException("Could not read another line of simulation frame: " + frameNumber + ", from the file: " + replayFile.getAbsolutePath() + " line: " + replayLine, e);
		}
		
		while (!line.contains("Simulation frame " + frameNumber + " ended.")) {
			Matcher m = ACTION_SELECTED_PATTERN.matcher(line);
			if (m.find()) {
				// GET MATCHER GROUPS
				String actionStr = m.group(1);
				String whoStr = m.group(2);
				String targetStr = m.group(3);
				
				// PARSE ACTION TYPE
				EAction actionType = EAction.valueOf(actionStr);
				
				// PARSE WHO-ID
				String whoIdStr = whoStr.split("-")[1];
				Id whoId = Id.get(whoIdStr);
				
				// PARSE TARGET ID
				String targetIdStr;
				if (targetStr.startsWith("Room[")) {
					targetIdStr = targetStr.split("=")[1];
					targetIdStr = targetIdStr.substring(0, targetIdStr.length()-1);
				} else {
					targetIdStr = targetStr.split("-")[1];
				}
				Id targetId = Id.get(targetIdStr);
				
				// DETERMINE TARGET ELEMENT
				Element target;
				target = simState.dungeon.rooms.get(targetId);
				if (target == null) {
					target = simState.features.containsKey(targetId) ? simState.features.get(targetId).body : null;
					if (target == null) { 
						target = simState.monsters.containsKey(targetId) ? simState.monsters.get(targetId).body : null;
						if (target == null) { 
							target = simState.heroes.containsKey(targetId) ? simState.heroes.get(targetId).body : null;
							if (target == null) target = itemCatalogue.get(targetId);
						}
					}
				}
				if (target == null) {
					throw new RuntimeException("Failed to resolve element id: " + targetIdStr + " neither a room, nor a feature, nor a monster, nor a hero, nor an item ??? File: " + replayFile.getAbsolutePath() + ", line: " + replayLine);
				}	
				
				// WE HAVE THE ACTION, STORE IT FOR THE NEXT USE
				Command command = new Command(actionType, target);
				agentActions.put(whoId, command);
			}
			
			try {
				line = replayReader.readLine();
				++replayLine;
			} catch (IOException e) {
				throw new RuntimeException("Could not read another line of simulation frame: " + frameNumber + ", from the file: " + replayFile.getAbsolutePath() + " line: " + replayLine, e);
			}
		}
		
	}

	@Override
	public void actionSelected(Element who, Command what) {
	}

	@Override
	public void actionStarted(Element who, Command what) {
	}

	@Override
	public void actionEnded(Element who, Command what) {
	}

	@Override
	public void actionInvalid(Element who, Command what) {
	}

	@Override
	public void elementCreated(Element element) {
	}

	@Override
	public void elementDead(Element element) {
	}

	@Override
	public void simulationFrameEnd(SimStaticStats stats) {
	}

	@Override
	public void simulationEnd(SimResult result, SimStaticStats stats) {
		// CLOSE STUFF
		try {
			replayReader.close();
		} catch (Exception e) {
		}	
		try {
			replayStreamReader.close();
		} catch (Exception e) {
		}
		try {
			replayInput.close();
		} catch (Exception e) {			
		}
	}

	@Override
	public void simulationLog(Element who, Level level, String message) {
	}
	
}
