package cz.dd4j.domain;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;

@AsEnumClass
public class EDungeonLabel extends ELabel {

	// ==========================================
	// KEYS FOR VALUES FOR EDugneonLabel.TOPOLOGY
	// ==========================================
	
	public static final String TOPOLOGY_TYPE = "type";
	public static final String TOPOLOGY_ROOMS_WIDTH = "rooms-width";
	public static final String TOPOLOGY_ROOMS_HEIGHT = "rooms-height";
	public static final String TOPOLOGY_ROOMS_COUNT = "rooms-count";
	
	// ==============
	// ENUM INSTANCES
	// ==============
	
	public static final EDungeonLabel TOPOLOGY = new EDungeonLabel("EDLTopology");
	
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
