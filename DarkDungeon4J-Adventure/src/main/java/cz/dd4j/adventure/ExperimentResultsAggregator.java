package cz.dd4j.adventure;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.adventure.ExperimentEvaluator.Playout;
import cz.dd4j.utils.csv.CSV;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class ExperimentResultsAggregator {

	private File resultsDir;
	private File outputDir;

	private CSV results;
	private CSV dungeonDesc;
	
	public ExperimentResultsAggregator(File resultsDir, File outputDir) {
		this.resultsDir = resultsDir;
		this.outputDir = outputDir;
	}
	
	public void aggregate() {
		System.out.println("Aggregating results...");
		
		results = new CSV();
		results.keys = new ArrayList<String>();
		
		dungeonDesc = new CSV();
		dungeonDesc.keys = new ArrayList<String>();
		
		for (File file : resultsDir.listFiles()) {
			if (file.getName().endsWith("-result.csv")) {
				processResultFile(file);
			} else
			if (file.getName().endsWith("-dungeon_descriptor.csv")) {
				processDescriptorFile(file);
			}
		}
	
		dungeonDesc.keys.remove(Playout.CSV_ID);
		dungeonDesc.keys.remove(Playout.CSV_HERO);
		
		results.toFile(new File(outputDir, "aggregated-results.csv"));		
		dungeonDesc.toFile(new File(outputDir, "aggregated-dungeon_descriptors.csv"));
	}

	private void processResultFile(File file) {
		CSV csv = null;
		try {
			csv = new CSV(file, ";", true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse CSV file at: " + file.getAbsolutePath(), e);
		}
		
		for (String key : csv.keys) {
			if (results.keys.contains(key)) continue;
			results.keys.add(key);
		}
		
		results.rows.addAll(csv.rows);
	}
	
	private void processDescriptorFile(File file) {
		CSV csv = null;
		try {
			csv = new CSV(file, ";", true);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse CSV file at: " + file.getAbsolutePath(), e);
		}
		
		for (String key : csv.keys) {
			if (dungeonDesc.keys.contains(key)) continue;
			dungeonDesc.keys.add(key);
		}
		
		for (CSVRow newRow : csv.rows) {
			boolean add = true;
			for (CSVRow existingRow : dungeonDesc.rows) {
				if (existingRow.getString(Playout.CSV_ADVENTURE).equals(newRow.getString(Playout.CSV_ADVENTURE))) {
					add = false;
					break;				
				}
			}
			if (add) dungeonDesc.rows.add(newRow);
		}		
	}
	
	public static void main(String[] args) {
		// FOR TESTING / MANUAL RUNS
		File resultsDir = new File("./results");
		File outputDir = new File("./results");
		
		System.out.println("Aggregating Experiment Results");
		System.out.println("-- results taken from          " + resultsDir.getAbsolutePath());
		System.out.println("-- aggregated file output into " + outputDir.getAbsolutePath());
		
		ExperimentResultsAggregator aggregator = new ExperimentResultsAggregator(resultsDir, outputDir);
		aggregator.aggregate();
		
		System.out.println("---// FINISHED //---");
	}
	
}
