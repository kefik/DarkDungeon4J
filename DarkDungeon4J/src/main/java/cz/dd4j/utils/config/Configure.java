package cz.dd4j.utils.config;

import java.util.Collection;

public class Configure {
	
	public static void configure(Object object, Collection<ConfigXML> configuration) {
		if (configuration != null && !configuration.isEmpty()) {
			ConfigMap config = new ConfigMap(configuration);	
			configure(object, config);
		}		
	}
	
	public static void configure(IConfigurable object, Collection<ConfigXML> configuration) {
		if (configuration != null && !configuration.isEmpty()) {
			ConfigMap config = new ConfigMap(configuration);	
			configure(object, config);
		}
	}
	
	public static void configure(Object object, ConfigMap configuration) {
		if (configuration != null && configuration.size() > 0) {
			if (!(IConfigurable.class.isAssignableFrom(object.getClass()))) {
				throw new RuntimeException("Cannot configure " + object.toString() + " that is instance of class " + object.getClass().getName() + " as it is not implementing IConfigurable interface.");
			}
			IConfigurable e = (IConfigurable)object;
			configure(e, configuration);
		}
	}
	
	public static void configure(IConfigurable object, ConfigMap configuration) {
		try {
			object.configure(configuration);
		} catch (Exception e) {
			throw new RuntimeException("Failed to configure instance of class: " + object.getClass().getName(), e);
		}
	}

}
