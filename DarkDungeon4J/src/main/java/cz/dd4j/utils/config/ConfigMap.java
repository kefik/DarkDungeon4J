package cz.dd4j.utils.config;

import java.lang.reflect.Field;
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
	
	public long getLong(String key) { 
		return getLong(key, 0);
	}
	
	public long getLong(String key, int defaultValue) {
		if (containsKey(key)) {
			Long value = Convert.toLong(get(key));
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
	
	public float getFloat(String key) {
		return getFloat(key, 0);
	}
	
	public float getFloat(String key, float defaultValue) {
		if (containsKey(key)) {
			Float value = Convert.toFloat(get(key));
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
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
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
	
	//
	// REFLECTION BASED AUTO-CONFIGURATION
	// -- using @Configurable ... {@link Configurable}
	//
	
	public void autoConfig(Object object) {
		if (object == null) return;
		Class cls = object.getClass();
		autoConfig(object, cls);
	}
	
	public void autoConfig(Object object, Class classInfo) {
		if (classInfo == null) return;
		if (classInfo == Object.class) return;
		
		for (Field field : classInfo.getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				autoConfigField(object, classInfo, field);
			}
		}
		
		// recursively auto-configure parent class
		autoConfig(object, classInfo.getSuperclass());
	}

	private void autoConfigField(Object object, Class classInfo, Field field) {
		String fieldName = field.getName();
		if (!containsKey(fieldName)) return;
		if (field.getType() == int.class || field.getType() == Integer.class) {
			autoConfigFieldInt(object, classInfo, field, fieldName);
		} else
		if (field.getType() == boolean.class || field.getType() == Boolean.class) {
			autoConfigFieldBoolean(object, classInfo, field, fieldName);
		} else
		if (field.getType() == String.class) {
			autoConfigFieldString(object, classInfo, field, fieldName);
		} else
		if (field.getType() == double.class || field.getType() == Double.class) {
			autoConfigFieldDouble(object, classInfo, field, fieldName);
		} else
		if (field.getType() == long.class || field.getType() == Long.class) {
			autoConfigFieldLong(object, classInfo, field, fieldName);
		} else
		if (field.getType() == float.class || field.getType() == Float.class) {
			autoConfigFieldFloat(object, classInfo, field, fieldName);
		} else {
			throw new RuntimeException("Auto-configuration of field of type " + field.getType() + " is unsupported, failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".");
		}
	}

	private void autoConfigFieldInt(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			int value = getInt(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	private void autoConfigFieldBoolean(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			boolean value = getBoolean(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	private void autoConfigFieldString(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			String value = getString(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	private void autoConfigFieldDouble(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			double value = getDouble(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	private void autoConfigFieldLong(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			float value = getLong(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	private void autoConfigFieldFloat(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			float value = getFloat(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	
	
}
