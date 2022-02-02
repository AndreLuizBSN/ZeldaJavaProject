package com.andreluizbsn.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;
import com.andreluizbsn.world.AStar;
import com.andreluizbsn.world.Camera;
import com.andreluizbsn.world.Vector2i;

public class Enemy extends Entity {
	
	private double speed = 0.6;
	private BufferedImage[] enemySprites;
	private BufferedImage enemySpritesDano;
	public int maxAnimation = 2;
	public int life = 5;
	public boolean isDamaged = false;
		
	public Enemy ( int x, int y, int w, int h, BufferedImage sprite ) {
		super(x, y, w, h, sprite);
		enemySprites = new BufferedImage[2];
		enemySprites[0] = Game.spritesheet.getSprite(48, 16, 16, 16);
		enemySprites[1] = Game.spritesheet.getSprite(80, 16, 16, 16);

		enemySpritesDano = Game.spritesheet.getSprite(96, 16, 16, 16);
	}
	
	public void tick() {
		depth = 0;
				
		if ( this.calculateDistace((int) this.getX(), (int) Game.player.getX(), (int) this.getY(), (int) Game.player.getY()) < 100) {
			/*if ( ! isCollidingWithPlayer( this ) ) {
				if ( Game.rand.nextInt(100) < 50 ) {
					if ( (int) x < Game.player.getX() && 
							World.isFree( (int) (x + speed), (int) y, this ) &&
							! this.isColliding((int) (this.getX() + speed), (int) this.getY() )) {
						x+=speed;
						animation(this.maxAnimation);
					} else if ( (int) x > Game.player.getX() && 
							World.isFree( (int) (x - speed), (int) y , this )  &&
							! this.isColliding((int) (this.getX() - speed), (int) this.getY() )) {
						x-=speed;
						animation(this.maxAnimation);
					}
			
					if ( (int) y < Game.player.getY() &&
							World.isFree( (int) x, (int) (y + speed) , this ) &&
							! this.isColliding((int) this.getX(), (int) (this.getY() + speed)) ) {
						y+=speed;
						animation(this.maxAnimation);
					} else if ( (int) y > Game.player.getY() &&
							World.isFree( (int) x, (int) (y - speed) , this )  &&
							! this.isColliding((int) this.getX(), (int) (this.getY() - speed)) ) {
						y-=speed;
						animation(this.maxAnimation);
					}
				}
			} else {
				Game.player.incrementLife(false, 0.1);
			}
			*/
			
			if ( isCollidingWithPlayer( this ) ) { 
				Game.player.incrementLife(false, 0.1);
			}
		
			//if ( path == null || path.size() == 0 ) {
				Vector2i start = new Vector2i((int) this.x / 16, (int) this.y / 16);
				Vector2i end = new Vector2i((int) Game.player.x / 16, (int) Game.player.y / 16);
				path = AStar.fintPath(Game.world, start, end);
			//}
			
			followPath(path, speed);
		
			animation(this.maxAnimation);
			
			if ( collidingBullet() ) {
				this.life -= 1;
				this.isDamaged = true;
			}
			
			if ( this.life ==0 ) {
				Game.entities.remove(this);
				Game.enemiesCtrl.remove(this);
				return;
			}
		}
		
	}
	
	public boolean collidingBullet() {
		for ( int i = 0; i < Game.bullets.size(); i++ ) {
			Entity e = Game.bullets.get(i);
			
			if ( e instanceof BulletShoot ) {
				if ( isCollidingEntity( this, e ) ) {
					Game.bullets.remove(e);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	@Override
	public void render ( Graphics g ) {
		if ( this.isDamaged ) {
			g.drawImage(enemySpritesDano, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
			this.isDamaged = false;
		} else {
			g.drawImage(enemySprites[this.curAnimation], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
		}
	}
	
}
