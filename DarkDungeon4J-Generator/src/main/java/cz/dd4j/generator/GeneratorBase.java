package cz.dd4j.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cz.dd4j.loader.LoaderXML;
import cz.dd4j.loader.meta.MetaInfo;
import cz.dd4j.utils.Const;
import cz.dd4j.utils.config.GenericConfig;

public abstract class GeneratorBase<CONFIG extends GenericConfig> {

	protected Class classToSave;
	
	protected CONFIG config;
	
	protected XStream xstream;

	public GeneratorBase(Class classToSave, CONFIG config) {
		this.classToSave = classToSave;
		this.config = config;	
		xstream = init();		
	}
	
	protected XStream init() {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.alias(((XStreamAlias)classToSave.getAnnotation(XStreamAlias.class)).value(), classToSave);
		LoaderXML.registerXStreamExtensions(xstream);
		return xstream;

	}
	
	public abstract void generate();
	
	protected void ensureDirsForFile(File targetFile) {
		if (targetFile.getParentFile() != null) {
			targetFile.getParentFile().mkdirs();
		}
	}
	
	public void write(File targetFile, Object data, Class loaderClass) {
		write(targetFile, data, loaderClass, null);
	}
	
	public void write(File targetFile, Object data, Class loaderClass, String comment) {
		ensureDirsForFile(targetFile);
		
		config.log.info(getClass().getSimpleName() + ".write(): writing file " + targetFile.getAbsolutePath());
		
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(targetFile);
			if (comment != null) {
				PrintWriter writer = new PrintWriter(out);
				writer.print("<!--" + Const.NEW_LINE);
				writer.print(formatComment(comment));
				writer.print(" -->" + Const.NEW_LINE);
				writer.flush();				
			}
			xstream.toXML(data, out);
		} catch (Exception e) {
			throw new RuntimeException("Failed to save data into: " + targetFile.getAbsolutePath());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {					
				}
			}
		}
		
		writeLoaderMeta(targetFile, loaderClass);
	}
	
	private String formatComment(String comment) {
		String[] parts = comment.split(Const.NEW_LINE_WIN);
		if (parts.length == 1) parts = comment.split(Const.NEW_LINE_LINUX);
		StringBuffer result = new StringBuffer();
		
		for (String line : parts) {
			result.append("    " + line);
			result.append(Const.NEW_LINE);
		}
		
		return result.toString();
	}

	protected void writeLoaderMeta(File forFile, Class loaderClass) {
		if (forFile == null) throw new RuntimeException("forFile is NULL");
		if (loaderClass == null) throw new RuntimeException("loaderClass is NULL");
		
		ensureDirsForFile(forFile);
		
		File targetFile = new File(forFile.getAbsolutePath() + ".meta");
		
		config.log.info(getClass().getSimpleName() + ".writeLoaderMeta(): writing file " + targetFile.getAbsolutePath());
		
		MetaInfo info = new MetaInfo();
		info.loaderFQCN = loaderClass.getName();
		
		FileOutputStream out = null;
		
		try {
			out = new FileOutputStream(targetFile);
			xstream.toXML(info, out);
		} catch (Exception e) {
			throw new RuntimeException("Failed to save meta information into: " + targetFile.getAbsolutePath());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {					
				}
			}
		}
	}
	
}
