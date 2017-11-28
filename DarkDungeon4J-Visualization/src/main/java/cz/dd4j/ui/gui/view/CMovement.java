package cz.dd4j.ui.gui.view;

import java.util.Random;

import cz.cuni.amis.clear2d.engine.SceneElement;
import cz.cuni.amis.clear2d.engine.components.CTick;
import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.time.C2DTime;
import cz.dd4j.simulation.data.dungeon.elements.places.Corridor;
import cz.dd4j.simulation.data.dungeon.elements.places.Room;

public class CMovement extends CTick {
	
	private static Random random = new Random(System.currentTimeMillis());
	
	public float targetX;
	
	public float targetY;
		
	public boolean animating = false;
	
	public float speedPerSec = 10;
	
	private int animatingComponents = 0;
	
	/**
	 * @param owner initial owner the component; if null, does nothing
	 */
	public CMovement(Entity owner) {
		super(owner);
	} 
	
	public void setTarget(float newTargetX, float newTargetY) {
		targetX = newTargetX;
		targetY = newTargetY;
		
		int animatingComponents = 0;
		
		if (Math.abs(owner.pos.x - targetX) < 1) {
			owner.pos.x = targetX;
		} else {
			++animatingComponents;
		}
		if (Math.abs(owner.pos.y - targetY) < 1) {
			owner.pos.y = targetY;
		} else {
			++animatingComponents;
		}
				
		animating = animatingComponents > 0;		
	}
	
	@Override
	public void tick(C2DTime time) {
		if (!animating) return;
		
		// for ref (x in [owner.x, owner.y]) 
		
		int animatingComponents = 0;
		
		if (Math.abs(owner.pos.x - targetX) < 1) {
			owner.pos.x = targetX;
		} else {
			owner.pos.x += (targetX - owner.pos.x) * Math.min(1, speedPerSec * time.game.delta);
			++animatingComponents;
		}
		if (Math.abs(owner.pos.y - targetY) < 1) {
			owner.pos.y = targetY;
		} else {
			owner.pos.y += (targetY - owner.pos.y) * Math.min(1, speedPerSec * time.game.delta);
			++animatingComponents;
		}
		
		if (animatingComponents == 0) {
			animating = false;
		}
	}

}
