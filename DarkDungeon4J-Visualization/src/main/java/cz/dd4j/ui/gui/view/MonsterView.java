package cz.dd4j.ui.gui.view;

import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.textures.Texture;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.cuni.amis.clear2d.engine.tween.TweenEase;
import cz.cuni.amis.clear2d.engine.tween.pos.ITweenPosCallback;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPosType;
import cz.dd4j.agents.IMonsterAgent;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Monster;
import cz.dd4j.ui.gui.c2d.Ctx;

public class MonsterView extends Entity<Texture> {
	
	private Texture texMonster;
	
	private CMovement movement;
	
	public CMovementCtrl movementCtrl;
	
	public AgentMindBody<Monster, IMonsterAgent> monster;
	
	public CDeath deathAnim;
	
	public MonsterView(AgentMindBody<Monster, IMonsterAgent> monster, RoomsView roomsView) {
		this(monster, Ctx.texMonster, roomsView);		
	}

	public MonsterView(AgentMindBody<Monster, IMonsterAgent> monster, Texture texMonster, RoomsView roomsView) {
		super(texMonster);
		this.monster = monster;
		this.texMonster = texMonster;
		
		movement = new CMovement(this);
		
		movementCtrl = new CMovementCtrl(this, monster.body, roomsView, movement);	
		movementCtrl.roomViewX = 48;
		movementCtrl.roomViewY = 8;
		movementCtrl.corridorViewX = 0;
		movementCtrl.corridorViewY = 0;
		
		deathAnim = new CDeath(this);
	}
	
	public TweenPos attack(final HeroView entity) {
		TweenPos tween = new TweenPos(pos, entity.pos, TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
		tween.play();
		
		tween.onPlayed = new ITweenPosCallback() {

			@Override
			public void run(TweenPos tween) {
				entity.animateDeath();
				tween.reverse();
			}
			
		};
		
		return tween;
	}
	
	public void animateDeath() {
		deathAnim.doIt();
	}
	
	@Override
	public void tick(C2DTime time) {
//		boolean alive = monster != null && monster.body != null && monster.body.alive;
//		this.cSprite.enabled = alive;
	}

}
