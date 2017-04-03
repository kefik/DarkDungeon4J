package cz.dd4j.simulation.data.dungeon.elements.items;

import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.data.dungeon.Element;

public class Item extends Element {

	public Item(EItem type) {
		super(type);
	}
	
	@Override
	public String getDescription() {
		if (name != null) return super.getDescription();
		return EH.getEnumObject(type).name + "-" + id;
	}
		
}
