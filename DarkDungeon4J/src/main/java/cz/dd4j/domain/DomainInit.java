package cz.dd4j.domain;

public class DomainInit {

	/**
	 * Forces initialization of ENUMs
	 */
	public static void init() {
	}
	
	private static void init(EElement dummy) {		
	}
	
	static {
		init(EElement.ENTITY);
		init(EEntity.HERO);
		init(EFeature.TRAP);
		init(EItem.SWORD);
		init(ELabel.ROOM_LABEL);
		init(ERoomLabel.GOAL);
	}
	
}
