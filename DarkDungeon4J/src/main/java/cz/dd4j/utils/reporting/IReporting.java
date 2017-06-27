package cz.dd4j.utils.reporting;

import java.util.List;

import cz.dd4j.utils.csv.CSV.CSVRow;

public interface IReporting {

	public List<String> getCSVHeaders();
	
	public CSVRow getCSVRow();
	
}
