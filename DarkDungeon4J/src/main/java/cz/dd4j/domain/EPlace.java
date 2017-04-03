package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EPlace extends EElement {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EPlace ROOM     = new EPlace(1);
	public static final EPlace CORRIDOR = new EPlace(2);
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EPlace(int id) {
		super(id);
	}

	static {
		Enums.getInstance().registerEnumClass(EPlace.class);
	}
	
}
