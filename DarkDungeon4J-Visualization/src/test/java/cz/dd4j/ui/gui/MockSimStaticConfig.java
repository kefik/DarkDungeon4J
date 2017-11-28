package cz.dd4j.ui.gui;

import cz.dd4j.domain.EEntity;
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

public class MockSimStaticConfig {

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
	
}
