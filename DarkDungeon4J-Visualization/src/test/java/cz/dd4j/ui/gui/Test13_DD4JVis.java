package cz.dd4j.ui.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.domain.EItem;
import cz.dd4j.loader.agents.AgentsLoader;
import cz.dd4j.loader.simstate.SimStateLoader;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.agents.Agents;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.simulation.data.dungeon.elements.entities.features.Trap;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;
import cz.dd4j.simulation.data.state.SimState;
import cz.dd4j.ui.gui.c2d.Ctx;
import cz.dd4j.ui.gui.utils.BusyWait;
import cz.dd4j.ui.gui.utils.IWaiting;
import cz.dd4j.ui.gui.view.HeroView;
import cz.dd4j.ui.gui.view.MonsterView;
import cz.dd4j.ui.gui.view.RoomView;
import cz.dd4j.ui.gui.view.RoomsView;

public class Test13_DD4JVis {
	
	public static final Random random = new Random(1);

	public static void main(String[] args) {
		// STATE LOAD
		SimStateLoader loader = new SimStateLoader();
		SimState simState = loader.loadSimState(new File("./example/Adventure.xml"), true);
		
		// ENGINE INIT
		Ctx.init();
		
		// VIS INIT
		DD4JVis vis = new DD4JVis(simState, null);
		
		// MAKE VISIBLE
		vis.frame.setVisible(true);
		
		// NOW DO SOME MOVEMENTS!
		while (true) {
			int entitiesCount = simState.heroes.size() + simState.monsters.size() + simState.features.size();
			
			for (AgentMindBody<Feature, IFeatureAgent> trap : simState.features.values()) {
				if (!trap.body.alive || trap.body.atRoom.monster == null) {
					--entitiesCount;
				}
			}
			
			int entityNum = random.nextInt(entitiesCount);
			if (entityNum == 0) {
				// HERO MOVEMENT
				Hero heroBody = simState.heroes.values().iterator().next().body;
				
				if (heroBody.atRoom.feature != null && heroBody.atRoom.feature.alive && heroBody.hand != null && heroBody.atRoom.item == null) {
					makeHeroDropSword(heroBody, vis.heroViews.get(heroBody), vis.roomsView.getRoomView(heroBody.atRoom.id));
				} else
				if (heroBody.atRoom.feature != null && heroBody.atRoom.feature.alive && heroBody.hand == null) {
					makeHeroAttackTrap(heroBody, vis.heroViews.get(heroBody), vis.roomsView.getRoomView(heroBody.atRoom.id));
				} else
				if (heroBody.atRoom.item != null && heroBody.hand == null && heroBody.atRoom.item.isA(EItem.SWORD)) {
					makeHeroPickupSword(heroBody, vis.heroViews.get(heroBody), vis.roomsView.getRoomView(heroBody.atRoom.id));
				} else {					
					if (heroBody.atRoom.monster != null && heroBody.hand != null && heroBody.hand.isA(EItem.SWORD)) {
						makeHeroAttack(heroBody, vis.heroViews.get(heroBody), heroBody.atRoom.monster, vis.monsterViews.get(heroBody.atRoom.monster));
						simState.monsters.remove(heroBody.atRoom.monster.id);
						heroBody.atRoom.monster = null;
					} else
					if (heroBody.atRoom.item == null && heroBody.hand != null && heroBody.hand.isA(EItem.SWORD) && random.nextDouble() > 0.75) {
						makeHeroDropSword(heroBody, vis.heroViews.get(heroBody), vis.roomsView.getRoomView(heroBody.atRoom.id));
					} else {				
						makeHeroMove(heroBody, vis.heroViews.get(heroBody));
					}
				}
			} else {
				entityNum -= 1;
				if (entityNum < simState.monsters.size()) {
					// MONSTER MOVEMENT
					Monster body = ((AgentMindBody<Monster, IMonsterAgent>)(simState.monsters.values().toArray()[entityNum])).body;
					
					if (body.atRoom.hero != null) {
						Hero heroBody = simState.heroes.values().iterator().next().body;
						if (heroBody.hand != null && heroBody.hand.isA(EItem.SWORD)) {
							makeHeroAttack(heroBody, vis.heroViews.get(heroBody), heroBody.atRoom.monster, vis.monsterViews.get(heroBody.atRoom.monster));
							simState.monsters.remove(heroBody.atRoom.monster.id);
							heroBody.atRoom.monster = null;
						} else {
							makeMonsterMove(body, vis.monsterViews.get(body));
						}
					} else {
						makeMonsterMove(body, vis.monsterViews.get(body));
					}
				} else {
					entityNum -= simState.monsters.size();
					if (entityNum < simState.features.size()) {
						// TRAP MOVEMENT
						Trap trapBody = (Trap) ((AgentMindBody<Feature, IFeatureAgent>)(simState.features.values().toArray()[entityNum])).body;
						if (trapBody.alive && trapBody.atRoom.monster != null) {
							makeTrapAttack(trapBody, vis.roomsView.getRoomView(trapBody.atRoom.id), trapBody.atRoom.monster, vis.monsterViews.get(trapBody.atRoom.monster));
							simState.monsters.remove(trapBody.atRoom.monster.id);
							trapBody.atRoom.monster = null;
						}
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static void makeTrapAttack(Trap trapBody, RoomView roomView, Monster monster, MonsterView monsterView) {
		final TweenPos tween = roomView.trap.attack(monsterView);
		
		// BUSY WAITING
		new BusyWait( new IWaiting() {
				@Override
				public boolean isWaiting() { return tween.isTweening(); } 
			}
		).busyWait();
		
		// KILL MONSTER
		monster.alive = false;
	}

	private static void makeHeroAttackTrap(Hero heroBody, HeroView heroView, RoomView roomView) {
		final TweenPos tween = heroView.attackTrap(roomView);
		
		// BUSY WAITING
		new BusyWait( new IWaiting() {
				@Override
				public boolean isWaiting() { return tween.isTweening(); } 
			}
		).busyWait();
		

		// MAKE TRAP DEAD
		roomView.room.feature.alive = false;		
	}
	
	private static void makeHeroDropSword(Hero heroBody, HeroView heroView, RoomView roomView) {
		final TweenPos tween = heroView.dropSword(roomView);
		
		// BUSY WAITING
		new BusyWait( new IWaiting() {
				@Override
				public boolean isWaiting() { return tween.isTweening(); } 
			}
		).busyWait();
		

		// ADD SWORD FROM ROOM		
		roomView.room.item = heroBody.hand;		
		// REMOVE SWORD TO HERO'S HAND
		heroBody.hand = null;
	}

	private static void makeHeroPickupSword(Hero heroBody, HeroView heroView, RoomView roomView) {
		final TweenPos tween = heroView.pickupSword(roomView);
		
		// BUSY WAITING
		new BusyWait( new IWaiting() {
				@Override
				public boolean isWaiting() { return tween.isTweening(); } 
			}
		).busyWait();
		
		
		// ADD SWORD TO HERO'S HAND
		heroBody.hand = roomView.room.item;
		// REMOVE SWORD FROM ROOM		
		roomView.room.item = null;		
	}

	private static void makeHeroAttack(Hero body, HeroView heroView, Monster monster, MonsterView monsterView) {
		final TweenPos tween = heroView.attack(monsterView);
		
		// BUSY WAITING
		new BusyWait( new IWaiting() {
				@Override
				public boolean isWaiting() { return tween.isTweening(); } 
			}
		).busyWait();
		
		// MAKE THE MONSTER DEAD
		monster.alive = false;
	}

	private static void makeMonsterMove(Monster body, MonsterView monsterView) {
		Corridor corridor = null;
		for (int i = 0; i < 10 && corridor == null; ++i) {
			int corridorIndex = random.nextInt(body.atRoom.corridors.size());
			corridor  = body.atRoom.corridors.get(corridorIndex);
			if (corridor.monster != null || corridor.getOther(body.atRoom).monster != null) {
				corridor = null;
			}			
		}
		
		if (corridor == null) {
			// NO CORRIDOR TO TRAVEL
			return; 
		}
		
		Room fromRoom = body.atRoom;
		body.atRoom.monster = null;
		body.atRoom = null;
		body.atCorridor = corridor;
		body.atCorridor.monster = body;
		
		// BUSY WAITING
		while (monsterView.movementCtrl.currentCorridor != corridor) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		// FINISH THE MOVEMENT
		body.atRoom = corridor.getOther(fromRoom);
		body.atRoom.monster = body;
		body.atCorridor.monster = null;
		body.atCorridor = null;
		
		// BUSY WAITING
		while (monsterView.movementCtrl.currentRoom != body.atRoom) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}	
	}

	private static void makeHeroMove(Hero body, HeroView heroView) {
		Corridor corridor = null;
		for (int i = 0; i < 10 && corridor == null; ++i) {
			int corridorIndex = random.nextInt(body.atRoom.corridors.size());
			corridor  = body.atRoom.corridors.get(corridorIndex);
			if (corridor.hero != null || corridor.getOther(body.atRoom).hero != null) {
				corridor = null;
			}			
		}
		
		if (corridor == null) {
			// NO CORRIDOR TO TRAVEL
			return; 
		}
		
		Room fromRoom = body.atRoom;
		body.atRoom.hero = null;
		body.atRoom = null;
		body.atCorridor = corridor;
		body.atCorridor.hero = body;		
		
		// BUSY WAITING
		while (heroView.movementCtrl.currentCorridor != corridor) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		// FINISH THE MOVEMENT
		body.atRoom = corridor.getOther(fromRoom);
		body.atRoom.hero = body;
		body.atCorridor.hero = null;
		body.atCorridor = null;
		
		// BUSY WAITING
		while (heroView.movementCtrl.currentRoom != body.atRoom) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
}
