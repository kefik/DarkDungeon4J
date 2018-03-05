package cz.dd4j.adventure;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * Console-app frontend for the {@link ExperimentEvaluator}.
 * 
 * @author Jimmy
 */
public class Main {
	
	private static final char ARG_ADVENTURES_DIR_SHORT = 'a';
	
	private static final String ARG_ADVENTURES_DIR_LONG = "adventures-dir";
	
	private static final char ARG_HEROES_DIR_SHORT = 'h';
	
	private static final String ARG_HEROES_DIR_LONG = "heroes-dir";
	
	private static final char ARG_RESULT_DIR_SHORT = 'r';
	
	private static final String ARG_RESULT_DIR_LONG = "result-dir";
	
	private static final char ARG_PLAYOUT_LIMIT_SHORT = 'l';
	
	private static final String ARG_PLAYOUT_LIMIT_LONG = "max-playouts";
	
	private static final char ARG_VIS_CONSOLE_SHORT = 'v';
	
	private static final String ARG_VIS_CONSOLE_LONG = "vis-console";
	
	private static final char ARG_VIS_FILE_SHORT = 'p';
	
	private static final String ARG_VIS_FILE_LONG = "vis-file";

	private static final char ARG_MAX_CORES_SHORT = 'c';
	
	private static final String ARG_MAX_CORES_LONG = "max-cores";
	
	private static final char ARG_TIMEOUT_MULTIPLIER_SHORT = 't';
	
	private static final String ARG_TIMEOUT_MULTIPLIER_LONG = "timeout-multiplier";
		
	private static JSAP jsap;

	private static String adventuresDir;
	
	private static File adventuresDirFile;
	
	private static String heroesDir;
	
	private static File heroesDirFile;

	private static String resultDir;
	
	private static File resultDirFile;
	
	private static int playoutLimit;
	
	private static boolean visConsole;
	
	private static boolean visFile;

	private static int maxCores;
	
	private static boolean headerOutput = false;
	
	private static double timeoutMultiplier;

	private static JSAPResult config;

	private static void fail(String errorMessage) {
		fail(errorMessage, null);
	}

	private static void fail(String errorMessage, Throwable e) {
		header();
		System.out.println("ERROR: " + errorMessage);
		System.out.println();
		if (e != null) {
			e.printStackTrace();
			System.out.println("");
		}		
        System.out.println("Usage: java -jar adventure.jar ");
        System.out.println("                " + jsap.getUsage());
        System.out.println();
        System.out.println(jsap.getHelp());
        System.out.println();
        throw new RuntimeException("FAILURE: " + errorMessage);
	}

	private static void header() {
		if (headerOutput) return;
		System.out.println();
		System.out.println("==============================================");
		System.out.println("DarkDungeon4J Adventure - Experiment Evaluator");
		System.out.println("==============================================");
		System.out.println();
		System.out.println("This program evaluates 'all heroes' on 'all adventures' outputing the results 'somewhere'.");
		System.out.println();
		headerOutput = true;
	}
		
