package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.AsEnumObject;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EEntity extends EElement {	
	
	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EEntity HERO    = new EEntity("EEHero", 1);
	public static final EEntity MONSTER = new EEntity("EEMonster", 2);
	@AsEnumObject(childClass=EFeature.class)
	public static final EEntity FEATURE = new EEntity("EEFeature", 3);
	
	public static final int LAST_ID = 3;
	
	public final int entityId;
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EEntity(String id) {
		this(id, -1);
	}
	
	protected EEntity(String id, int entityId) {
		super(id);
		this.entityId = entityId;
	}	
	
	static {
		Enums.getInstance().registerEnumClass(EEntity.class);
	}

	
	
}
