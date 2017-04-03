package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.AsEnumObject;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class ELabel extends EElement {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	@AsEnumObject(childClass=ERoomLabel.class)
	public static final ELabel ROOM_LABEL = new ELabel(1);
	
	// =================
	// ENUM CONSTRUCTORS
	// =================

	protected ELabel(int id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(ELabel.class);
	}
}
