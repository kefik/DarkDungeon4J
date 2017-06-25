package cz.dd4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.domain.EEntity;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.SimStatic;
import cz.dd4j.simulation.SimStaticConfig;
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
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.ui.console.VisConsole;
import cz.dd4j.utils.ExceptionToString;
import cz.dd4j.utils.collection.Tuple2;
import cz.dd4j.utils.files.DirCrawler;
import cz.dd4j.utils.files.DirCrawlerCallback;

public class ExperimentEvaluator {
	
	public static class WorkItem {
		File adventureFile;
		File heroFile;

		WorkItem(File dungeonDir, File heroFile) {
			this.adventureFile = dungeonDir;
			this.heroFile = heroFile;
		}

		public String toFileName() {
			return adventureFile.getName() + "_" + heroFile.getName();
		}
		
		@Override
		public String toString() {
			return "WorkItem[adventure=" + adventureFile.getName() + ", hero=" + heroFile.getName() + "]";
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
		
		int numCores = Runtime.getRuntime().availableProcessors();
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
		// ENSURE INITIALIZATION
		init();
		
		// CRAWL DUNGEONS AND EXECUTE SIMULATIONS
		File dungeonDirs = config.source.getDir("adventures");
		DirCrawler.crawl(dungeonDirs, new DirCrawlerCallback() {
			
			@Override
			public void visitFile(File adventurefile) {
				if (!adventurefile.getName().endsWith(".xml")) return;
				for (File heroAgentFile : heroAgentsFiles) {
					WorkItem item = new WorkItem(adventurefile, heroAgentFile);
					runOnExecutor(item);
				}
			}
			
		});
						
		executor.shutdown();
		executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);

		System.out.println("DONE");
	}
	
	private synchronized void runOnExecutor(final WorkItem item) {
		if (config.playoutLimit == 0) return;
		if (config.playoutLimit > 0) {
			--config.playoutLimit;
		}
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					SimResult result = playout(getSimStaticConfig(), item);
					System.out.println("Playout result " + result);
					try {
						saveResult(item, result);
					} catch (IOException e) {
						System.out.println(ExceptionToString.process("Error trying to save result " + item, e));
					}
				} catch (Exception e) {
					System.out.println(ExceptionToString.process("FAILED TO RUN: " + item, e));
				}
			}
		});
	}
	
	private SimResult playout(SimStaticConfig config, WorkItem item) {
		SimState simState = new SimStateLoader().loadSimState(item.adventureFile);
		config.bindSimState(simState);

		Agents<IHeroAgent> heroes = new AgentsLoader<IHeroAgent>().loadAgents(item.heroFile);
		config.bindHeroes(heroes);

		if (!config.isReady()) {
			throw new RuntimeException("Configuration is not complete. " + config.getMissingInitDescription());
		}
		
		config.description = item.toString();
		
		config.state.roundsLeft = 5 * (int)Math.pow(config.state.dungeon.rooms.size(), 2); 
		
		SimStatic simulation = new SimStatic(config);
		simulation.getEvents().addHandler(new VisConsole());

		return simulation.simulate();
	}

	private void saveResult(WorkItem item, SimResult result) throws IOException {
		File file = config.target.getFile(item.toFileName());
		
		FileOutputStream writer = new FileOutputStream(file);

		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.toXML(result, writer);
	}

//	private File[] resultFiles() {
//		File[] files = config.target.dir.listFiles();
//
//		if (files == null) {
//			return new File[0];
//		} else {
//			return files;
//		}
//	}

	public static SimStaticConfig getSimStaticConfig() {
		// CREATE ADVANTURE CONFIGURATION
		SimStaticConfig config = new SimStaticConfig();

		// SPECIFY ACTIONS TO USE
		IHeroInstantAction[] heroActions = new IHeroInstantAction[]{
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
			config.playoutLimit = 20;
			
			config.source.dir = new File("../DarkDungeon4J-Generator/result");
			config.target.dir = new File("./result");
			
			// TODO: parse args to extract experiment & result dirs + continue/restart flag
			new ExperimentEvaluator(config).run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
