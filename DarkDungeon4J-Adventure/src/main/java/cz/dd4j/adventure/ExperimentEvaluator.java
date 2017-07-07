package cz.dd4j.adventure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.descriptor.DungeonDescriptor;
import cz.dd4j.descriptor.DungeonPaths;
import cz.dd4j.domain.EEntity;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
import cz.dd4j.simulation.SimStaticStats;
import cz.dd4j.simulation.actions.instant.IFeatureInstantAction;
import cz.dd4j.simulation.actions.instant.IHeroInstantAction;
import cz.dd4j.simulation.actions.instant.IMonsterInstantAction;
import cz.dd4j.simulation.actions.instant.impl.FeatureAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroDisarmInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroDropInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroMoveInstant;
import cz.dd4j.simulation.actions.instant.impl.HeroPickupInstant;
import cz.dd4j.simulation.actions.instant.impl.MonsterAttackInstant;
import cz.dd4j.simulation.actions.instant.impl.MonsterMoveInstant;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.utils.ExceptionToString;
import cz.dd4j.utils.csv.CSV.CSVRow;
import cz.dd4j.utils.files.DirCrawler;
import cz.dd4j.utils.files.DirCrawlerCallback;
import cz.dd4j.utils.reporting.IReporting;
import cz.dd4j.utils.reporting.Reporting;

public class ExperimentEvaluator {
	
	public static class Playout implements IReporting {
		
		private static AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);
		
		public final int number;
		
		public File adventureFile;
		public File heroFile;
		
		// ============================
		// TO BE FILLED BY THE EXECUTOR
		// ============================
		
		public SimStaticConfig config;
		public DungeonDescriptor descriptor;
		public SimStaticStats stats;
		
		Playout(File dungeonDir, File heroFile) {
			this.number = ATOMIC_INTEGER.incrementAndGet();			
			this.adventureFile = dungeonDir;
			this.heroFile = heroFile;
		}

		public String toFileName(String suffix) {
			return toId() + suffix;
		}
		
		@Override
		public String toString() {
			return "Playout[number=" + number + ", adventure=" + adventureFile.getName() + ", hero=" + heroFile.getName() + "]";
		}

		public String toId() {
			return "E" + number + "-" + adventureFile.getName().replaceAll("\\.xml", "") + "-" + heroFile.getName().replaceAll("\\.xml", "");
		}

		// =========
		// REPORTING
		// =========
		
		public static final String CSV_ID = "EXP-id";
		public static final String CSV_ADVENTURE = "EXP-adventure";
		public static final String CSV_HERO = "EXP-hero";
		
		@Override
		public List<String> getCSVHeaders() {
			List<String> result = new ArrayList<String>();
			
			result.add(CSV_ID);
			result.add(CSV_ADVENTURE);
			result.add(CSV_HERO);
			
			return result;
		}

		@Override
		public CSVRow getCSVRow() {
			CSVRow result = new CSVRow();
			
			result.add(CSV_ID, number);
			result.add(CSV_ADVENTURE, adventureFile.getName());
			result.add(CSV_HERO, heroFile.getName());
			
			return result;
		}
	}
	
	private ExperimentEvaluatorConfig config;
	
	private ExecutorService executor;
	
	private List<File> heroAgentsFiles;

	public ExperimentEvaluator(ExperimentEvaluatorConfig config) {
		this.config = config;
	}
	
	public synchronized void init() {
		if (executor != null) return;

		int numCores = config.maxCores;

		if (numCores == -1)
			numCores = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(numCores);
		
		heroAgentsFiles = findHeroAgentFiles();
		
		config.target.ensureDir();
	}
	
	private List<File> findHeroAgentFiles() {
		// LOAD HERO AGENTS
		final List<File> heroAgents = new ArrayList<File>();		
		File heroAgentsDir = config.heroAgents.dir;
		DirCrawler.crawl(heroAgentsDir, new DirCrawlerCallback() {
			
			@Override
			public void visitFile(File file) {
				if (file.getName().endsWith(".xml")) {
					heroAgents.add(file);
				}
			}
			
		});
		return heroAgents;
	}

