package cz.dd4j.ui.gui;

import java.io.PrintStream;

import cz.cuni.amis.clear2d.Clear2D;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.agents.commands.Command;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EPlace;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.entities.features.Trap;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.events.ISimEvents;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.utils.BusyWait;
import cz.dd4j.ui.gui.utils.IWaiting;
import cz.dd4j.ui.gui.view.HeroView;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomView;
import cz.dd4j.ui.gui.view.TrapView;
import cz.dd4j.ui.log.VisFile;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.ExceptionToString;

public class VisGUI implements ISimEvents {

	public final String WHO_SIMULATOR = VisConsole.WHO_SIMULATOR;
	
	protected final PrintStream out;
	
	protected long frameNumber;
	
	protected int frameLength = 3;
	protected int whoLength = 9;
	protected int whatLength = 15;
	
	public String outputPrefix = "";
	
	protected DD4JVis vis;
	
	protected boolean autoCloseOnSimulationEnd = true;
	
	/**
	 * Auto-close visualization frame after the simulation ends.
	 */
	public VisGUI() {
		this(true);
	}
	
	/**
	 * Whether to automatically close the visualization frame when the simulation ends.
	 * @param autoCloseOnSimulationEnd
	 */
	public VisGUI(boolean autoCloseOnSimulationEnd) {
		//out = System.out;
		out = null;
		this.autoCloseOnSimulationEnd = true;
	}
	
	public VisGUI(PrintStream out) {
		this.out = out;
	}
	
	protected void log(String who, String what, String description) {
		if (frameNumber > Math.pow(10, frameLength)) frameLength = (int)(Math.ceil(Math.log10(frameNumber)));
		if (who.length() > whoLength) whoLength = who.length();
		if (what.length() > whatLength) whatLength = what.length();
		String msg = String.format(outputPrefix + "[%" + frameLength + "d] {%" + whoLength + "s} (%" + whatLength + "s) %s", frameNumber, who, what, description);
		if (out != null) {			
			out.printf(msg);
			out.println();
		}
		vis.frame.log(msg);
	}
	
