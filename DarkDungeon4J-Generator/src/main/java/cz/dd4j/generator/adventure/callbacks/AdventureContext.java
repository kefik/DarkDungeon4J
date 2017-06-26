package cz.dd4j.generator.adventure.callbacks;

import java.util.ArrayList;
import java.util.List;

public class AdventureContext {
	
	public int adventureNumber;
	
	public int roomsCount;
	
	public String roomsFile;
	
	public int heroRoom;
	
	public String heroesFile;
	
	public int goalRoom;
	
	public String goalsFile;
	
	public String corridorsFile;
	
	public String resultDirRootRelativePath;
	
	public List<String> allMonsterTypes = new ArrayList<String>();
	
	public List<String> allTrapTypes = new ArrayList<String>();
	
	public List<String> allItemTypes = new ArrayList<String>();

	public List<String> itemTypes;

	public List<String> trapTypes;

	public List<String> monsterTypes;
	
	public int monstersCount;

	public List<Integer> roomIds;
	
	public void reset() {
		adventureNumber = 0;
		roomsCount = 0;
		roomsFile = null;
		heroesFile = null;
		goalsFile = null;
		corridorsFile = null;		
		resultDirRootRelativePath = null;
		allMonsterTypes.clear();
		allTrapTypes.clear();
		allItemTypes.clear();
	}
	
}