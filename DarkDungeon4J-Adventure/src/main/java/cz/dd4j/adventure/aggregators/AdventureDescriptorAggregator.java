package cz.dd4j.adventure.aggregators;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.adventure.ExperimentEvaluator.Playout;
import cz.dd4j.utils.csv.CSV;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class AdventureDescriptorAggregator {

	private File resultsDir;
	private File outputDir;

	private CSV simstateDesc;
	
	public AdventureDescriptorAggregator(File resultsDir, File outputDir) {
		this.resultsDir = resultsDir;
		this.outputDir = outputDir;
	}
	
	public void aggregate() {
		System.out.println("Aggregating dventure descriptors...");
		
		simstateDesc = new CSV();
		simstateDesc.keys = new ArrayList<String>();
		
		for (File file : resultsDir.listFiles()) {
			if (file.getName().endsWith("-adventure_descriptor.csv")) {
				processDescriptorFile(file);
			}
		}
	
		simstateDesc.keys.remove(Playout.CSV_ID);
		simstateDesc.keys.remove(Playout.CSV_HERO);
		
		simstateDesc.toFile(new File(outputDir, "aggregated-adventure_descriptors.csv"));
	}

	private void processDescriptorFile(File file) {
		CSV csv = null;
		try {
			csv = new CSV(file, ";", true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse CSV file at: " + file.getAbsolutePath(), e);
		}
		
		for (String key : csv.keys) {
			if (simstateDesc.keys.contains(key)) continue;
			simstateDesc.keys.add(key);
		}
		
		for (CSVRow newRow : csv.rows) {
			boolean add = true;
			for (CSVRow existingRow : simstateDesc.rows) {
				if (existingRow.getString(Playout.CSV_ADVENTURE).equals(newRow.getString(Playout.CSV_ADVENTURE))) {
					add = false;
					break;				
				}
			}
			if (add) simstateDesc.rows.add(newRow);
		}		
	}
	
	public static void main(String[] args) {
		// FOR TESTING / MANUAL RUNS
		File resultsDir = new File("./results");
		File outputDir = new File("./results");
		
		System.out.println("Aggregating Experiment SimState Descriptors");
		System.out.println("-- results taken from          " + resultsDir.getAbsolutePath());
		System.out.println("-- aggregated file output into " + outputDir.getAbsolutePath());
		
		AdventureDescriptorAggregator aggregator = new AdventureDescriptorAggregator(resultsDir, outputDir);
		aggregator.aggregate();
		
		System.out.println("---// FINISHED //---");
	}
	
}
