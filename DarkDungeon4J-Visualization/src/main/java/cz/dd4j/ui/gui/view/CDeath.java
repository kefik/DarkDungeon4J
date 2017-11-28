package cz.dd4j.ui.gui.view;

import cz.cuni.amis.clear2d.engine.components.CSprite;
import cz.cuni.amis.clear2d.engine.components.CTick;
import cz.cuni.amis.clear2d.engine.prefabs.Entity;
import cz.cuni.amis.clear2d.engine.textures.Subtexture;
import cz.cuni.amis.clear2d.engine.textures.Texture;
import cz.cuni.amis.clear2d.engine.time.C2DTime;

public class CDeath extends CTick {
	
	public Subtexture[] subtextures;
	
	public CSprite[] subsprites;
	
	public boolean animating = false;
	
	public float timeLeft = 0.3f;
	
	public float speed = 50;
	
	/**
	 * @param owner initial owner the component; if null, does nothing
	 */
	public CDeath(Entity owner) {
		super(owner);
	}
	
	public void doIt() {
		
		((Entity)owner).cSprite.setEnabled(false);
		
		subtextures = new Subtexture[4];
		
		Texture texture = ((Texture)((Entity)owner).cSprite.texture);
		
		subtextures[0] = new Subtexture(texture, 0, 0, texture.getWidth() / 2, texture.getHeight() / 2);  
		subtextures[1] = new Subtexture(texture, texture.getWidth() / 2 + 1, 0, texture.getWidth(), texture.getHeight() / 2);
		subtextures[2] = new Subtexture(texture, texture.getWidth() / 2 + 1, texture.getHeight() / 2 + 1, texture.getWidth(), texture.getHeight());
		subtextures[3] = new Subtexture(texture, 0, texture.getHeight() / 2 + 1, texture.getWidth() / 2, texture.getHeight());
		
		subsprites = new CSprite[4];
		
		subsprites[0] = new CSprite(owner, subtextures[0], 0, 0);
		subsprites[1] = new CSprite(owner, subtextures[1], texture.getWidth() / 2 + 1, 0);
		subsprites[2] = new CSprite(owner, subtextures[2], texture.getWidth() / 2 + 1, texture.getHeight() / 2 + 1);
		subsprites[3] = new CSprite(owner, subtextures[3], 0, texture.getHeight() / 2 + 1);
				
		animating = true;
		timeLeft = 0.5f;
	}
	
	@Override
	public void tick(C2DTime time) {
		if (!animating) return;
		
		subsprites[0].pos.inAdd( -speed * time.game.delta, -speed * time.game.delta);
		subsprites[1].pos.inAdd(  speed * time.game.delta, -speed * time.game.delta);
		subsprites[2].pos.inAdd(  speed * time.game.delta,  speed * time.game.delta);
		subsprites[3].pos.inAdd( -speed * time.game.delta,  speed * time.game.delta);
		
		timeLeft -= time.game.delta;
		
		animating = timeLeft > 0;
		
		if (!animating) {
			for (CSprite subsprite : subsprites) subsprite.setEnabled(false);
		}
	}
	
}
