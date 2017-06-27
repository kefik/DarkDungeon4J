package cz.dd4j.utils.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.dd4j.utils.csv.CSV.CSVRow;

/**
 * CSV parser.
 * 
 * @author Jimmy
 */
public class CSV {

	private static class StringComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	}
	    
    private String delimiter = ";";
    
    public List<String> keys;
    
    public List<CSVRow> rows = new ArrayList<CSVRow>();

    public CSV() {    	
    }
    
    /**
     * @param filename
     * @param delimiter
     * @throws FileNotFoundException
     * @throws IOException
     */
    public CSV(File file, String delimiter, boolean containsHeaderRow) throws FileNotFoundException, IOException {
        super();
        
        this.delimiter = delimiter;
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        
        try {
	        String[] line = nextLine(reader);
	        
	        if (line == null) {
	        	throw new RuntimeException("File '" + file.getAbsolutePath() + "' does not contain any line!");
	        }
	        
	        if (containsHeaderRow) {
	        	keys = new ArrayList<String>();
	        	for (String item : line) {
	        		keys.add(item);
	        	}
	        } else {
	        	keys = new ArrayList<String>();
	        	for (int i = 0; i < line.length; ++i) {
	        		keys.add(String.valueOf(i));
	        	}
	        	processLine(line);
	        }
	        
	        while (reader.ready()) {
	        	processLine(nextLine(reader));
	        }
        } finally {
        	reader.close();
        }
    }
    
    private String[] nextLine(BufferedReader reader) throws IOException {
    	if (reader.ready()) {
    		String line = reader.readLine();
    		return line.split(delimiter);
    	}
    	return null;
    }
    
    private CSVRow processLine(String[] line) {
    	CSVRow result = new CSVRow();
    	for (int i = 0; i < line.length; ++i) {
    		if (i >= keys.size()) {
    			keys.add(String.valueOf(i));
    		}
    		String key = keys.get(i);
    		String value = line[i];
    		result.add(key, value);
    	}
    	rows.add(result);
    	return result;
	}    
    
    public void toFile(File file) {
		FileOutputStream output = null;
		PrintWriter writer = null;
		try {
			output = new FileOutputStream(file);
			writer = new PrintWriter(output);
			
			boolean first = true;
			for (String key : keys) {
				if (first) first = false;
				else writer.write(";");
				writer.write(key);
			}
			writer.println();
			
			for (CSVRow row : rows) {			
				first = true;
				for (String key : keys) {
					if (first) first = false;
					else writer.write(delimiter);
					String value = row.getString(key);
					value = value.replaceAll(delimiter, (delimiter.equals("|") ? ":" : "|"));
					writer.write(value);
				}
				writer.println();
			}
			
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

    public static class CSVRow {

        private Map<String, String> row;

        public CSVRow() {
            this.row = new HashMap<String, String>();
        }
        
        public void add(String key, String value) {
        	row.put(key, value);
        }
        
        public void add(String key, Object value) {
        	row.put(key, value.toString());
        }
        
        public void addAll(CSVRow csvRow) {
        	if (csvRow == null || csvRow.row == null) return;
        	this.row.putAll(csvRow.row);
		}
        
        public boolean containsKey(String name) {
        	return row != null && row.containsKey(name);
        }

        public String getString(String name) {
            String val = row.get(name);
            if (val == null) {
                return "";
            }
            return val;
        }

        public Integer getInt(String name) {
            try {
                int number = Integer.parseInt(getString(name));
                return number;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public Double getDouble(String name) {
            try {
                double number = Double.parseDouble(getString(name));
                return number;
            } catch (NumberFormatException e) {
                return null;
            }
        }

		
    }
}
