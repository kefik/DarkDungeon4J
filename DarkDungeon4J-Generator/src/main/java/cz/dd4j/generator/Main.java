package cz.dd4j.generator;

import java.io.File;

import cz.cuni.amis.utils.simple_logging.SimpleLogging;

public class Main {

	public static void generateRooms(int from, int to) {
		
	}
	
	public static void main(String[] args) {
		SimpleLogging.initLogging();
		
		GeneratorConfig config = new GeneratorConfig();
		config.targetDir = new File("result");
		
		Generator generator = new Generator(config);
		
		//generator.generateRooms(4, 100);
		
		//generator.generateGrid(4, 100);
			
		//generator.generateSphere(4, 100);
		
		// Mazes 5x5 -> 10x10, 5 random mazes per dimension, 0-3 extra junctions (0 == no circles in the graph)
		generator.generateMazes(5, 5, 10, 10, 10, 5);
		
	}
	
	
	
	
}
