package cz.dd4j.utils.config;

import java.io.File;

import cz.dd4j.utils.xstream.XStreamLoader;

/**
 * Defaults for {@link ConfigMap}; if some value is not found within {@link ConfigMap}, the {@link ConfigMap} consults {@link #INSTANCE} first.
 * 
 * @author Jimmy
 */
public class Policy {

	public static final ConfigMap INSTANCE = new ConfigMap();
	
	public static class PolicyXMLLoader extends XStreamLoader<PolicyXML> {

		public PolicyXMLLoader() {
			super(PolicyXML.class);
		}
		
	}
	
	public static final PolicyXMLLoader POLICY_XML_LOADER = new PolicyXMLLoader();
	
	public static void loadPolicy(File xmlFile) {
		PolicyXML policy = POLICY_XML_LOADER.load(xmlFile);
		
		for (ConfigXML config : policy.values) {
			INSTANCE.put(config.key, config.value);
		}
		
	}
	
}
