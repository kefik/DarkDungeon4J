package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EFeature extends EEntity {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EFeature TRAP = new EFeature("EFTrap");
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EFeature(String id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(EFeature.class);
	}
	
}
