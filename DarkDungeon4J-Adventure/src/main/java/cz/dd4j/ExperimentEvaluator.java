package cz.dd4j;

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
import cz.dd4j.simulation.actions.instant.impl.*;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.simulation.result.SimResult;
import cz.dd4j.ui.console.VisConsole;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExperimentEvaluator {
	private String _resultsDir;
	private String _setupDir;

	public ExperimentEvaluator(String resultsDir, String setupDir) {
		this._resultsDir = resultsDir;
		_setupDir = setupDir;
	}

	public class WorkItem {
		File dungeonFile;
		File heroFile;

		WorkItem(File dungeonDir, File heroFile) {
			this.dungeonFile = dungeonDir;
			this.heroFile = heroFile;
		}

		public String toFileName() {
			return dungeonFile.getName() + "_" + heroFile.getName();
		}
	}

	public HashSet<String> loadProgress() {
		HashSet<String> results = new HashSet<String>();

		File[] files = resultFiles();

		for (File result : files) {
			results.add(result.getName());
		}

		return results;
	}

	private File resultsDir() {
		return new File(_resultsDir);
	}

	// TODO: generate the work items lazily.
	private ArrayList<WorkItem> generateWorkItems() {
		Tuple<File[], File[]> workItemFiles = loadWorkItemFiles();

		File[] dungeonDirs = workItemFiles.a;
		File[] heroFiles = workItemFiles.b;

		HashSet<String> currentProgress = loadProgress();

		ArrayList<WorkItem> items = new ArrayList<WorkItem>(dungeonDirs.length * heroFiles.length);

		for (File dungeonDir : dungeonDirs) {
			final File dungeonFile = new File(dungeonDir.getAbsolutePath(), dungeonDir.getName() + ".xml");

			for (File heroDir : heroFiles) {
				if (heroDir.toString().endsWith(".meta")) continue;

				final File heroFile = new File(heroDir.getAbsolutePath());

				WorkItem workItem = new WorkItem(dungeonFile, heroFile);

				if (!currentProgress.contains(workItem.toFileName())) {
					items.add(workItem);
				}
			}
		}

		return items;
	}

	public void runEvaluator() throws InterruptedException {
		if (!resultsDir().isDirectory()) {
			resultsDir().mkdir();
		}

		int numCores = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numCores);

		for (final WorkItem item : generateWorkItems()) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					SimResult result = playout(getSimStaticConfig(), item.dungeonFile, item.heroFile);

					System.out.println("Playout result " + result);
					try {
						saveResult(item, result);
					} catch (IOException e) {
						System.err.println("Error trying to save result " + item);
						e.printStackTrace();
					}
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);

		System.out.println("DONE");
	}

	private void saveResult(WorkItem item, SimResult result) throws IOException {
		File dir = resultsDir();

		String resultFileName = item.toFileName();

		File file = new File(dir, resultFileName);
		FileOutputStream writer = new FileOutputStream(file);

		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.toXML(result, writer);
	}

	private SimResult playout(SimStaticConfig config, File dungeonFile, File heroFile) {
		SimState simState = new SimStateLoader().loadSimState(dungeonFile);
		config.bindSimState(simState);

		Agents<IHeroAgent> heroes = new AgentsLoader<IHeroAgent>().loadAgents(heroFile);
		config.bindHeroes(heroes);

		if (!config.isReady()) {
			throw new RuntimeException("Configuration is not complete. " + config.getMissingInitDescription());
		}

		SimStatic simulation = new SimStatic(config);
		simulation.getEvents().addHandler(new VisConsole());

		return simulation.simulate();
	}


	public static SimStaticConfig getSimStaticConfig() {
		// CREATE ADVANTURE CONFIGURATION
		SimStaticConfig config = new SimStaticConfig();

		// SPECIFY ACTIONS TO USE
		IHeroInstantAction[] heroActions = new IHeroInstantAction[]{new HeroAttackInstant(), new HeroDisarmInstant(), new HeroDropInstant(),
				new HeroMoveInstant(), new HeroPickupInstant()};
		IMonsterInstantAction[] monsterActions = new IMonsterInstantAction[]{new MonsterMoveInstant(), new MonsterAttackInstant()};
		IFeatureInstantAction[] featureActions = new IFeatureInstantAction[]{new FeatureAttackInstant()};

		config.bindActions(EEntity.HERO, heroActions);
		config.bindActions(EEntity.MONSTER, monsterActions);
		config.bindActions(EEntity.FEATURE, featureActions);
		return config;
	}


	private File[] resultFiles() {
		File[] files = resultsDir().listFiles();

		if (files == null) {
			return new File[0];
		} else {
			return files;
		}
	}

	private Tuple<File[], File[]> loadWorkItemFiles() {
		File dungeonsParent = new File(_setupDir, "dungeons");
		final File[] dungeonDirs = dungeonsParent.listFiles();

		if (dungeonDirs == null) {
			throw new RuntimeException("Directory " + dungeonsParent.getAbsolutePath() + " doesn't exist.");
		}

		File heroesParent = new File(_setupDir, "heroes");
		final File[] heroDirs = heroesParent.listFiles();

		if (heroDirs == null) {
			throw new RuntimeException("Directory " + heroesParent.getAbsolutePath() + " doesn't exist.");
		}

		return new Tuple<File[], File[]>(dungeonDirs, heroDirs);
	}
}
