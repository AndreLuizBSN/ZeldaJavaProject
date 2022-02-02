package com.andreluizbsn.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.andreluizbsn.entities.Enemy;
import com.andreluizbsn.entities.Entity;
import com.andreluizbsn.entities.FinalDoor;
import com.andreluizbsn.entities.Player;
import com.andreluizbsn.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 16;
	
	public int[] playerPosition = new int[2];
	public int[] weapom = new int[2];
	public int[] finalDoor = new int[3];
	public ArrayList< int[] > enemyPositions = new ArrayList< int[] >();
	public ArrayList< int[] > lifePacks = new ArrayList< int[] >();
	public ArrayList< int[] > emunition = new ArrayList< int[] >();
	
	private boolean isRandMap = false;
	
	public World ( String path ) {
		if ( isRandMap ) {
			randMap();
		} else {
			try {
				BufferedImage map = ImageIO.read( getClass().getResource(path) );
				int[] pixels = new int[map.getWidth() * map.getHeight()];
				tiles = new Tile[ map.getWidth() * map.getHeight() ];
				WIDTH = map.getWidth();
				HEIGHT = map.getHeight();
				int posicaoTile;
				map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
				for ( int xx = 0; xx < map.getWidth(); xx++ ) {
					for ( int yy = 0; yy < map.getHeight(); yy++ ) {
						
						posicaoTile = xx + ( yy * WIDTH);
						
						switch ( pixels[ posicaoTile ] ) {
							case 0xFF000000://chao
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0xFF7F3300://chao de terra
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE);
								break;
							case 0xFF4CFF00://terra/grama R
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_R);
								break;
							case 0xFF267F00://terra/grama L
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_L);
								break;
							case 0xFFB200FF://terra/grama D
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_D);
								break;
							case 0xFF57007F://terra/grama U
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_U);
								break;
							case 0xFF4800FF://terra/grama R - U
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_R_U);
								break;
							case 0xFF00137F://terra/grama L - U
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR_STONE_L_U);
								break;
							case 0xFFFFFFFF://parede
								tiles[ posicaoTile ] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
								break;
							case 0xFF7F0000://parede
								tiles[ posicaoTile ] = new WallTile(xx*16, yy*16, Tile.TILE_WALL_1);
								break;
							case 0XFF0026FF://player
								playerPosition[0] = xx * 16; 
								playerPosition[1] = yy * 16; 
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0xFFFF0000://enemy
								int[] enemyPos = new int[2];
								enemyPos[0] = xx * 16;
								enemyPos[1] = yy * 16;
								enemyPositions.add( enemyPos );
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0xFFFF00DC://life pack
								int[] lifePack = new int[3];
								lifePack[0] = xx * 16;
								lifePack[1] = yy * 16;
								lifePack[2] = 10;//Quantidade que retoma
								lifePacks.add( lifePack );
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0XFFFFD800://munição
								int[] municao = new int[3];
								municao[0] = xx * 16;
								municao[1] = yy * 16;
								municao[2] = 5;
								emunition.add( municao );
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0XFFFF6A00://weapom
								weapom[0] = xx * 16; 
								weapom[1] = yy * 16; 
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							case 0XFF00FF21://Final Door
								finalDoor[0] = xx * 16; 
								finalDoor[1] = yy * 16; 
								finalDoor[2] = 1; 
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
							default:
								tiles[ posicaoTile ] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
								break;
						}
					}
				}
				
			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void randMap() {
		playerPosition[0] = 0;
		playerPosition[1] = 0;
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH * HEIGHT];
		
		for ( int i = 0; i < WIDTH; i++ ) {
			for ( int j = 0; j < HEIGHT; j++ ) {
				tiles[i + j * WIDTH] = new WallTile(i*16, j*16, Tile.TILE_WALL);
			}
		}
		
		int dir = 0, xx = 0, yy = 0;
		
		for ( int i = 0; i < 200; i++ ) {
			
			tiles[xx + yy * WIDTH] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
			
			if ( dir == 0 ) {
				//direita
				if ( xx < WIDTH )
					xx++;
			} else if ( dir == 1 ) {
				//esquerda
				if ( xx > 0 )
					xx--;
			} else if ( dir == 2 ) {
				//baixo
				if ( yy < HEIGHT )
					yy++;
			} else if ( dir == 3 ) {
				//cima
				if ( yy > 0 )
					yy--;
			}
			
			if ( Game.rand.nextInt(100) < 30 ) {
				dir = Game.rand.nextInt(4);
			}
			
		}
				
				
	}
	
	public void render ( Graphics g ) {
		
		for ( int xx = 0; xx < WIDTH; xx++ ) {
			for ( int yy = 0; yy < HEIGHT; yy++ ) {
				Tile tile = tiles[xx + ( yy * WIDTH)];
				if ( tile != null )
					tile.render(g);
			}
		}
	}
	
	public int[] getPlayerPosition () {
		return playerPosition;
	}
	
	public ArrayList<int[]> getEnemyPositions () {
		return enemyPositions;
	}
	
	public ArrayList<int[]> getLifePacks () {
		return lifePacks;
	}
	
	public ArrayList<int[]> getEmunition () {
		return emunition;
	}
	
	public int[] getWeapom () {
		return weapom;
	}
	
	public int[] getFinalDoor () {
		return finalDoor;
	}
	
	public static boolean isFree ( int xnext, int ynext, Entity e ) {
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		try {
			if ( !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
					tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
					tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
					tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile) ) {
				return true;
			}
		} catch(Exception ex) {
			Game.state = "GAME_OVER";
			return false;
		}
		
		if ( e instanceof Player ) {
		
			if ( Game.player.getZ() > 0 ) {
				return true;
			}
			
		}
		
		
		return false;
	}

	public static void renderMiniMap () {
		for ( int i = 0; i < Game.minimapPixels.length; i++ ) {
			Game.minimapPixels[i] = 0;
		}
		
		for ( int xx = 0; xx < WIDTH; xx++ ) {
			for ( int yy = 0; yy < HEIGHT; yy++ ) {
				if ( tiles[xx + (yy * WIDTH)] instanceof WallTile ) {
					Game.minimapPixels[xx + (yy * WIDTH)] = 0x7F3300 ;
				}
			}
		}
		
		int xPlayer = (int) Game.player.getX() / 16;
		int yPlayer = (int) Game.player.getY() / 16;
		
		Game.minimapPixels[xPlayer + (yPlayer * WIDTH)] = 0x0026FF ;
		
		int xEnt, yEnt;
		
		for ( int i = 0; i < Game.entities.size(); i++ ) {
			if ( Game.entities.get(i) instanceof Enemy ) {
				xEnt = (int) Game.entities.get(i).getX() / 16;
				yEnt = (int) Game.entities.get(i).getY() / 16;
				Game.minimapPixels[xEnt + (yEnt * WIDTH)] = 0xFF0000 ;
			} else if ( Game.entities.get(i) instanceof FinalDoor ) {
				xEnt = (int) Game.entities.get(i).getX() / 16;
				yEnt = (int) Game.entities.get(i).getY() / 16;
				Game.minimapPixels[xEnt + (yEnt * WIDTH)] = 0x00FF21 ;
			}
		}
	}
}
