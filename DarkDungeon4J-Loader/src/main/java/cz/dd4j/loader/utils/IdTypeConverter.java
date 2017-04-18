package cz.dd4j.loader.utils;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import cz.dd4j.utils.Id;

public class IdTypeConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type == Id.class;
	}

	@Override
	public Object fromString(String str) {
		if (str == null) return Id.get("null");
		return Id.get(str);
	}
	
}
