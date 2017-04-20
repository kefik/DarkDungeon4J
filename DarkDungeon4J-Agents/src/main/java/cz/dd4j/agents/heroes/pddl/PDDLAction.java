package cz.dd4j.agents.heroes.pddl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.utils.Id;


public class PDDLAction {
	
	public EAction action;
	
	public Id arg1;
	
	public Id arg2;
	
	// ==============
	// STATIC PARSING
	// ==============
	
	/**
	 * 0 : (pickup_sword r1)
	 * 1 : (move r1 r2)
	 * 2 : (kill r2)
	 * 3 : (drop_sword r2)
	 * 4 : (move r2 r3)
	 * 5 : (disarm r3)
	 * 6 : (move r3 r4)
	 *
	 * @author Jimmy
	 */
	public static PDDLAction parseSOL(String line) {
		PDDLAction result = new PDDLAction();
		
		String[] parts = line.split(" ");
		
		if (parts.length > 2) {
			result.action = getActionType(parts[2].substring(1));
		}
		if (parts.length > 3) {
			if (parts[3].endsWith(")")) parts[3] = parts[3].substring(0, parts[3].length()-1);			
			result.arg1 = Id.get(parts[3]);
		}
		if (parts.length > 4) {
			if (parts[4].endsWith(")")) parts[4] = parts[4].substring(0, parts[4].length()-1);			
			result.arg2 = Id.get(parts[4]);
		}
				
		if (result.action == null) {
			return null;
		}
		
		return result;
	}

	private static EAction getActionType(String group) {
		group = group.toLowerCase();
		
		if (group.equals("pickup_sword")) return EAction.PICKUP;
		if (group.equals("move")) return EAction.MOVE;
		if (group.equals("kill")) return EAction.ATTACK;
		if (group.equals("drop_sword")) return EAction.DROP;
		if (group.equals("disarm")) return EAction.DISARM;
		
		return null;
	}

}
