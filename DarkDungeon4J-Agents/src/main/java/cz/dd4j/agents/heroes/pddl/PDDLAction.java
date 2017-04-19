package cz.dd4j.agents.heroes.pddl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.agents.commands.CommandParam;
import cz.dd4j.simulation.actions.EAction;
import cz.dd4j.utils.Id;

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
public class PDDLAction {
	
	public EAction action;
	
	public Id arg1;
	
	public Id arg2;
	
	// ==============
	// STATIC PARSING
	// ==============
	
	private static Pattern actionPattern = Pattern.compile("[^(]*\\(\\s*(\\w*)\\s*(\\w*)?\\s*(\\w*)?\\s*\\)\\s*");
	
	public static PDDLAction parse(String line) {
		PDDLAction result = new PDDLAction();
		
		Matcher matcher = actionPattern.matcher(line);
		if (matcher.find()) {			
			if (matcher.groupCount() > 1) result.action = getActionType(matcher.group(1));
			if (matcher.groupCount() > 2) result.arg1 = Id.get(matcher.group(2));
			if (matcher.groupCount() > 3) result.arg2 = Id.get(matcher.group(3));
		}
		
		if (result.action == null) {
			throw new RuntimeException("Cannot parse PDDLAction from string: " + line);
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
