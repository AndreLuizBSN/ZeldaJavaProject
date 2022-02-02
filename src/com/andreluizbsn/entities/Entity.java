package com.andreluizbsn.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.andreluizbsn.main.Game;
import com.andreluizbsn.world.Camera;
import com.andreluizbsn.world.Node;
import com.andreluizbsn.world.Vector2i;
import com.andreluizbsn.world.World;

public class Entity {
	
	public static BufferedImage LIFEPACK_EM = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage WEAPOM_EM = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage WEAPOMRELOAD_EM = Game.spritesheet.getSprite(32, 16, 16, 16);
	public static BufferedImage ENEMY_EM = Game.spritesheet.getSprite(48, 16, 16, 16);
	public static BufferedImage FINALDOOR_EM = Game.spritesheet.getSprite(64, 16, 16, 16);
	public static BufferedImage WEAPOM_UP = Game.spritesheet.getSprite(48, 32, 16, 16);
	public static BufferedImage WEAPOM_DOWN = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage WEAPOM_LEFT = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage WEAPOM_RIGHT = Game.spritesheet.getSprite(16, 32, 16, 16);
	
	private int maxFrames = 4, frames = 0;
	public int curAnimation = 0, maxAnimation = 2;
	
	protected double x, y, w, h;
	protected int z = 0;
	
	protected List< Node > path;
	
	public int depth = -1;
	
	private BufferedImage sprite;	
	//private int maskx, masky, mwidth, mheight;
	
	public Entity (double x, double y, double w, double h, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.sprite = sprite;
		/*
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = (int) w;
		this.mheight = (int) h;*/
	}
	/*
	public void setMask( int maskx, int masky, int mwidth, int mheight ) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}*/

	public double getH () {
		return h;
	}
	
	public void setH ( double h ) {
		this.h = h;
	}
	
	public double getW () {
		return w;
	}
	
	public void setW ( double w ) {
		this.w = w;
	}
	
	public double getX () {
		return x;
	}
	
	public void setX ( double x ) {
		this.x = x;
	}
	
	public double getY () {
		return y;
	}
	
	public void setY ( double y ) {
		this.y = y;
	}
	
	public int getZ () {
		return z;
	}
	
	public void setZ ( int z ) {
		this.z = z;
	}
	
	public BufferedImage getSprite () {
		return sprite;
	}
	
	public void setSprite ( BufferedImage sprite ) {
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
	}
	
	public void tick() {
		
	}
	
	public double calculateDistace( int x1, int x2, int y1, int y2 ) {
		return Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
	}
	

	public boolean isColliding( int nx, int ny ) {
		Rectangle enemyCurrent = new Rectangle((int) (nx + this.x), (int) (ny + this.y), (int) this.w, (int) this.h);
		Rectangle targetEnemy;
		Enemy e;
		for ( int i = 0; i < Game.enemiesCtrl.size(); i++ ) {
			e = Game.enemiesCtrl.get(i);
			if ( e == this )
				continue;
			
			targetEnemy = new Rectangle((int) (e.getX() + this.x), (int) (e.getY() + this.y), (int) this.w, (int) this.h);
			
			if ( enemyCurrent.intersects(targetEnemy) )
				return true;
		}
		
		return false;
	}
	
	public void followPath( List< Node > path, double speed ) {
		if ( path != null ) {
			if ( path.size() > 0 ) {
				Vector2i target = path.get( path.size() - 1 ).tile;
				//xprev = x;
				//yprev = y;
				if ( (int) x < target.x * 16
						&& World.isFree( (int) (x + speed), (int) y , this )
						&& ! isColliding((int) this.getX() + 1, (int) this.getY())) {
					x+=speed;
				} else if ( (int) x > target.x * 16
						&& World.isFree( (int) (x - speed), (int) y , this ) 
						&& ! isColliding((int) this.getX() - 1, (int) this.getY())){
					x-=speed;
				}
				
				if ( (int) y < target.y * 16 
						&& World.isFree( (int) (x), (int) (y + speed) , this )
						&& ! isColliding((int) this.getX(), (int) this.getY() + 1)){
					y+=speed;
				} else if ( (int) y > target.y * 16
						&& World.isFree( (int) (x), (int) (y - speed), this )
						&& ! isColliding((int) this.getX(), (int) this.getY() - 1)) {
					y-=speed;
				}
								
				if ( (int) x == target.x && (int) y == target.y ) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	public boolean isCollidingEntity( Entity e1, Entity e2 ) {
		Rectangle r1 = new Rectangle((int) e1.getX(), (int) e1.getY(), (int) e1.w, (int) e1.h );
		Rectangle r2 = new Rectangle((int) e2.getX(), (int) e2.getY(), (int) e2.w, (int) e2.h );
		
		if ( r1.intersects(r2) && e1.getZ() == e2.getZ() ) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean isCollidingWithPlayer( Entity e ) {
		
		return isCollidingEntity(e, Game.player);
		
	}
	
	public void animation(int maxAnimation) {
		frames ++;
		if ( frames > maxFrames ) {
			frames = 0;
			curAnimation++;
			if ( curAnimation >= maxAnimation ) {
				curAnimation = 0;
			}
		}		
	}
	
	public static Comparator< Entity > nodeSorted = new Comparator< Entity >() {
		@Override
		public int compare( Entity n0, Entity n1 ) {
			if ( n1.depth < n0.depth )
				return +1;
			if ( n1.depth > n0.depth )
				return -1;
			return 0;
		}
	}; 
	
}
