package cz.dd4j.descriptor;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.generator.adventure.impls.AdventureGeneratorBase;
import cz.dd4j.loader.simstate.impl.xml.FileXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateLoaderXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.utils.astar.IAStarHeuristic;
import cz.dd4j.utils.collection.IntMap;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class AdventureDescriptor extends DungeonDescriptor {

	public static final Pattern MONSTER_TYPE_PATTERN = Pattern.compile(AdventureGeneratorBase.MONSTER_AGENT_TYPE_PATTERN_STR);
	
	public static final Pattern TRAP_TYPE_PATTERN = Pattern.compile(AdventureGeneratorBase.TRAP_AGENT_TYPE_PATTERN_STR);
	
	public static final double[] DANGER_DENSITIES = new double[]{ 0.0d, 0.01d, 0.05d, 0.1d, 0.15d, 0.2d, 0.25d, 0.3d, 0.35d, 0.4d, 0.45d, 0.5d, 0.55d, 0.65d, 0.7d, 0.75d, 0.8d, 0.85d, 0.95d, 0.99d, 1.0d };
	
	public static NumberFormat DANGER_DENSITY_FORMAT;
	
	static {
		DANGER_DENSITY_FORMAT = NumberFormat.getInstance(Locale.US);
		DANGER_DENSITY_FORMAT.setMaximumFractionDigits(2);
	}
	
	public int dangersCount;
	
	/**
	 * Rooms / dangersCount
	 * It is always some number from {@link #DANGER_DENSITIES} !
	 */
	public double dangersDensity;
	
	/**
	 * Key: monster type (read from file name)
	 * Value: number of monsters of such a type 
	 */
	public IntMap<String> monstersCountByType;
	
	/**
	 * Key: trap type (read from file name)
	 * Value: number of traps of such a type
	 */
	public IntMap<String> trapsCountByType;
	
	public static AdventureDescriptor describe(File xmlSimStateFile) {
		return describe(xmlSimStateFile, DungeonPaths.ASTAR_NO_HEURISTIC);
	}
	
	public static AdventureDescriptor describe(File xmlSimStateFile, IAStarHeuristic<Room> heuristic) {
		SimStateLoaderXML loader = new SimStateLoaderXML();
		SimStateXML simStateXML = loader.load(xmlSimStateFile);
		
		SimState simState = loader.loadSimState(xmlSimStateFile.getParentFile(), simStateXML, true);		
		
		AdventureDescriptor result = new AdventureDescriptor();
		DungeonDescriptor.describe(simState.dungeon, heuristic, result);
		
		// AGENT TYPE COUNTS
		result.monstersCountByType = new IntMap<String>();
		result.trapsCountByType = new IntMap<String>();
				
		for (FileXML agentFile : simStateXML.agents) {
			File file = new File(xmlSimStateFile.getParentFile(), agentFile.path);
			
			Matcher matcher;
			
			matcher = MONSTER_TYPE_PATTERN.matcher(file.getName().toLowerCase());
			if (matcher.find()) {
				// MONSTER!
				String type = matcher.group(1);
				result.monstersCountByType.inc(type);
				continue;
			} 
			
			matcher = TRAP_TYPE_PATTERN.matcher(file.getName().toLowerCase());
			if (matcher.find()) {
				// TRAP!
				String type = matcher.group(1);
				result.trapsCountByType.inc(type);				
			}
		}
		
		// TOTAL DANGERS COUNT
		result.dangersCount = result.monstersCountByType.sum() + result.trapsCountByType.sum();
		
		// FIND MATCHING DANGER DENSITY
		result.dangersDensity = findMatchingDangerDensity(result.dangersCount, result.dangersDensity, result.roomsCount);
		
		return result;
	}
	
	/**
	 * We're using "Math.floor" when determining the count of dangers out of "density"... that's why we search for the first "greater" density and return the previous one
	 * if we do not find an exact match...
	 * @param value
	 * @return
	 */
	private static double findMatchingDangerDensity(int dangerCount, double dangerDensity, int roomsCount) {
		for (int index = 0; index < DANGER_DENSITIES.length; ++index) {
			int targetCount = (int)Math.floor(DANGER_DENSITIES[index] * roomsCount);
			if (targetCount == dangerCount) {
				return DANGER_DENSITIES[index]; 
			}
		}
		
		for (int index = 0; index < DANGER_DENSITIES.length; ++index) {
			if (dangerDensity < DANGER_DENSITIES[index]) {
				if (index == 1 && dangerDensity > 0) return DANGER_DENSITIES[1];
				return DANGER_DENSITIES[index - 1];
			}
		}
		
		return 1;			
	}

	// =========
	// REPORTING
	// =========
	
	public static final String CSV_DANGERS_COUNT = "DESC-dangers-count";
	
	public static final String CSV_DANGERS_DENSITY = "DESC-dangers-density";
	
	public static final String CSV_MONSTER_TYPES = "DESC-monster-types";
	
	public static final String CSV_MONSTER_TYPE_PREFIX = "DESC-monster-"; 
	
	public static final String CSV_TRAP_TYPES = "DESC-trap-types";
	
	public static final String CSV_TRAP_TYPE_PREFIX = "DESC-trap-";
	
	@Override
	public List<String> getCSVHeaders() {
				
		List<String> result = super.getCSVHeaders();
		
		result.add(CSV_DANGERS_COUNT);
		
		result.add(CSV_DANGERS_DENSITY);
		
		result.add(CSV_MONSTER_TYPES);
		
		List<String> types;
		
		types = new ArrayList<String>(monstersCountByType.keySet());
		Collections.sort(types);		
		for (String type : types) {
			result.add(CSV_MONSTER_TYPE_PREFIX+type+"-count");
		}
				
		result.add(CSV_TRAP_TYPES);
		
		types = new ArrayList<String>(trapsCountByType.keySet());
		Collections.sort(types);		
		for (String type : types) {
			result.add(CSV_TRAP_TYPE_PREFIX+type+"-count");
		}
		
		return result;
	}

	@Override
	public CSVRow getCSVRow() {
		CSVRow result = super.getCSVRow();
		
		result.add(CSV_DANGERS_COUNT, dangersCount);
		
		result.add(CSV_DANGERS_DENSITY, DANGER_DENSITY_FORMAT.format(dangersDensity));
		
		List<String> types;
		
		types = new ArrayList<String>(monstersCountByType.keySet());
		Collections.sort(types);		
		result.add(CSV_MONSTER_TYPES, concat(types));
		for (String type : types) {
			result.add(CSV_MONSTER_TYPE_PREFIX+type+"-count", monstersCountByType.get(type));
		}
		
		
		types = new ArrayList<String>(trapsCountByType.keySet());
		Collections.sort(types);		
		result.add(CSV_TRAP_TYPES, concat(types));
		for (String type : types) {
			result.add(CSV_TRAP_TYPE_PREFIX+type+"-count", trapsCountByType.get(type));
		}
		
		return result;
	}
	
	private String concat(Collection<String> values) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String value : values) {
			if (first) first = false;
			else sb.append(",");
			sb.append(value);
		}
		return sb.toString();
	}
	
}