	protected void initFrame(SimState state, SimStaticStats stats) {
		if (Ctx.engine == null) {
			Ctx.init();
		}
		
		if (vis != null) {
			vis.die();
		}
		vis = new DD4JVis(state, stats);
		vis.frame.setVisible(true);
	}

	
	@Override
	public void simulationBegin(SimState state, SimStaticStats stats) {
		frameNumber = 0;
		
		initFrame(state, stats);
		
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
	public void actionEnded(final Element who, final Command what) {
		actionPerforming("ACTION-ENDED", who, what);
		
		if (what.isType(EAction.MOVE)) {
			if (who.isA(EEntity.HERO)) {
				final Hero heroBody = vis.simState.heroes.values().iterator().next().body;
				final HeroView heroView = vis.heroViews.get(heroBody);
				
				if (what.target.isA(EPlace.CORRIDOR)) {
					// MOVING TO CORRIDOR
					new BusyWait(new IWaiting() {
						@Override
						public boolean isWaiting() {
							return heroView.movementCtrl.currentCorridor != what.target;
						}
						
					}).busyWait();
				} else
				if (what.target.isA(EPlace.PLACE)) {
					// MOVING TO CORRIDOR
					new BusyWait(new IWaiting() {
						@Override
						public boolean isWaiting() {
							return heroView.movementCtrl.currentRoom != what.target && heroView.movementCtrl.currentCorridor != heroBody.atCorridor;
						}
						
					}).busyWait();
				}
			} else
			if (who.isA(EEntity.MONSTER)) {
				final Monster monsterBody = vis.simState.monsters.values().iterator().next().body;
				final MonsterView monsterView = vis.monsterViews.get(monsterBody);
				
				if (what.target.isA(EPlace.CORRIDOR)) {
					// MOVING TO CORRIDOR
					new BusyWait(new IWaiting() {
						@Override
						public boolean isWaiting() {
							return monsterView.movementCtrl.currentCorridor != what.target;
						}
						
					}).busyWait();;
				} else
				if (what.target.isA(EPlace.PLACE)) {
					// MOVING TO CORRIDOR
					new BusyWait(new IWaiting() {
						@Override
						public boolean isWaiting() {
							return monsterView.movementCtrl.currentRoom != what.target && monsterView.movementCtrl.currentCorridor != monsterBody.atCorridor;
						}
						
					}).busyWait();;
				}
			}			
		} else
		if (what.isType(EAction.ATTACK)) {
			if (who.isA(EEntity.HERO)) {
				final Hero heroBody = vis.simState.heroes.values().iterator().next().body;
				final HeroView heroView = vis.heroViews.get(heroBody);
				final Monster monsterBody = vis.simState.monsters.get(what.target.id).body;  
				final MonsterView monsterView = vis.monsterViews.get(monsterBody);
				
				final TweenPos tween = heroView.attack(monsterView);
				// BUSY WAITING
				new BusyWait( new IWaiting() {
						@Override
						public boolean isWaiting() { return tween.isTweening(); } 
					}
				).busyWait();
			} else
			if (who.isA(EEntity.MONSTER)) {
				final Hero heroBody = vis.simState.heroes.values().iterator().next().body;
				final HeroView heroView = vis.heroViews.get(heroBody);
				final Monster monsterBody = vis.simState.monsters.get(who.id).body;  
				final MonsterView monsterView = vis.monsterViews.get(monsterBody);
				
				final TweenPos tween = monsterView.attack(heroView);
				// BUSY WAITING
				new BusyWait( new IWaiting() {
						@Override
						public boolean isWaiting() { return tween.isTweening(); } 
					}
				).busyWait();
			} else
			if (who.isA(EFeature.TRAP)) {
				final Trap trapBody = (Trap)(vis.simState.features.get(who.id).body);
				final RoomView roomView = vis.roomsView.getRoomView(trapBody.atRoom);
				final TrapView trapView = roomView.trap;		
				
				if (what.target.isA(EEntity.HERO)) {
					final Hero heroBody = vis.simState.heroes.values().iterator().next().body;
					final HeroView heroView = vis.heroViews.get(heroBody);
					
					final TweenPos tween = trapView.attack(heroView);
					// BUSY WAITING
					new BusyWait( new IWaiting() {
							@Override
							public boolean isWaiting() { return tween.isTweening(); } 
						}
					).busyWait();
					
				} else
				if (what.target.isA(EEntity.MONSTER)) {
					final Monster monsterBody = vis.simState.monsters.get(what.target.id).body;  
					final MonsterView monsterView = vis.monsterViews.get(monsterBody);
					
					final TweenPos tween = trapView.attack(monsterView);
					// BUSY WAITING
					new BusyWait( new IWaiting() {
							@Override
							public boolean isWaiting() { return tween.isTweening(); } 
						}
					).busyWait();
				}
			}
			
		} else 
		if (what.isType(EAction.DROP)) {
			final Hero heroBody = vis.simState.heroes.values().iterator().next().body;			
			final HeroView heroView = vis.heroViews.get(heroBody);
			final RoomView roomView = vis.roomsView.getRoomView(heroBody.atRoom);
			
			final TweenPos tween = heroView.dropSword(roomView);
			
			// BUSY WAITING
			new BusyWait( new IWaiting() {
					@Override
					public boolean isWaiting() { return tween.isTweening(); } 
				}
			).busyWait();
		}
		else 
		if (what.isType(EAction.PICKUP)) {
			final Hero heroBody = vis.simState.heroes.values().iterator().next().body;			
			final HeroView heroView = vis.heroViews.get(heroBody);
			final RoomView roomView = vis.roomsView.getRoomView(heroBody.atRoom);
			
			final TweenPos tween = heroView.pickupSword(roomView);
			
			// BUSY WAITING
			new BusyWait( new IWaiting() {
					@Override
					public boolean isWaiting() { return tween.isTweening(); } 
				}
			).busyWait();
			
		}
		else 
		if (what.isType(EAction.DISARM)) {
			final Hero heroBody = vis.simState.heroes.values().iterator().next().body;			
			final HeroView heroView = vis.heroViews.get(heroBody);
			final RoomView roomView = vis.roomsView.getRoomView(heroBody.atRoom);
			
			final TweenPos tween = heroView.attackTrap(roomView);
			
			// BUSY WAITING
			new BusyWait( new IWaiting() {
					@Override
					public boolean isWaiting() { return tween.isTweening(); } 
				}
			).busyWait();
		}
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
		
		if (autoCloseOnSimulationEnd) {
			vis.die();
			Clear2D.engine.stop();
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
