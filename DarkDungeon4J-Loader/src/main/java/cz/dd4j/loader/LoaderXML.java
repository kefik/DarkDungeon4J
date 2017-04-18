package cz.dd4j.loader;

import cz.dd4j.loader.utils.EnumTypeConverter;
import cz.dd4j.loader.utils.IdTypeConverter;
import cz.dd4j.utils.xstream.XStreamLoader;

public class LoaderXML<XML> extends XStreamLoader<XML>  {

	public LoaderXML(Class<XML> classToLoad) {
		super(classToLoad);
		xstream.registerConverter(new EnumTypeConverter());
		xstream.registerConverter(new IdTypeConverter());
	}

}