	private static void initJSAP() throws JSAPException {
		jsap = new JSAP();
		
		FlaggedOption opt1 = new FlaggedOption(ARG_ADVENTURES_DIR_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(true)
	    	.setShortFlag(ARG_ADVENTURES_DIR_SHORT)
	    	.setLongFlag(ARG_ADVENTURES_DIR_LONG);    
	    opt1.setHelp("Directory where you have your adventure XML files; XML files are searched in sub-directories recursively.");
	
	    jsap.registerParameter(opt1);
	    
	    FlaggedOption opt2 = new FlaggedOption(ARG_HEROES_DIR_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(true)
	    	.setShortFlag(ARG_HEROES_DIR_SHORT)
	    	.setLongFlag(ARG_HEROES_DIR_LONG);    
	    opt2.setHelp("Directory where you have your hero XML files; XML files are searched in sub-directories recursively.");
	
	    jsap.registerParameter(opt2);
		    	
	    FlaggedOption opt3 = new FlaggedOption(ARG_RESULT_DIR_LONG)
	    	.setStringParser(JSAP.STRING_PARSER)
	    	.setRequired(false)
	    	.setDefault("./results")
	    	.setShortFlag(ARG_RESULT_DIR_SHORT)
	    	.setLongFlag(ARG_RESULT_DIR_LONG);    
	    opt3.setHelp("Directory where to output results, will be created if not exist.");
	    
	    jsap.registerParameter(opt3);
	    
	    FlaggedOption opt5 = new FlaggedOption(ARG_PLAYOUT_LIMIT_LONG)
		    	.setStringParser(JSAP.INTEGER_PARSER)
		    	.setRequired(false)
		    	.setDefault("-1")
		    	.setShortFlag(ARG_PLAYOUT_LIMIT_SHORT)
		    	.setLongFlag(ARG_PLAYOUT_LIMIT_LONG);    
	    opt5.setHelp("Limits the amount of playouts (== experiments == number of simulations executed), -1 == no limit; 0 == nothing will be evaluated, > 0 maximum this number of simulations will be carried out.");
	    
	    jsap.registerParameter(opt5);
	    
	    Switch opt4 = new Switch(ARG_VIS_CONSOLE_LONG)
	    	.setDefault("false")
	    	.setShortFlag(ARG_VIS_CONSOLE_SHORT)
	    	.setLongFlag(ARG_VIS_CONSOLE_LONG);    
	    opt4.setHelp("If specified, attaches VisConsole to all simulations outputting their progress to stdout.");
	    
	    jsap.registerParameter(opt4);
	    
	    Switch opt44 = new Switch(ARG_VIS_FILE_LONG)
		    	.setDefault("false")
		    	.setShortFlag(ARG_VIS_FILE_SHORT)
		    	.setLongFlag(ARG_VIS_FILE_LONG);    
		opt44.setHelp("If specified, attaches VisFill to all simulations outputting their zipped replay files into the result directory.");
		    
		jsap.registerParameter(opt44);
	    
	    FlaggedOption opt6 = new FlaggedOption(ARG_MAX_CORES_LONG)
				.setStringParser(JSAP.INTEGER_PARSER)
				.setRequired(false)
				.setDefault("-1")
				.setShortFlag(ARG_MAX_CORES_SHORT)
				.setLongFlag(ARG_MAX_CORES_LONG);
	    opt6.setHelp("Maximum number of CPU cores to use for experiments, -1 == all cores");

	    jsap.registerParameter(opt6);
	    
	    FlaggedOption opt7 = new FlaggedOption(ARG_TIMEOUT_MULTIPLIER_LONG)
				.setStringParser(JSAP.DOUBLE_PARSER)
				.setRequired(false)
				.setDefault("10")
				.setShortFlag(ARG_TIMEOUT_MULTIPLIER_SHORT)
				.setLongFlag(ARG_TIMEOUT_MULTIPLIER_LONG);
	    opt7.setHelp("Alters the timeout for the number of steps. Once an agent executes PARAM*dungeon.#rooms step without reaching the goal, the simulation will timeout. Minimum value is 0.1.");

	    jsap.registerParameter(opt7);
	    
   	}

	private static void readConfig(String[] args) {
		System.out.println("Parsing command arguments.");
		
		try {
	    	config = jsap.parse(args);
	    } catch (Exception e) {
	    	fail(e.getMessage());
	    	System.out.println("");
	    	e.printStackTrace();
	    	throw new RuntimeException("FAILURE!");
	    }
		
		if (!config.success()) {
			String error = "Invalid arguments specified.";
			Iterator errorIter = config.getErrorMessageIterator();
			if (!errorIter.hasNext()) {
				error += "\n-- No details given.";
			} else {
				while (errorIter.hasNext()) {
					error += "\n-- " + errorIter.next();
				}
			}
			fail(error);
    	}

		adventuresDir = config.getString(ARG_ADVENTURES_DIR_LONG);
		
		heroesDir = config.getString(ARG_HEROES_DIR_LONG);
		
		resultDir = config.getString(ARG_RESULT_DIR_LONG);
		
		playoutLimit = config.getInt(ARG_PLAYOUT_LIMIT_LONG);
		
		visConsole = config.getBoolean(ARG_VIS_CONSOLE_LONG);
		
		visFile = config.getBoolean(ARG_VIS_FILE_LONG);

		maxCores = config.getInt(ARG_MAX_CORES_LONG);
		
		timeoutMultiplier = config.getDouble(ARG_TIMEOUT_MULTIPLIER_LONG);
	}
	
	private static void sanityChecks() {
		System.out.println("Sanity checks...");
		
		adventuresDirFile = new File(adventuresDir);
		System.out.println("-- adventures dir: " + adventuresDir + " --> " + adventuresDirFile.getAbsolutePath());		
		if (!adventuresDirFile.exists()) {
			fail("Directory with adventures does not exist at '" + adventuresDir + "', resolved as: " + adventuresDirFile.getAbsolutePath());
		}
		if (!adventuresDirFile.isDirectory()) {
			fail("Directory with adventures is not a directory at '" + adventuresDir + "', resolved as: " + adventuresDirFile.getAbsolutePath());
		}
		System.out.println("---- adventures directory exists, ok");
		
		heroesDirFile = new File(heroesDir);
		System.out.println("-- heroes dir: " + heroesDir + " --> " + heroesDirFile.getAbsolutePath());		
		if (!heroesDirFile.exists()) {
			fail("Directory with heroes does not exist at '" + heroesDir + "', resolved as: " + heroesDirFile.getAbsolutePath());
		}
		if (!heroesDirFile.isDirectory()) {
			fail("Directory with heroes is not a directory at '" + heroesDir + "', resolved as: " + heroesDirFile.getAbsolutePath());
		}
		System.out.println("---- heroes directory exists, ok");
		
		resultDirFile = new File(resultDir);
		System.out.println("-- result dir: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		if (!resultDirFile.exists()) {
			System.out.println("---- result dir does not exist, creating!");
			resultDirFile.mkdirs();
		}
		if (!resultDirFile.exists()) {
			fail("Result dir does not exists. Parsed as: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		}
		if (!resultDirFile.isDirectory()) {
			fail("Result dir is not a directory. Parsed as: " + resultDir + " --> " + resultDirFile.getAbsolutePath());
		}
		System.out.println("---- result directory exists, ok");
		
		if (playoutLimit < -1) {
			System.out.println("-- playout limit set to negative number " + playoutLimit + ", normalizing to -1");
			playoutLimit = -1;
		}
		if (playoutLimit == -1) {
			System.out.println("-- playout limit is -1 => no limit, all heroes x adventures will be evaluated");
		} else
		if (playoutLimit == 0) {
			System.out.println("-- playout limit is 0 => no simulations will be executed");
		} else {
			System.out.println("-- playout limit is " + playoutLimit + ", only this number of simulations will be executed at max");
		}
		
		if (visConsole) {
			System.out.println("-- VisConsole specified; simulation progeress will be output");
		} else {
			System.out.println("-- VisConsole NOT specified; simulation progeress will NOT be output");
		}

		if (maxCores == -1) {
			System.out.println("-- using all available cores");
		} else {
			System.out.println("-- max-cores specified; using " + maxCores + "cores");
		}
		
		if (timeoutMultiplier < 0.1) {
			System.out.println("-- timeoutMultiplier specified as " + timeoutMultiplier + " < 0.1, which is too small, setting back to 0.1");
			timeoutMultiplier = 0.1;
		}
		
	    System.out.println("Sanity checks OK!");
	}
	
	// =====================
	// EXPERIMENT EVALUATION
	// =====================
	
	private static void evaluate() {
		
		System.out.println("Creating experiment evaluator config...");
		
		ExperimentEvaluatorConfig config = new ExperimentEvaluatorConfig();
		
		config.source.dir           = adventuresDirFile;
		config.heroAgents.dir       = heroesDirFile;
		config.target.dir           = resultDirFile;
		config.playoutLimit         = playoutLimit;
		config.consoleVisualization = visConsole;
		config.storeReplays         = visFile;
		config.maxCores				= maxCores;
		config.timeoutMultiplier    = timeoutMultiplier;
		
		System.out.println("Creating experiment evaluator...");
		
		ExperimentEvaluator evaluator = new ExperimentEvaluator(config);
		
		System.out.println("Running experiment evaluator...");
		
		long start = System.currentTimeMillis();
		
		try {
			evaluator.run();
		} catch (Exception e) {
			fail("Failed to run the evaluator.", e);
		}
		
		long time = System.currentTimeMillis() - start;
		System.out.println("EVALUATION TIME: " + time + "ms = " + ((double)time / (60*1000)) + "min");
	}
	
	// ==============
	// TEST ARGUMENTS
	// ==============
	
	public static String[] getTestArgs1() {
		return new String[] {
				  "-a", "./data/dungeons/dungeon-example"                              // directory with adventures
				, "-h", "./data/hero-agents"                                           // directory with heroes
				, "-r", "./results"                                                    // directory with results	
//				, "-l", "4"                                                            // limits maximum number of simulations, -1 == no limit
				, "-v"                                                                 // console visualization, if commented out, evaluator will not output simulation progresses
				, "-p"                                                                 // generate zipped replay files
				, "-t", "5"															   // timeout multiplier
		};
	}
	
	public static String[] getTestArgs2() {
		return new String[] {
				  "-a", "../DarkDungeon4J-Generator/result/adventures/2017/june/test"  // directory with adventures
				, "-h", "./data/hero-agents"                                           // directory with heroes
				, "-r", "./results"                                                    // directory with results	
//				, "-l", "4"                                                            // limits maximum number of simulations, -1 == no limit
//				, "-v"                                                                 // console visualization, if commented out, evaluator will not output simulation progresses
				, "-p"                                                                 // generate zipped replay files
				, "-t", "5"															   // timeout multiplier
		};
	}
	
	public static void main(String[] args) throws JSAPException {
		// -----------
		// FOR TESTING
		// -----------
		args = getTestArgs1();		

		// --------------
		// IMPLEMENTATION
		// --------------
		
		initJSAP();
	    
	    header();
	    
	    readConfig(args);
	    
	    sanityChecks();
	    
	    evaluate();
	    
	    System.out.println("---// FINISHED //---");
	    
	    System.exit(0);
	}

	

}
