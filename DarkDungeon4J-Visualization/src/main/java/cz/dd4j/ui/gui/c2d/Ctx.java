package cz.dd4j.ui.gui.c2d;

import java.io.InputStream;

import cz.cuni.amis.clear2d.Clear2D;
import cz.cuni.amis.clear2d.Clear2DConfig;
import cz.cuni.amis.clear2d.engine.fonts.C2DFonts;
import cz.cuni.amis.clear2d.engine.iface.IDrawable;
import cz.cuni.amis.clear2d.engine.textures.Subtexture;
import cz.cuni.amis.clear2d.engine.textures.Texture;
import cz.cuni.amis.clear2d.engine.textures.TextureAtlas;
import cz.cuni.amis.clear2d.engine.textures.TextureAtlasResource;

public class Ctx {

	public static Clear2D engine;
	
	public static TextureAtlas taIndoor;
	
	public static Texture texMonster;
	
	public static Texture texHero;
	
	public static Texture texTrap;
	public static Texture texTrapDestroyed;
	
	public static Texture texSword;
	
	public static Subtexture subtexExit;
	
	public static void init() {
	
		Clear2DConfig cfg = new Clear2DConfig();
		cfg.fps = 30;
				
		InputStream xmlStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/16x16-Indoor/sprites.xml");
		taIndoor = new TextureAtlasResource(xmlStream, "cz/dd4j/ui/gui/16x16-Indoor/");
		try { xmlStream.close(); } catch (Exception e) {}
		
		InputStream heroPngStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/TomeTik/thief.png");
		texHero = new Texture(heroPngStream);
		try { heroPngStream.close(); } catch (Exception e) {}
		
		InputStream monsterPngStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/TomeTik/painkiller.png");
		texMonster = new Texture(monsterPngStream);
		try { monsterPngStream.close(); } catch (Exception e) {}
		
		InputStream trapPngStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/CrawlTiles/dngn_trap_arrow.png");
		texTrap = new Texture(trapPngStream);
		try { trapPngStream.close(); } catch (Exception e) {}
		
		InputStream trapDestPngStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/CrawlTiles/dngn_trap_arrow_destroyed.png");
		texTrapDestroyed = new Texture(trapDestPngStream);
		try { trapDestPngStream.close(); } catch (Exception e) {}
		
		InputStream swordPngStream = Ctx.class.getClassLoader().getResourceAsStream("cz/dd4j/ui/gui/ScimitarSword/Scimitar2.png");
		texSword = new Texture(swordPngStream);
		try { swordPngStream.close(); } catch (Exception e) {}
		
		subtexExit = taIndoor.getSubtexture(TileIndoor.Exit_png.texture);
		
		C2DFonts.init();
		
		engine = Clear2D.engine;		
		engine.start(cfg);
		
	}
	
	public static Subtexture tileIndoor(TileIndoor tile) {
		return taIndoor.getSubtexture(tile.texture);
	}
	
}