//	public HashSet<String> loadProgress() {
//		HashSet<String> results = new HashSet<String>();
//
//		File[] files = resultFiles();
//
//		for (File result : files) {
//			results.add(result.getName());
//		}
//
//		return results;
//	}

	public void run() throws InterruptedException {
		System.out.println("EXPERIMENT EVALUATOR: RUNNING");
		
		// ENSURE INITIALIZATION
		init();
		
		// CRAWL DUNGEONS AND EXECUTE SIMULATIONS
		File dungeonDirs = config.source.dir;
		DirCrawler.crawl(dungeonDirs, new DirCrawlerCallback() {
			@Override
			public void visitFile(File adventurefile) {
				if (!adventurefile.getName().endsWith(".xml")) return;
				for (File heroAgentFile : heroAgentsFiles) {
					Playout item = new Playout(adventurefile, heroAgentFile);
					runOnExecutor(item);
				}
			}
		});
						
		System.out.println("EXPERIMENT EVALUATOR: FINISHED SUBMITTING TASKS, awaiting executor termination");
		
		executor.shutdown();
		executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
		executor = null;

		System.out.println("EXPERIMENT EVALUATOR: AGGREGATING RESULTS");
		
		ExperimentResultsAggregator aggregator = new ExperimentResultsAggregator(config.target.dir, config.target.dir);
		aggregator.aggregate();
		
		System.out.println("EXPERIMENT EVALUATOR: AGGREGATING RESULTS");		
	}
	
	private synchronized void runOnExecutor(final Playout item) {
		if (config.playoutLimit == 0) return;
		if (config.playoutLimit > 0) {
			--config.playoutLimit;
		}
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					item.stats = playout(getSimStaticConfig(), item);
					System.out.println(item.toString() + ": playout result " + item.stats.simulationResult.resultType + " in " + item.stats.simulationResult.frameNumber + " frame");
					if (item.stats.simulationResult.exception != null) {
						System.out.println(ExceptionToString.process(item.toString() + ": EXCEPTION!", item.stats.simulationResult.exception));
					}
					try {
						saveResult(item);
					} catch (IOException e) {
						System.out.println(ExceptionToString.process(item.toString() + ": error trying to save result", e));
					}
					System.out.println(item.toString() + ": DONE");
				} catch (Exception e) {
					System.out.println(ExceptionToString.process(item.toString() + ": FAILED TO RUN", e));
				}
			}
		});
	}
	
	private SimStaticStats playout(SimStaticConfig config, Playout item) {
		System.out.println(item.toString() + ": loading simulation state");
		
		// FINISH THE CONFIGURATION
		SimState simState = new SimStateLoader().loadSimState(item.adventureFile);
		config.bindSimState(simState);

		System.out.println(item.toString() + ": loading hero agent");
		
		Agents<IHeroAgent> heroes = new AgentsLoader<IHeroAgent>().loadAgents(item.heroFile);
		config.bindHeroes(heroes);

		config.id = "E" + item.number;
		config.description = item.toString();		
		config.state.roundsLeft = 10 * (int)Math.pow(config.state.dungeon.rooms.size(), 1);
		item.config = config;		
		System.out.println(item.toString() + ": timeout set to " + config.state.roundsLeft + " frames = 10*dungeon.rooms.size()^2 = 10*" + config.state.dungeon.rooms.size() + "^2");
		
		System.out.println(item.toString() + ": checking the configuration");
		if (!config.isReady()) {
			throw new RuntimeException("Configuration is not complete. " + config.getMissingInitDescription());
		}
		
		// CREATE DESCRIPTOR FOR THE DUNGEON
		System.out.println(item.toString() + ": creating dungeon descriptor");		
		DungeonDescriptor descriptor = DungeonDescriptor.describe(config.state.dungeon, DungeonPaths.ASTAR_NO_HEURISTIC);
		item.descriptor = descriptor;
		
		// FIRE THE SIMULATION		
		System.out.println(item.toString() + ": creating the simulation");		
		SimStatic simulation = new SimStatic(config);
		
		if (this.config.consoleVisualization) {
			System.out.println(item.toString() + ": attaching VisConsole to the simulation");
			VisConsole visConsole = new VisConsole();
			visConsole.outputPrefix = item.toString() + " -> ";
			simulation.getEvents().addHandler(visConsole);
		}
		
		System.out.println(item.toString() + ": running the simulation");
		
		// SAVE THE SIMULATION RESULT
		item.stats = simulation.simulate();
		
		return item.stats;
	}

	private void saveResult(Playout item) throws IOException {
		System.out.println(item.toString() + ": saving the results");
		
		File resultFile = config.target.getFile(item.toFileName("-result.csv"));
		new Reporting(item, item.stats.config, item.stats.simulationResult, item.config.state.heroes.values().iterator().next().mind).reportToFile(resultFile);
		
		File descriptorFile = config.target.getFile(item.toFileName("-dungeon_descriptor.csv"));
		new Reporting(item, item.descriptor).reportToFile(descriptorFile);
		
		File statsFile = config.target.getFile(item.toFileName("-stats.csv"));
		new Reporting(item, item.stats).reportToFile(statsFile);
	}

	public static SimStaticConfig getSimStaticConfig() {
		// CREATE ADVANTURE CONFIGURATION
		SimStaticConfig config = new SimStaticConfig();

		// SPECIFY ACTIONS TO USE
		IHeroInstantAction[] heroActions = new IHeroInstantAction[] {
											   new HeroAttackInstant(), new HeroDisarmInstant(), new HeroDropInstant(),
											   new HeroMoveInstant(), new HeroPickupInstant()
										   };
		IMonsterInstantAction[] monsterActions = new IMonsterInstantAction[]{new MonsterMoveInstant(), new MonsterAttackInstant()};
		IFeatureInstantAction[] featureActions = new IFeatureInstantAction[]{new FeatureAttackInstant()};

		config.bindActions(EEntity.HERO, heroActions);
		config.bindActions(EEntity.MONSTER, monsterActions);
		config.bindActions(EEntity.FEATURE, featureActions);
		
		return config;
	}
	
	public static void main(String[] args) {
		try {
			ExperimentEvaluatorConfig config = new ExperimentEvaluatorConfig();
			
			config.source.dir     = new File("../DarkDungeon4J-Generator/result/adventures/2017/june/test");
			config.heroAgents.dir = new File("./data/hero-agents");
			config.target.dir     = new File("./result");
			
			config.playoutLimit = 4;
			config.consoleVisualization = true;
			
			// TODO: parse args to extract experiment & result dirs + continue/restart flag
			new ExperimentEvaluator(config).run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
