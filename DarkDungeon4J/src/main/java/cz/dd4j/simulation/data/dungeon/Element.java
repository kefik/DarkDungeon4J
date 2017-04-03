package cz.dd4j.simulation.data.dungeon;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.domain.EElement;

public class Element {

	public int id;
	
	/**
	 * Human-readable name of the entity; w/o id, that can be appended if required.
	 */
	public String name;
	
	/**
	 * Enum-instance that describes the type of the element.
	 */
	public final EElement type;
	
	public Element(EElement type) {
		this.type = type;
	}
	
	/**
	 * Is this element instance of 'type' ?
	 * @param type
	 * @return
	 */
	public boolean isA(EElement type) {
		return EH.isA(this.type, type);
	}
	
	/**
	 * Is this element instance of 'cls'.
	 * @param cls
	 * @return
	 */
	public boolean isOf(Class cls) {
		return cls.isAssignableFrom(getClass());
	}
	
	/**
	 * Does the type of element belongs to the instances of types determined by 'typeClass'?
	 * @param typeClass
	 * @return
	 */
	public boolean isKindOf(Class typeClass) {
		return typeClass.isAssignableFrom(type.getClass());
	}
	
	/**
	 * Human-readable name+id of the element.
	 * @return
	 */
	public String getDescription() {
		if (this == null) return "Element[in-construction]";
		if (name == null) return getClass().getSimpleName() + "-" + id;
		return name + "-" + id;				
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
}
