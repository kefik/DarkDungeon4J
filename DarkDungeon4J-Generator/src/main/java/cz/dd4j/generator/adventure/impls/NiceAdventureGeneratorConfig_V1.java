package cz.dd4j.generator.adventure.impls;

import java.io.File;

import cz.dd4j.agents.monsters.DynamicMonsterAgent;
import cz.dd4j.agents.monsters.KillerMonsterAgent;

public class NiceAdventureGeneratorConfig_V1 extends AdventureGeneratorConfigBase {

	public double[] dangerDensities;
	
	/**
	 * Refers to result/agents/monsters ...
	 * Example:
	 * 		monster10-Killer090.xml
	 * 		is type "Kille090"
	 */
	public String[] monsterTypes;
	
	public double[] trapMonsterRatios;
	
	/**
	 * Minimum is always 1 sword per dungeon.
	 */
	public double[] swordDensities;
	
	public File[] corridorsFiles;
	
	// =====================
	// EXPERIMENTS JUNE 2017
	// =====================
	
	private void setupJune2017_Base() {
		dangerDensities = new double[] { 0.05d, 0.1d, 0.15d, 0.2d, 0.25d, 0.3d, 0.35d, 0.4d, 0.45d, 0.5d };
		
		monsterTypes = new String[]{ "Dynamic000", "Dynamic010", "Dynamic020", "Dynamic050", "Killer010", "Killer020", "Killer050" };
		
		trapMonsterRatios = new double[] { 0.25d, 0.5d, 0.75d };
		
		swordDensities = new double[] { 0.0d, 0.02d, 0.05d }; // 0.0d is for "1", minimum is always 1 ;)
	}
	
	public void setupJune2017_Test() {
		setupJune2017_Base();		
		corridorsFiles = new File[] {
			new File("corridors/grid/Grid25.xml"),
			new File("corridors/torus/Torus25.xml"),
			new File("corridors/maze/Maze-5x5-V0-EJ2.xml"),
		};
		
		target.dir = new File("result/adventures/2017/june/test");
	}
	
	public void setupJune2017_Full() {
		setupJune2017_Base();		
		corridorsFiles = new File[] {
			new File("corridors/grid/Grid36.xml"),
			new File("corridors/grid/Grid100.xml"),
			new File("corridors/torus/Torus36.xml"),
			new File("corridors/torus/Torus100.xml"),
			new File("corridors/maze/Maze-6x6-V0-EJ2.xml"),
			new File("corridors/maze/Maze-6x6-V1-EJ2.xml"),
			new File("corridors/maze/Maze-6x6-V4-EJ2.xml"),
			new File("corridors/maze/Maze-8x8-V1-EJ3.xml"),
			new File("corridors/maze/Maze-8x8-V2-EJ3.xml"),
			new File("corridors/maze/Maze-8x8-V3-EJ3.xml"),
			new File("corridors/maze/Maze-10x10-V0-EJ5.xml"),
			new File("corridors/maze/Maze-10x10-V2-EJ5.xml"),
			new File("corridors/maze/Maze-10x10-V3-EJ5.xml"),
			new File("corridors/maze/Maze-12x12-V1-EJ5.xml"),
			new File("corridors/maze/Maze-12x12-V2-EJ5.xml"),
			new File("corridors/maze/Maze-12x12-V3-EJ5.xml"),
			new File("corridors/maze/Maze-14x14-V1-EJ5.xml"),
			new File("corridors/maze/Maze-14x14-V3-EJ5.xml"),
			new File("corridors/maze/Maze-14x14-V4-EJ5.xml"),
			new File("corridors/maze/Maze-16x16-V4-EJ5.xml")
		};
		
		target.dir = new File("result/adventures/2017/june/full");
	}
	
}
