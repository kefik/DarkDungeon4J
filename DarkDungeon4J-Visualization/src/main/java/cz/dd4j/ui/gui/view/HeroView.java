package cz.dd4j.ui.gui.view;

import cz.cuni.amis.clear2d.engine.components.CSprite;
import cz.cuni.amis.clear2d.engine.iface.IDrawable;
import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.cuni.amis.clear2d.engine.tween.TweenEase;
import cz.cuni.amis.clear2d.engine.tween.pos.ITweenPosCallback;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPosType;
import cz.dd4j.agents.IHeroAgent;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Hero;
import cz.dd4j.ui.gui.c2d.Ctx;

public class HeroView extends Entity {
	
	public CSprite sword;
	
	public AgentMindBody<Hero, IHeroAgent> hero;
	
	public CMovement movement;
	public CMovementCtrl movementCtrl;
	
	public CDeath deathAnim;
	
	public HeroView(AgentMindBody<Hero, IHeroAgent> hero, RoomsView roomsView) {
		this(hero, Ctx.texHero, Ctx.texSword, roomsView);
	}
	
	public HeroView(AgentMindBody<Hero, IHeroAgent> hero, IDrawable texHero, IDrawable texSword, RoomsView roomsView) {
		super(texHero);
		
		this.hero = hero;
		
		sword = new CSprite(this, texSword);
		sword.pos.x = 15;
		sword.pos.y = 3;
		sword.setEnabled(false);
		
		movement = new CMovement(this);
		
		movementCtrl = new CMovementCtrl(this, hero.body, roomsView, movement);	
		movementCtrl.roomViewX = 8;
		movementCtrl.roomViewY = 25;
		movementCtrl.corridorViewX = 0;
		movementCtrl.corridorViewY = 0;
		
		deathAnim = new CDeath(this);
	}
	
	public void animateDeath() {
		deathAnim.doIt();
		sword.setEnabled(false);
	}
	
	public TweenPos attack(final MonsterView entity) {
		TweenPos tween = new TweenPos(pos, entity.pos, TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
		tween.onPlayed = new ITweenPosCallback() {

			@Override
			public void run(TweenPos tween) {
				entity.animateDeath();
				tween.reverse();
			}
			
		};
		
		tween.play();
		
		return tween;
	}
	
	public TweenPos pickupSword(final RoomView room) {
		TweenPos tween = new TweenPos(pos, room.pos.add(room.sword.pos).add(-8, 5), TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
		
		tween.onPlayed = new ITweenPosCallback() {

			@Override
			public void run(TweenPos tween) {
				room.sword.setEnabled(false);
				sword.setEnabled(true);
				tween.reverse();
			}
			
		};
		
		tween.play();
		
		return tween;
	}
	
	public TweenPos dropSword(final RoomView room) {
		TweenPos tween = new TweenPos(pos, room.pos.add(room.sword.pos).add(-8, 5), TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
		
		tween.onPlayed = new ITweenPosCallback() {

			@Override
			public void run(TweenPos tween) {
				room.sword.setEnabled(true);
				sword.setEnabled(false);
				tween.reverse();
			}
			
		};
		
		tween.play();
		
		return tween;
	}
	
	public TweenPos attackTrap(final RoomView room) {
		TweenPos tween = new TweenPos(pos, room.pos.add(room.trap.pos), TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
		
		tween.onPlayed = new ITweenPosCallback() {

			@Override
			public void run(TweenPos tween) {
				room.setTrap(false);
				tween.reverse();
			}
			
		};
		
		tween.play();
		
		return tween;
	}
	
	@Override
	public void tick(C2DTime time) {
//		boolean alive = hero != null && hero.body != null && hero.body.alive;
//		this.cSprite.enabled = alive;
		
//		if (hero != null && hero.body != null) {
//			if (hero.body.hand != null && hero.body.hand.isA(EItem.SWORD)) {
//				sword.setEnabled(true);
//			} else {
//				sword.setEnabled(false);
//			}
//		}
	}
	

}
