package cz.dd4j.utils.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import cz.dd4j.domain.ELabel;

@XStreamAlias("config")
public class ConfigXML implements IConfigEntry {

	/**
	 * Parameter-less constructor required for XStream deserialization
	 */
	public ConfigXML() {
	}
	
	public ConfigXML(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public ConfigXML(String key, int value) {
		this.key = key;
		this.value = String.valueOf(value);
	}
	
	public ConfigXML(String key, double value) {
		this.key = key;
		this.value = String.valueOf(value);
	}
	
	public ConfigXML(String key, boolean value) {
		this.key = key;
		this.value = String.valueOf(value);
	}
	
	public ConfigXML(ELabel key, String value) {
		this.key = key.id.name;
		this.value = value;
	}
	
	public ConfigXML(ELabel key, int value) {
		this.key = key.id.name;
		this.value = String.valueOf(value);
	}
	
	public ConfigXML(ELabel key, double value) {
		this.key = key.id.name;
		this.value = String.valueOf(value);
	}
	
	public ConfigXML(ELabel key, boolean value) {
		this.key = key.id.name;
		this.value = String.valueOf(value);
	}
	
	public String key;
	
	public String value;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Object getValue() {
		return value;
	}
	
}
