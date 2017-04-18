package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.AsEnumObject;
import cz.cuni.amis.utils.eh4j.EnumObject;
import cz.cuni.amis.utils.eh4j.Enums;
import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.utils.Id;

@AsEnumClass
public class EElement {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	@AsEnumObject(childClass=EPlace.class)
	public static final EElement PLACE = new EElement(1);
	@AsEnumObject(childClass=EEntity.class)
	public static final EElement ENTITY = new EElement(2);
	@AsEnumObject(childClass=EItem.class)
	public static final EElement ITEM = new EElement(3);
	@AsEnumObject(childClass=ELabel.class)
	public static final EElement LABEL = new EElement(4);
	
	// nada for now

	// ==============
	// ENUM VARIABLES
	// ==============
	
	public final Id id;
	
	// =================
	// ENUM CONSTRUCTORS
	// =================

	protected EElement(String id) {
		this.id = Id.get(id);
	}
	
	protected EElement(int id) {
		this.id = Id.get(id);
	}
	
	protected EElement(Id id) {
		this.id = id;
	}
	
	static {
		Enums.getInstance().registerEnumClass(EElement.class);
	}
	
	@Override
	public String toString() {
		EnumObject enumObject = EH.getEnumObject(this);
		if (enumObject == null) return "EElement";
		return enumObject.type.getName() + "." + enumObject.name;
	}
	
}
