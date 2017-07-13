package cz.dd4j.adventure.aggregators;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.adventure.ExperimentEvaluator.Playout;
import cz.dd4j.utils.csv.CSV;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class ExperimentResultsAggregator {

	private File resultsDir;
	private File outputDir;

	private CSV results;
	private CSV simstateDesc;
	
	public ExperimentResultsAggregator(File resultsDir, File outputDir) {
		this.resultsDir = resultsDir;
		this.outputDir = outputDir;
	}
	
	public void aggregate() {
		ResultsAggregator resultsAggregator = new ResultsAggregator(resultsDir, outputDir);
		resultsAggregator.aggregate();
		
		AdventureDescriptorAggregator simstateAggregator = new AdventureDescriptorAggregator(resultsDir, outputDir);
		simstateAggregator.aggregate();
		
		System.out.println("Aggregating results...");
		
		results = new CSV();
		results.keys = new ArrayList<String>();
		
		simstateDesc = new CSV();
		simstateDesc.keys = new ArrayList<String>();
		
		for (File file : resultsDir.listFiles()) {
			if (file.getName().endsWith("-result.csv")) {
				processResultFile(file);
			} else
			if (file.getName().endsWith("-simstate_descriptor.csv")) {
				processDescriptorFile(file);
			}
		}
	
		simstateDesc.keys.remove(Playout.CSV_ID);
		simstateDesc.keys.remove(Playout.CSV_HERO);
		
		results.toFile(new File(outputDir, "aggregated-results.csv"));		
		simstateDesc.toFile(new File(outputDir, "aggregated-simstate_descriptors.csv"));
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
		
		System.out.println("Aggregating Experiment Results");
		System.out.println("-- results taken from          " + resultsDir.getAbsolutePath());
		System.out.println("-- aggregated file output into " + outputDir.getAbsolutePath());
		
		ExperimentResultsAggregator aggregator = new ExperimentResultsAggregator(resultsDir, outputDir);
		aggregator.aggregate();
		
		System.out.println("---// FINISHED //---");
	}
	
}
