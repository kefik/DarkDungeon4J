package cz.dd4j.utils.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.dd4j.utils.Const;
import cz.dd4j.utils.Convert;

public class ConfigMap extends HashMap<String, Object> {

	/**
	 * Auto-generated
	 */
	private static final long serialVersionUID = -8761199575929886868L;

	public ConfigMap() {		
	}
	
	public ConfigMap(Collection<? extends IConfigEntry> config) {
		for (IConfigEntry pair : config) {
			put(pair.getKey(), pair.getValue());
		}
	}
	
	public int getInt(String key) { 
		return getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue) {
		if (containsKey(key)) {
			Integer value = Convert.toInt(get(key));
			if (value == null) return defaultValue;
			return value;
		}
		return defaultValue;
	}
	
	public double getDouble(String key) {
		return getDouble(key, 0);
	}
	
	public double getDouble(String key, double defaultValue) {
		if (containsKey(key)) {
			Double value = Convert.toDouble(get(key));
			if (value == null) return defaultValue;
			return value;
		}
		return defaultValue;
	}
	
	public String getString(String key) {
		return getString(key, "");
	}
	
	public String getString(String key, String defaultValue) {
		if (containsKey(key)) {
			String value = Convert.toString(get(key));
			if (value == null) return defaultValue;
			return value;
		}
		return defaultValue;
	}
	
	public boolean getBooolean(String key) {
		return getBooolean(key, false);
	}
	
	public boolean getBooolean(String key, boolean defaultValue) {
		if (containsKey(key)) return Convert.toBoolean(get(key));
		return defaultValue;
	}

	public String describe() {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Map.Entry<String, Object> entry : entrySet()) {
			if (first) first = false;
			else sb.append(Const.NEW_LINE);
			sb.append(entry.getKey());
			sb.append(":");
			sb.append(entry.getValue());
		}
		return null;
	}
	
}
