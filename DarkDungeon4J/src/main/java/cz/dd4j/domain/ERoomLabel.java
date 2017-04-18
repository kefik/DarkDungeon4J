package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class ERoomLabel extends ELabel {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final ERoomLabel GOAL = new ERoomLabel("ERLGoal");
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected ERoomLabel(String id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(ERoomLabel.class);
	}
	
}
