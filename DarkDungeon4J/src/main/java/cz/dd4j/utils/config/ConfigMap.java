package cz.dd4j.utils.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.dd4j.domain.ELabel;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.Convert;

/**
 * If getter does not find value in this map, looks up {@link Policy#INSTANCE} before returning default value.
 * @author Jimmy
 */
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
	
	@Override
	public Object get(Object key) {
		if (containsKey(key)) {
			return super.get(key);
		}
		return (this == Policy.INSTANCE ? null : Policy.INSTANCE.get(key));		
	}
	
	public int getInt(ELabel key) { 
		return getInt(key.id.name);
	}
	
	public int getInt(ELabel key, int defaultValue) { 
		return getInt(key.id.name, defaultValue);
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
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getInt(key, defaultValue));
	}
	
	public long getLong(ELabel key) { 
		return getLong(key.id.name);
	}
	
	public long getLong(ELabel key, long defaultValue) { 
		return getLong(key.id.name, defaultValue);
	}
	
	public long getLong(String key) { 
		return getLong(key, 0);
	}
	
	public long getLong(String key, long defaultValue) {
		if (containsKey(key)) {
			Long value = Convert.toLong(get(key));
			if (value == null) return defaultValue;
			return value;
		}
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getLong(key, defaultValue));
	}
	
	public double getDouble(ELabel key) { 
		return getDouble(key.id.name);
	}
	
	public double getDouble(ELabel key, double defaultValue) { 
		return getDouble(key.id.name, defaultValue);
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
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getDouble(key, defaultValue));
	}
	
	public float getFloat(ELabel key) { 
		return getFloat(key.id.name);
	}
	
	public float getFloat(ELabel key, float defaultValue) { 
		return getFloat(key.id.name, defaultValue);
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
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getFloat(key, defaultValue));
	}
	
	public String getString(ELabel key) { 
		return getString(key.id.name);
	}
	
	public String getString(ELabel key, String defaultValue) { 
		return getString(key.id.name, defaultValue);
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
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getString(key, defaultValue));
	}
	
	public boolean getBoolean(ELabel key) { 
		return getBoolean(key.id.name);
	}
	
	public boolean getBoolean(ELabel key, boolean defaultValue) { 
		return getBoolean(key.id.name, defaultValue);
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		if (containsKey(key)) return Convert.toBoolean(get(key));
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getBoolean(key, defaultValue));
	}
	
	public File getFile(ELabel key) { 
		return getFile(key.id.name);
	}
	
	public File getFile(ELabel key, File defaultValue) { 
		return getFile(key.id.name, defaultValue);
	}
	
	public File getFile(String key) {
		return getFile(key, null);
	}
	
	public File getFile(String key, File defaultValue) {
		if (containsKey(key)) {
			File value = Convert.toFile(get(key));
			if (value == null) return defaultValue;
			return value;
		}
		return (this == Policy.INSTANCE ? defaultValue : Policy.INSTANCE.getFile(key, defaultValue));
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
	
	/**
	 * Tries to auto-config 'object'. Auto-configs all {@link Configurable} annotated field (class hierarchy-wise); recursively auto-configures fields that are of class
	 * annotated with {@link AutoConfig} as well. Fields are configured ONLY IFF we have corresponding entry (based on field names) within the map.
	 * 
	 * @param object
	 * @param classInfo
	 */
	public void autoConfig(Object object, Class classInfo) {
		if (classInfo == null) return;
		if (classInfo == Object.class) return;
		
		for (Field field : classInfo.getDeclaredFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				autoConfigField(object, classInfo, field);
			} else 
			if (field.getType().isAnnotationPresent(AutoConfig.class)) {
				try {
					field.setAccessible(true);
					Object fieldValue = field.get(object);
					autoConfig(fieldValue);
				} catch (Exception e) {
					throw new RuntimeException("Failed to recursively auto-configure field '" + field.getName() + "' of class " + classInfo + " on object " + object + ".", e);
				}
			}
		}
		
		// recursively auto-configure parent class
		autoConfig(object, classInfo.getSuperclass());
	}

	private void autoConfigField(Object object, Class classInfo, Field field) {
		String fieldName = field.getName();
		if (!containsKey(fieldName)) {
			// TRY TO USE POLICY AS DEFAULTS...
			
			return;
		}
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
		} else
		if (field.getType() == File.class) {
			autoConfigFieldFile(object, classInfo, field, fieldName);
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
	
	private void autoConfigFieldFile(Object object, Class classInfo, Field field, String fieldName) {		
		try {
			field.setAccessible(true);
			File value = getFile(fieldName);
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to auto-configure field '" + fieldName + "' of class " + classInfo + " on object " + object + ".", e);
		}
	}
	
	
	
}
