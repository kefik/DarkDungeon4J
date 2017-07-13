package cz.dd4j.descriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.dd4j.utils.csv.CSV;
import cz.dd4j.utils.csv.CSV.CSVRow;
import cz.dd4j.utils.files.DirCrawler;
import cz.dd4j.utils.files.DirCrawlerCallback;

public class AdventureDescriptorsFileGenerator {

	public static final String CSV_ADVENTURE = "EXP-adventure";
	
	private File adventuresDir;
	private File outputDir;

	private CSV adventureDesc;
	
	public AdventureDescriptorsFileGenerator(File adventuresDir, File outputDir) {
		this.adventuresDir = adventuresDir;
		this.outputDir = outputDir;
	}
	
	public void describeAdventures() {
		System.out.println("Generating adventure descriptors...");
		
		adventureDesc = new CSV();
		adventureDesc.keys = new ArrayList<String>();
		adventureDesc.keys.add(CSV_ADVENTURE);		
		
		DirCrawler.crawl(adventuresDir, new DirCrawlerCallback() {

			@Override
			public void visitFile(File file) {
				if (file.getName().endsWith(".xml")) {
					processSimStateXMLFile(file);
				}
			}
			
		});
		
		adventureDesc.toFile(new File(outputDir, "adventure_descriptors.csv"));
	}

	private void processSimStateXMLFile(File file) {
		System.out.println("-- processing " + file.getAbsolutePath());
		
		AdventureDescriptor desc = null;
		try {
			desc = AdventureDescriptor.describe(file);
		} catch (Exception e) {
			// we assume that this is not an adventure file containing SimStateXML ...
			return;
		}
		
		List<String> csvHeaders = desc.getCSVHeaders();				
		for (String key : csvHeaders) {
			if (adventureDesc.keys.contains(key)) continue;
			adventureDesc.keys.add(key);
		}
		
		CSVRow csvRow = desc.getCSVRow();
		csvRow.add(CSV_ADVENTURE, file.getName());
		
		adventureDesc.rows.add(csvRow);
	}
	
	public static void main(String[] args) {
		// FOR TESTING / MANUAL RUNS
		File adventuresDir = new File("d:/Temp/DD4j/dd4j/DarkDungeon4J-Generator/result/adventures/2017/june/full");
		File outputDir = new File("d:/Temp/DD4j/dd4j/DarkDungeon4J-Generator/result/adventures/2017/june");
		
		System.out.println("Generating adventure descriptors file");
		System.out.println("-- adventures taken from   " + adventuresDir.getAbsolutePath());
		System.out.println("-- result file output into " + outputDir.getAbsolutePath());
		
		AdventureDescriptorsFileGenerator aggregator = new AdventureDescriptorsFileGenerator(adventuresDir, outputDir);
		aggregator.describeAdventures();
		
		System.out.println("---// FINISHED //---");
	}
	
}
