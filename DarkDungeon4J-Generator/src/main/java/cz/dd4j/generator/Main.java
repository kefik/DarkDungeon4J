package cz.dd4j.generator;

import java.io.File;

import cz.cuni.amis.utils.simple_logging.SimpleLogging;
import cz.dd4j.agents.features.TrapAgent;
import cz.dd4j.agents.heroes.HeroRandom;
import cz.dd4j.agents.heroes.HeroRulesWithRandomMove;
import cz.dd4j.agents.heroes.HeroSemiRandom;
import cz.dd4j.agents.monsters.DynamicMonsterAgent;
import cz.dd4j.agents.monsters.KillerMonsterAgent;
import cz.dd4j.agents.monsters.StaticMonsterAgent;
import cz.dd4j.domain.DD4JDomainInit;
import cz.dd4j.loader.agents.impl.xml.AgentXML;
import cz.dd4j.utils.config.ConfigXML;

public class Main {

	public static void main(String[] args) {
		SimpleLogging.initLogging();
		
		DD4JDomainInit.init();
		
		GeneratorConfig config = new GeneratorConfig();
		config.targetDir = new File("result");
		
		Generator generator = new Generator(config);
		
		// Rooms 4-100
		//generator.generateRooms(4, 100);
		
		// Goals for rooms 1-100
		//generator.generateGoals(100);
		
		// Grids 2x2 -> 10x10
		//generator.generateGrid(4, 100);
			
		// Torus 2x2 -> 10x10
		//generator.generateTorus(4, 100);
		
		// Mazes
//		generator.generateMazes(
//				4, 4,   // 4x4
//				6, 6,   // up-to 6x6 
//				5,      // 5 random mazes for each dimension
//				3       // 0-3 extra junctions
//		);
		
		// Monsters 1-10 for rooms 1-100
		//generator.generateMonsters(10, 100);
		
		// Hero 1-1 for rooms 1-100		
		//generator.generateHeroes(1, 100);
		
		// Trap 1-10 for rooms 1-100
		//generator.generateTraps(10, 100);
		
		// Swords for rooms 1-100
		//generator.generateSwords(100);
		
		// Generate hero agents
//		generator.generateAgents(
//			"hero",   // agent id prefix
//			"heroes", // sub-directory
//			1,        // heroes count
//			new AgentXML("Random", HeroRandom.class.getName()),
//			new AgentXML("SemiRandom", HeroSemiRandom.class.getName()),
//			new AgentXML("RuleBased", HeroRulesWithRandomMove.class.getName())
//		);
		
		// Generate monster agents
//		generator.generateAgents(
//			"monster",  // agent id prefix
//			"monsters", // sub-directory
//			10,         // monsters count
//			new AgentXML("Static",     StaticMonsterAgent.class.getName()),
//			new AgentXML("Dynamic0",   DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.0d)),
//			new AgentXML("Dynamic10",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.1d)),
//			new AgentXML("Dynamic20",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.2d)),
//			new AgentXML("Dynamic30",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.3d)),
//			new AgentXML("Dynamic40",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.4d)),
//			new AgentXML("Dynamic50",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.5d)),
//			new AgentXML("Dynamic60",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.6d)),
//			new AgentXML("Dynamic70",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.7d)),
//			new AgentXML("Dynamic80",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.8d)),
//			new AgentXML("Dynamic90",  DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 0.9d)),
//			new AgentXML("Dynamic100", DynamicMonsterAgent.class.getName(), new ConfigXML("movementProbability", 1.0d)),
//			new AgentXML("Killer0",    KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.0d)),
//			new AgentXML("Killer10",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.1d)),
//			new AgentXML("Killer20",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.2d)),
//			new AgentXML("Killer30",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.3d)),
//			new AgentXML("Killer40",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.4d)),
//			new AgentXML("Killer50",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.5d)),
//			new AgentXML("Killer60",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.6d)),
//			new AgentXML("Killer70",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.7d)),
//			new AgentXML("Killer80",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.8d)),
//			new AgentXML("Killer90",   KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 0.9d)),
//			new AgentXML("Killer100",  KillerMonsterAgent.class.getName(),  new ConfigXML("movementProbability", 1.0d))
//		);
		
		// Generate trap agents
//		generator.generateAgents(
//			"trap",  // agent id prefix
//			"traps", // sub-directory
//			10,      // traps count
//			new AgentXML("Trap", TrapAgent.class.getName())
//		);
		
	}
	
}
