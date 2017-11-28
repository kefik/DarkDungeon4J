package cz.dd4j.ui.gui.view;

import cz.cuni.amis.clear2d.engine.components.CSprite;
import cz.cuni.amis.clear2d.engine.iface.IDrawable;
import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.cuni.amis.clear2d.engine.tween.TweenEase;
import cz.cuni.amis.clear2d.engine.tween.pos.ITweenPosCallback;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPos;
import cz.cuni.amis.clear2d.engine.tween.pos.TweenPosType;
import cz.dd4j.agents.IFeatureAgent;
import cz.dd4j.simulation.data.agents.AgentMindBody;
import cz.dd4j.simulation.data.dungeon.elements.entities.Feature;
import cz.dd4j.ui.gui.c2d.Ctx;

public class TrapView extends Entity {
	
	public AgentMindBody<Feature, IFeatureAgent> trap;
	
	private IDrawable texTrap;

	private IDrawable texTrapDestroyed;
	
	private RoomsView roomsView;
	
	public TrapView(AgentMindBody<Feature, IFeatureAgent> trap, RoomsView roomsView) {
		this(trap, Ctx.texTrap, Ctx.texTrapDestroyed,  roomsView);
	}
	
	public TrapView(AgentMindBody<Feature, IFeatureAgent> trap, IDrawable texTrap, IDrawable texTrapDestroyed, RoomsView roomsView) {
		super(texTrap);
		
		this.trap = trap;
		
		this.texTrap = texTrap;
		this.texTrapDestroyed = texTrapDestroyed;
		
		this.roomsView = roomsView;
	}
	
	public void makeDestroyed() {
		cSprite.texture = texTrapDestroyed;
	}
	
	public TweenPos attack(final MonsterView entity) {
		TweenPos tween = new TweenPos(pos, parent.getLocalPos(entity.getGlobalPos()), TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
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
	
	public TweenPos attack(final HeroView entity) {
		TweenPos tween = new TweenPos(pos, parent.getLocalPos(entity.getGlobalPos()), TweenPosType.TIME, 0.3f, TweenEase.LINEAR);
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
	
	@Override
	public void tick(C2DTime time) {
	}
	

}
