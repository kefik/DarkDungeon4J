package cz.dd4j.simulation.data.agents.actions;

public enum EAction {

	// todo: prioritizovat akce
	
	// clash akci == 
	
	//NONE(0), // REPRESENTED AS NULL
	MOVE(1, true, false),
	EQUIP(2, false, true),	
	ATTACK(3, true, false),
	DISARM(4, true, false),
	PICKUP(5, true, false);
	
	// MOSNTER utoci jen kdyz hero nema mec...
	
	public static final int LAST_ID = 5;
	
	public final int id;
	
	public final int priority;
	
	public final boolean requiresUsing;
	
	public final boolean requiresTarget;
	
	private EAction(int id, boolean requiresTarget,  boolean requiresUsing) {
		this(id, id, requiresTarget, requiresUsing);
	}
	
	private EAction(int id, int priority, boolean requiresTarget,  boolean requiresUsing) {
		this.id = id;
		this.priority = priority;
		this.requiresTarget = requiresTarget;
		this.requiresUsing = requiresUsing;		
	}
	
}
