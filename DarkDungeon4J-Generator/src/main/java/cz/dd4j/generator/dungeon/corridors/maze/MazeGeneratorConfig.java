package cz.dd4j.generator.dungeon.corridors.maze;

import cz.dd4j.generator.GeneratorConfig;

/**
 * All numbers are 1-based, min 3x3!
 * @author Jimmy
 */
public class MazeGeneratorConfig extends GeneratorConfig {
	
	public int xFrom;
	public int xTo;
	
	public int yFrom;
	public int yTo;
	
	public int numberMazesPerDimension;
	
	public int maxExtraJunctions = 3;
	
	public MazeGeneratorConfig(int xFrom, int xTo, int yFrom, int yTo, int numberMazesPerDimension2, int maxExtraJunctions) {
		this.xFrom = xFrom;
		this.xTo = xTo;
		this.yFrom = yFrom;
		this.yTo = yTo;
		if (xFrom < 3) throw new RuntimeException("xFrom = " + xFrom + " < 3, invalid!");
		if (yFrom < 3) throw new RuntimeException("yFrom = " + yFrom + " < 3, invalid!");
		if (xTo < 3) throw new RuntimeException("xTo = " + xTo + " < 3, invalid!");
		if (yTo < 3) throw new RuntimeException("yTo = " + yTo + " < 3, invalid!");
		if (xFrom > xTo) throw new RuntimeException("xFrom = " + xFrom + " > " + xTo + " = xTo, invalid!");
		if (yFrom > yTo) throw new RuntimeException("yFrom = " + yFrom + " > " + yTo + " = yTo, invalid!");
		this.numberMazesPerDimension = numberMazesPerDimension2;
		if (numberMazesPerDimension <= 0) throw new RuntimeException("numberMazesPerDimension = " + numberMazesPerDimension + " <= 0, invalid!");
		this.maxExtraJunctions = maxExtraJunctions;
		if (this.maxExtraJunctions < 0) this.maxExtraJunctions = 0;
	}
	
	
	
}
