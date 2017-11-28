package cz.dd4j.utils.sprites;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;

public class SpriteSheetSplitter {
	
	public static void split(File spriteSheet, int startX, int startY, int tileWidth, int tileHeight, Color transparent, File dirOut) {
		
		BufferedImage sprites;
		try {
			sprites = ImageIO.read(spriteSheet);
		} catch (IOException e) {
			throw new RuntimeException("Fail to read spritesheet from: " + spriteSheet.getAbsolutePath(), e);
		}
		
		int w = sprites.getWidth();
		int h = sprites.getHeight();
		
		int spriteSheetX = startX;
		int spriteSheetY = startY;
		
		int spriteNum = 0;
		
		dirOut.mkdirs();
		
		while (spriteSheetX < w && spriteSheetY < h) {
			
			BufferedImage sprite = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_4BYTE_ABGR);
			
			for (int spriteX = 0; spriteX < tileWidth; ++spriteX) {
				for (int spriteY = 0; spriteY < tileHeight; ++spriteY) {
					int rgb;
					
					if (spriteSheetX + spriteX < w && spriteSheetY + spriteY < h) {
						rgb = sprites.getRGB(spriteSheetX + spriteX, spriteSheetY + spriteY);
					} else {
						rgb = 0; // transparent black
					}

					if (transparent != null) {
						int alpha = (rgb >> 24) & 0xFF;
						int red =   (rgb >> 16) & 0xFF;
						int green = (rgb >>  8) & 0xFF;
						int blue =  (rgb      ) & 0xFF;
						
						//System.out.println("A" + alpha + "R" + red + "G" + green + "B" + blue);
						
						if (red == transparent.getRed() && green == transparent.getGreen() && blue == transparent.getBlue()) {
							alpha = 0;
						}
						
						rgb = alpha << 24 | red << 16 | green << 8 | blue;
					}
					
					sprite.setRGB(spriteX, spriteY, rgb);
				}
			}
			
			File spriteFile = new File(dirOut, spriteNum + ".png");
			try {				
				ImageIO.write(sprite, "png", spriteFile);
			} catch (IOException e) {
				throw new RuntimeException("Failed to save a sprite into: " + spriteFile.getAbsolutePath());
			}
			
			++spriteNum;
			
			// ADVANCE SPRITESHEET
			spriteSheetX += tileWidth;			
			if (spriteSheetX >= w) {
				spriteSheetX = startX;
				spriteSheetY += tileHeight;
			}
		}
		
	}
	
	public static void main(String[] args) {
		File spriteSheet = new File("d:/Workspaces/MFF/GACR/GIT-DD4J/__GRAPHICS/16x16-Indoor/tilesetformattedupdate1.png");
		
		File targetDir = new File("d:/Workspaces/MFF/GACR/GIT-DD4J/__GRAPHICS/16x16-Indoor/tiles/");
		
		int transparentRed = 255;
		int transparentGreen = 0;
		int transparentBlue = 255;
		
		float[] hsb = new float[3];
		Color.RGBtoHSB(transparentRed, transparentGreen, transparentBlue, hsb);
		
		SpriteSheetSplitter.split(spriteSheet,
								  0,      // startX
				                  1,      // startY
								  16, 16, // tileWidth x tileHeight
								  Color.getHSBColor(hsb[0], hsb[1], hsb[2]), // transparent color 
								  targetDir);
		
		System.out.println("---/// DONE ///---");	
	}
	
}
