package main;

import java.io.File;

import main.graphics.SpriteSheet;
import main.map.FloorMap;
import processing.core.PApplet;

public class Application extends PApplet {
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	public static final int TILE_COUNT_X = 40;
	public static final int TILE_COUNT_Y = HEIGHT * TILE_COUNT_X / WIDTH;
	
	public static final int TILE_SIZE = 16;
	
	private FloorMap map;
	
	public static void main(String[] args) {
		PApplet.main("main.Application");
	}
	
	public void settings() {
		size(WIDTH, HEIGHT);
	}
	
	public void setup() {
		background(0);
		SpriteSheet mapSheet = new SpriteSheet(new File("./assets/overworld.png"), 12, 4);
		map = new FloorMap(this, TILE_COUNT_X, TILE_COUNT_Y, mapSheet);
		
		
		// map.generateRandomMap();
		map.generateMap(-1, 100, 4, 8, 3, 0.15, 0.03);
		
		
		map.draw();
		
		
	}
	
	public void draw() {
	}
}
