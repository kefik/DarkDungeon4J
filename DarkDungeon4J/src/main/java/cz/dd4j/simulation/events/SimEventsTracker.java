package cz.dd4j.simulation.events;

import java.util.ArrayList;
import java.util.List;

import cz.dd4j.agents.commands.Command;
import cz.dd4j.simulation.data.dungeon.Element;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.result.SimResult;

public class SimEventsTracker {
	
	public class SimEventsHandlers {
		
		public void addHandler(ISimEvents handler) {
			handlers.add(handler);
		}
		
		public boolean isRegistered(ISimEvents handler) {
			return handlers.contains(handler);
		}
		
		public void removeHandler(ISimEvents handler) {
			handlers.remove(handler);
		}
		
	}

	protected class SimEventsNotifier implements ISimEvents {

		@Override
		public void simulationBegin(SimState state) {
			for (ISimEvents handler : handlers) {
				handler.simulationBegin(state);
			}
		}

		@Override
		public void simulationFrameBegin(long frameNumber, long simMillis) {
			for (ISimEvents handler : handlers) {
				handler.simulationFrameBegin(frameNumber, simMillis);
			}
		}
		
		@Override
		public void actionSelected(Element who, Command what) {
			for (ISimEvents handler : handlers) {
				handler.actionSelected(who, what);
			}
		}
		
		@Override
		public void actionStarted(Element who, Command what) {
			for (ISimEvents handler : handlers) {
				handler.actionStarted(who, what);
			}
		}

		@Override
		public void actionEnded(Element who, Command what) {
			for (ISimEvents handler : handlers) {
				handler.actionEnded(who, what);
			}
		}
		
		@Override
		public void actionInvalid(Element who, Command what) {
			for (ISimEvents handler : handlers) {
				handler.actionInvalid(who, what);
			}
		}

		@Override
		public void elementCreated(Element element) {
			for (ISimEvents handler : handlers) {
				handler.elementCreated(element);
			}
		}

		@Override
		public void elementDead(Element element) {
			for (ISimEvents handler : handlers) {
				handler.elementDead(element);
			}
		}

		@Override
		public void simulationFrameEnd(long frameNumber) {
			for (ISimEvents handler : handlers) {
				handler.simulationFrameEnd(frameNumber);
			}
		}

		@Override
		public void simulationEnd(SimResult result) {
			for (ISimEvents handler : handlers) {
				handler.simulationEnd(result);
			}
		}
		
	}
	
	protected List<ISimEvents> handlers     = new ArrayList<ISimEvents>();
	
	protected SimEventsHandlers registrator = new SimEventsHandlers();
	
	protected SimEventsNotifier notifier    = new SimEventsNotifier();
	
	/**
	 * Facade used by objects outside simulator to register their handler. 
	 * @return
	 */
	public SimEventsHandlers handlers() {
		return registrator;
	}
	
	/**
	 * Facade used by simulation to trigger events.
	 * @return
	 */
	public ISimEvents event() {
		return notifier;
	}

}
