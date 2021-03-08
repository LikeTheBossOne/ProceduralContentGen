package main.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main.Application;
import processing.core.PImage;

public class SpriteSheet {
	private final List<PImage> sprites;
	
	public SpriteSheet(File f, int xCount, int yCount) {
		this.sprites = new ArrayList<PImage>();
		
		try {
			BufferedImage sheet = ImageIO.read(f);
			int subImageWidth = sheet.getWidth() / xCount;
			int subImageHeight = sheet.getHeight() / yCount;
			
			for (int y = 0; y < yCount; y++) {
				for (int x = 0; x < xCount; x++) {
					BufferedImage subImage = sheet.getSubimage(x * subImageWidth, y * subImageHeight, subImageWidth, subImageHeight);
					
					BufferedImage scaled = new BufferedImage(Application.TILE_SIZE, Application.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale(Application.TILE_SIZE/(double)subImageWidth, Application.TILE_SIZE/(double)subImageHeight);
					AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					scaled = scaleOp.filter(subImage, scaled);
					
					sprites.add(new PImage(scaled));
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public PImage getSprite(int index) {
		return sprites.get(index);
	}
	
}
