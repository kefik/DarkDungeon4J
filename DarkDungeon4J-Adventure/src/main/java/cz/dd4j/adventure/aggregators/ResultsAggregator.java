package cz.dd4j.adventure.aggregators;

import java.io.File;
import java.util.ArrayList;

import cz.dd4j.adventure.ExperimentEvaluator.Playout;
import cz.dd4j.utils.csv.CSV;
import cz.dd4j.utils.csv.CSV.CSVRow;

public class ResultsAggregator {

	private File resultsDir;
	private File outputDir;

	private CSV results;
	
	public ResultsAggregator(File resultsDir, File outputDir) {
		this.resultsDir = resultsDir;
		this.outputDir = outputDir;
	}
	
	public void aggregate() {
		System.out.println("Aggregating results...");
		
		results = new CSV();
		results.keys = new ArrayList<String>();
		
		for (File file : resultsDir.listFiles()) {
			if (file.getName().endsWith("-result.csv")) {
				processResultFile(file);
			}
		}
	
		results.toFile(new File(outputDir, "aggregated-results.csv"));		
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
	
	public static void main(String[] args) {
		// FOR TESTING / MANUAL RUNS
		File resultsDir = new File("./results");
		File outputDir = new File("./results");
		
		System.out.println("Aggregating Experiment Results");
		System.out.println("-- results taken from          " + resultsDir.getAbsolutePath());
		System.out.println("-- aggregated file output into " + outputDir.getAbsolutePath());
		
		ResultsAggregator aggregator = new ResultsAggregator(resultsDir, outputDir);
		aggregator.aggregate();
		
		System.out.println("---// FINISHED //---");
	}
	
}
