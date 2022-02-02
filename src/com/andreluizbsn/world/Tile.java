package com.andreluizbsn.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16,16);
	public static BufferedImage TILE_FLOOR_STONE = Game.spritesheet.getSprite(128, 16, 16,16);

	public static BufferedImage TILE_FLOOR_STONE_R = Game.spritesheet.getSprite(64, 32, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_L = Game.spritesheet.getSprite(80, 32, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_U = Game.spritesheet.getSprite(96, 32, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_D = Game.spritesheet.getSprite(112, 32, 16,16);
	
	public static BufferedImage TILE_FLOOR_STONE_R_D = Game.spritesheet.getSprite(0, 48, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_L_U = Game.spritesheet.getSprite(16, 48, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_R_U = Game.spritesheet.getSprite(32, 48, 16,16);
	public static BufferedImage TILE_FLOOR_STONE_L_D = Game.spritesheet.getSprite(48, 48, 16,16);
	
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16,16);
	public static BufferedImage TILE_WALL_1 = Game.spritesheet.getSprite(112, 16, 16,16);
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile (int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite; 
	}
	
	public void render( Graphics g ) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
