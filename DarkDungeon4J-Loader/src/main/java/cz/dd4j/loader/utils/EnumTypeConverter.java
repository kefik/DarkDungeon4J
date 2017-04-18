package cz.dd4j.loader.utils;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import cz.cuni.amis.utils.eh4j.EnumObject;
import cz.cuni.amis.utils.eh4j.Enums;
import cz.cuni.amis.utils.eh4j.shortcut.EH;

public class EnumTypeConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return EH.type(type) != null;
	}

	@Override
	public Object fromString(String str) {
		if (str == null || str.length() == 0) return null;
		EnumObject enumObject = Enums.getInstance().getEnumObject(str);
		if (enumObject == null) return null;
		return enumObject.enumInstance;
	}

}
