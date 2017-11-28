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
	public static final ELabel ROOM_LABEL = new ELabel("ELRoomLabel");
	
	@AsEnumObject(childClass=EDungeonLabel.class)
	public static final ELabel DUNGEON_LABEL = new ELabel("ELDungeonLabel");
	
	// =================
	// ENUM CONSTRUCTORS
	// =================

	protected ELabel(String id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(ELabel.class);
	}
}
