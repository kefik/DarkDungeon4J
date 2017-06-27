package cz.dd4j.utils.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cz.dd4j.utils.csv.CSV.CSVRow;

public class Reporting implements IReporting {

	private List<String> headers;
	
	private CSVRow row;

	private IReporting[] elements;
	
	public Reporting(IReporting... elements) {
		this.elements = elements;		
	}
	
	/**
	 * Queries all elements specified within {@link #ReportRow(IReporting...)} and refreshes values {@link #getCSVHeaders()} and {@link #getCSVRow()}.
	 */
	public void report() {
		this.headers = new ArrayList<String>();
		this.row = new CSVRow();
		
		for (IReporting element : elements) {			
			List<String> headers = element.getCSVHeaders();			
			if (headers == null || headers.size() == 0) continue;
			this.headers.addAll(headers);

			CSVRow elementRow = element.getCSVRow();
			for (String key : headers) {
				if (this.row.containsKey(key)) {
					throw new RuntimeException("Clash in CSV headers: " + key);
				}
				this.row.add(key, elementRow.getString(key));
			}			
		}	
	}
	
	@Override
	public List<String> getCSVHeaders() {
		return headers;
	}
	
	@Override
	public CSVRow getCSVRow() {
		return row;
	}
	
	public void reportToFile(File file) {
		report();
		
		FileOutputStream output = null;
		PrintWriter writer = null;
		try {
			output = new FileOutputStream(file);
			writer = new PrintWriter(output);
			
			boolean first = true;
			for (String key : getCSVHeaders()) {
				if (first) first = false;
				else writer.write(";");
				writer.write(key);
			}
			writer.println();
			
			first = true;
			for (String key : getCSVHeaders()) {
				if (first) first = false;
				else writer.write(";");
				String value = getCSVRow().getString(key);
				value = value.replaceAll(";", "|");
				writer.write(value);
			}
			writer.println();
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to save CSV into " + file.getAbsolutePath());
		} finally {
			if (writer != null) {
				try { writer.close(); } catch (Exception e) {}
				writer = null;
			}
			if (output != null) {
				try { output.close(); } catch (Exception e) {}
				output = null;
			}
		}
		
	}
	
}
