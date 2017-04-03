package cz.dd4j.simulation.config;

public enum ObserveMode {
	
	/**
	 * Partially observable mode; hero will receive information only about the
	 * room it is in + adjecent corridors and rooms.
	 */
	PARTIAL,
	
	/**
	 * Fully observable mode; hero will receive complete information about the dungeon.
	 */
	FULL

}
