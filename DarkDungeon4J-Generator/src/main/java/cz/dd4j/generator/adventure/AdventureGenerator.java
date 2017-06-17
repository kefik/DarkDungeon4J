package cz.dd4j.generator.adventure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;

public class AdventureGenerator extends GeneratorBase<AdventureGeneratorConfig>{

	private static final Pattern ROOMS_PATTERN = Pattern.compile("Requires: Rooms([0-9]*)\\.xml"); 
	
	private int adventureNumber;
	
	private String roomsFile;
	
	private String heroesFile;
	
	private String goalsFile;
	
	private String corridorsFile;
	
	private Set<Integer> roomsTaken = new HashSet<Integer>();
	
	
	public AdventureGenerator(AdventureGeneratorConfig config) {
		super(SimStateXML.class, config);
	}
	
	private void reset() {
		adventureNumber = 0;
		roomsFile = null;
		heroesFile = null;
		goalsFile = null;
		corridorsFile = null;		
		roomsTaken.clear();
	}

	@Override
	public void generate() {
		reset();
		
		File corridorsDir = config.getTargetDir(config.corridorsDir);
		
		for (File corridorsFile : corridorsDir.listFiles()) {
			if (!corridorsFile.getName().endsWith(".xml")) continue;
			generateForCorridors(corridorsFile);
		}
		
		
	}

	private void generateForCorridors(File corridorsFile) {
		// SETUP TARGET CORRIDORS FILE
		this.corridorsFile = config.corridorsDir + "/" + corridorsFile.getName();
		
		// SETUP TARGET ROOMS FILE
		int rooms = readRequiredRooms(corridorsFile);		
		this.roomsFile = config.roomsDir + "/Rooms" + rooms + ".xml";
		
		// SETUP TARGET HEROES FILE
		this.heroesFile = config.heroesDir + "/Hero1-Room1.xml";
				
		// SETUP TARGET GOALS FILE
		this.goalsFile = config.goalsDir + "/Goal-Room" + rooms + ".xml";
		
		// GENERATE ALL POSSIBILITIES
		roomsTaken.add(1);
		roomsTaken.add(rooms);
	}

	private int readRequiredRooms(File corridorsFile) {
		FileInputStream stream = null;
		BufferedReader reader = null;
		try {
			stream = new FileInputStream(corridorsFile);
			reader = new BufferedReader(new InputStreamReader(stream));
			while (reader.ready()) {
				String line = reader.readLine();
				Matcher matcher = ROOMS_PATTERN.matcher(line);
				if (matcher.find()) {
					return Integer.parseInt(matcher.group(1));
				}
			}
		} catch (Exception e) {	
			throw new RuntimeException("Failed to open file: " + corridorsFile.getAbsolutePath());
		} finally {
			if (reader != null) {				
				try { reader.close(); } catch (Exception e) {}
			}
			if (stream != null) {				
				try { stream.close(); } catch (Exception e) {}
			}
		}
				
		throw new RuntimeException("Failed to find the required number of rooms in file: " + corridorsFile.getAbsolutePath());
	}
	
}
