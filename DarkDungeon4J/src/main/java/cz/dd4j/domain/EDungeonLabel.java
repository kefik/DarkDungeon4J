package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EDungeonLabel extends ELabel {

	// ==============
	// ENUM INSTANCES
	// ==============
	
	/**
	 * Values are of a string type.
	 */
	public static final EDungeonLabel TOPOLOGY_TYPE = new EDungeonLabel("TOPOLOGY_TYPE");
	
	public static final String TOPOLOGY_TYPE_VALUE_GRID = "Grid";
	public static final String TOPOLOGY_TYPE_VALUE_TORUS = "Torus";
	
	/**
	 * Values are of an int type.
	 */
	public static final EDungeonLabel TOPOLOGY_ROOMS_WIDTH = new EDungeonLabel("TOPOLOGY_ROOMS_WIDTH");
	
	/**
	 * Values are of an int type.
	 */
	public static final EDungeonLabel TOPOLOGY_ROOMS_HEIGHT = new EDungeonLabel("TOPOLOGY_ROOMS_HEIGHT");
	
	/**
	 * Values are of an int type.\
	 */
	public static final EDungeonLabel TOPOLOGY_ROOMS_COUNT = new EDungeonLabel("TOPOLOGY_ROOMS_COUNT");
	
	// =================
	// ENUM CONSTRUCTORS
	// =================
	
	protected EDungeonLabel(String id) {
		super(id);
	}
	
	static {
		Enums.getInstance().registerEnumClass(EDungeonLabel.class);
	}
	
}
