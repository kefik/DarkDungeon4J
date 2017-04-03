package cz.dd4j.utils;

public class Convert {

	public static Integer toInt(Object obj) {
		if (obj == null) return null;
		if (obj instanceof Integer) return (Integer)(obj);
		return Integer.parseInt(obj.toString());
	}
	
	public static Double toDouble(Object obj) {
		if (obj == null) return null;
		if (obj instanceof Double) return (Double)(obj);
		return Double.parseDouble(obj.toString());
	}
	
	public static String toString(Object obj) {
		if (obj == null) return null;
		return obj.toString();
	}
	
	public static Boolean toBoolean(Object obj) {
		if (obj == null) return null;
		
		if (obj instanceof Boolean) return (Boolean)obj;
		
		if (obj instanceof Integer) return ((Integer)(obj)) > 0;
		
		if (obj instanceof Double) return ((Double)(obj)) > 0;
		
		if (obj instanceof String) {
			String str = toString(obj).toLowerCase();
			return str.toLowerCase().equals("1") || str.toLowerCase().equals("t") || str.toLowerCase().equals("true");
		}
		
		return false;
	}
	
}
