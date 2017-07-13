package cz.dd4j.generator.adventure.impls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.dd4j.generator.GeneratorBase;
import cz.dd4j.generator.adventure.callbacks.AdventureContext;
import cz.dd4j.loader.simstate.impl.xml.FileXML;
import cz.dd4j.loader.simstate.impl.xml.SimStateXML;
import cz.dd4j.utils.files.DirCrawler;
import cz.dd4j.utils.files.DirCrawlerCallback;

public abstract class AdventureGeneratorBase<CONFIG extends AdventureGeneratorConfigBase> extends GeneratorBase<CONFIG>{
	
	public static final Pattern ROOMS_PATTERN = Pattern.compile("Requires: Rooms([0-9]*)\\.xml"); 
	
	public static final String MONSTER_AGENT_TYPE_PATTERN_STR = "monster[0-9]*-([^.]*)\\.xml";
	
	public static final Pattern MONSTER_AGENT_TYPE_PATTERN = Pattern.compile(MONSTER_AGENT_TYPE_PATTERN_STR + "$");
	
	public static final String TRAP_AGENT_TYPE_PATTERN_STR = "trap[0-9]*-([^.]*)\\.xml";
	
	public static final Pattern TRAP_AGENT_TYPE_PATTERN = Pattern.compile(TRAP_AGENT_TYPE_PATTERN_STR + "$");
	
	public static final String ITEM_TYPE_PATTERN_STR = "([a-zA-Z_]*)[0-9]*-Room[0-9]*\\.xml";
	
	public static final Pattern ITEM_TYPE_PATTERN = Pattern.compile(ITEM_TYPE_PATTERN_STR + "$");
	
	protected AdventureContext ctx = new AdventureContext();	
	
	public AdventureGeneratorBase(CONFIG config) {
		super(SimStateXML.class, config);
	}
	
	protected void reset() {
		ctx.reset();
	}
	
	protected void init() {
		reset();		
		probeMonsterTypes();
		probeTrapTypes();
		probeItemTypes();
	}

	protected void probeMonsterTypes() {
		ctx.allMonsterTypes.clear();
		File dir = config.target.getDir(config.agentMonstersDir);
		probeTypes(MONSTER_AGENT_TYPE_PATTERN, 1, dir, ctx.allMonsterTypes);
	}

	protected void probeTrapTypes() {
		ctx.allTrapTypes.clear();
		File agentTrapsDir = config.target.getDir(config.agentTrapsDir);
		probeTypes(TRAP_AGENT_TYPE_PATTERN, 1, agentTrapsDir, ctx.allTrapTypes);
	}
	
	protected void probeItemTypes() {
		ctx.allItemTypes.clear();
		File dir = config.target.getDir(config.itemsDir);
		probeTypes(ITEM_TYPE_PATTERN, 1, dir, ctx.allItemTypes);
	}
	
	protected void probeTypes(final Pattern pattern, final int typeInMatcherGroup, File rootDir, final List<String> output) {
		final Set<String> types = new HashSet<String>();
		
		DirCrawler.crawl(rootDir, new DirCrawlerCallback() {
			
			@Override
			public void visitFile(File file) {
				
				Matcher matcher = pattern.matcher(file.getName());
				if (matcher.find()) {
					String type = matcher.group(typeInMatcherGroup);
					types.add(type);
				}
			}
			
		});		
		
		output.addAll(types);
		Collections.sort(output);		
	}

	protected String determineResultDirRootRelativePath() {
		String result = "";
		
		String[] parts = config.target.dir.getPath().split("/");
		if (parts.length == 1) {
			parts = config.target.dir.getPath().split("\\\\");
		}
		for (int i = 0; i < parts.length; ++i) {
			result += "../";
		}
		
		result += config.source.dir.getPath().replaceAll("\\\\", "/") + "/";
		
		return result;
	}

	protected FileXML newFileXML(String file) {
		FileXML result = new FileXML();
		result.path = ctx.resultDirRootRelativePath + file;
		return result;
	}

	protected int readRequiredRooms(File corridorsFile) {
		FileInputStream stream = null;
		BufferedReader reader = null;
		try {
			stream = new FileInputStream(corridorsFile);
			reader = new BufferedReader(new InputStreamReader(stream));
			while (reader.ready()) {
				String line = reader.readLine();
				Matcher matcher = ROOMS_PATTERN.matcher(line);
				if (matcher.find()) {
					return Integer.parseInt(matcher.group(1));
				}
			}
		} catch (Exception e) {	
			throw new RuntimeException("Failed to open file: " + corridorsFile.getAbsolutePath());
		} finally {
			if (reader != null) {				
				try { reader.close(); } catch (Exception e) {}
			}
			if (stream != null) {				
				try { stream.close(); } catch (Exception e) {}
			}
		}
				
		throw new RuntimeException("Failed to find the required number of rooms in file: " + corridorsFile.getAbsolutePath());
	}
	
}
