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
		
		//generator.generateRooms(4, 16);
		generator.generateGrid(4, 16);
		
	}
	
	
	
	
}
