package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.AsEnumObject;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EEntity extends EElement {	
	
	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EEntity HERO    = new EEntity(1);
	public static final EEntity MONSTER = new EEntity(2);
	@AsEnumObject(childClass=EFeature.class)
	public static final EEntity FEATURE = new EEntity(3);
	
	public static final int LAST_ID = 3;
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EEntity(int id) {
		super(id);
	}
	
	
	static {
		Enums.getInstance().registerEnumClass(EEntity.class);
	}

	
	
}
