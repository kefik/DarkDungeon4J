package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EPlace extends EElement {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EPlace ROOM     = new EPlace("EPRoom");
	public static final EPlace CORRIDOR = new EPlace("EPCorridor");
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EPlace(String id) {
		super(id);
	}

	static {
		Enums.getInstance().registerEnumClass(EPlace.class);
	}
	
}
