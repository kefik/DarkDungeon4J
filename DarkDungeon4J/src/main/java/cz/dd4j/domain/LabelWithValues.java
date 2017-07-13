package cz.dd4j.domain;

import cz.dd4j.utils.config.ConfigMap;

public class LabelWithValues {
	
	public final ELabel label;
	
	public final ConfigMap values = new ConfigMap();

	public LabelWithValues(ELabel label) {
		this.label = label;
	}

}
