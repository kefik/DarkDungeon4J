package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EItem extends EElement {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EItem SWORD = new EItem(1);
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EItem(int id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(EItem.class);
	}

}
