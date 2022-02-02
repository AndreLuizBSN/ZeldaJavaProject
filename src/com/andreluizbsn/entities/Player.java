package com.andreluizbsn.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.andreluizbsn.main.Game;
import com.andreluizbsn.main.Sound;
import com.andreluizbsn.world.Camera;
import com.andreluizbsn.world.World;

public class Player extends Entity {
	
	public boolean r, l, u, d;
	public double speed = 1;
	
	private BufferedImage[] rPlayer, lPlayer, uPlayer, dPlayer;
	private BufferedImage stopedPlayer; 
	public double life = 100;
	public int ammo = 0;
	
	public boolean hasGun = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int mouseX, mouseY;
	
	public boolean jump = false, isJumping = false, jumpUp = false, jumpDown = false;
	public int jumpFrame = 50, jumpCur = 0, jumpSpeed = 2;

	public Player ( int x, int y, int w, int h, BufferedImage sprite ) {
		super(x, y, w, h, sprite);
		// TODO Auto-generated constructor stub
		rPlayer = new BufferedImage[2];
		rPlayer[0] = Game.spritesheet.getSprite(128, 0, 16, 16);
		rPlayer[1] = Game.spritesheet.getSprite(144, 0, 16, 16);
		
		lPlayer = new BufferedImage[2];
		lPlayer[0] = Game.spritesheet.getSprite(96, 0, 16, 16);
		lPlayer[1] = Game.spritesheet.getSprite(112, 0, 16, 16);
		
		uPlayer = new BufferedImage[2];
		uPlayer[0] = Game.spritesheet.getSprite(64, 0, 16, 16);
		uPlayer[1] = Game.spritesheet.getSprite(80, 0, 16, 16);
		
		dPlayer = new BufferedImage[2];
		dPlayer[0] = Game.spritesheet.getSprite(32, 0, 16, 16);
		dPlayer[1] = Game.spritesheet.getSprite(48, 0, 16, 16);
		
		stopedPlayer = Game.spritesheet.getSprite(32, 0, 16, 16);
	}
	
	@Override
	public void tick () {
		
		depth = 1;
		
		if ( jump ) {
			jump = false;
			if ( ! isJumping ) {
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if ( isJumping ) {
			//System.out.println("Is Jumping");
			if ( jumpCur <= jumpFrame ) {
				if ( jumpUp ) {
					jumpCur+=jumpSpeed;
				} else if ( jumpDown ) {
					jumpCur-=jumpSpeed;
					if ( jumpCur <= 0 ) {
						isJumping = false;
						jumpDown = false;
					}
				}
				if ( jumpCur > jumpFrame ) {
					jumpCur = jumpFrame;
				}
				this.setZ( jumpCur );
				if ( jumpCur == jumpFrame ) {
					jumpUp = false;
					jumpDown = true;
				}
			}
		}
			
		Camera.posicionarCamera((int) this.x - (Game.WIDTH/2), (int) this.y - (Game.HEIGHT/2));
		
		if ( r && World.isFree( (int) (x + speed), (int) y , this)) {
			 this.x = this.x+=speed;
			 animation(this.maxAnimation);
		}
		if ( l && World.isFree( (int) (x - speed), (int) y , this ) ) {
			 this.x = this.x-=speed;
			 animation(this.maxAnimation);
		}
		if ( u && World.isFree( (int) x, (int) (y - speed) , this ) ) {
			 this.y = this.y-=speed;
			 animation(this.maxAnimation);
		}
		if ( d && World.isFree( (int) x, (int) (y + speed) , this ) ) {
			 this.y = this.y+=speed;
			 animation(this.maxAnimation);
		}
		
		shoot();
	}
	
	public void shoot() {
		
		if ( shoot || mouseShoot ) {
			
			if ( hasGun && ammo > 0 ) {
				ammo -= 1;
						
				double dx = 0, dy = 0;
				
				if ( shoot ) {
					if ( r ) {
						dx = 1;
					} else if ( l ) {
						dx = -1;
					}
					
					if ( d ) {
						dy = 1;
					} else if ( u ) {
						dy = -1;
					}
					
					if ( !u && !d && !l && !r ) {
						dy = 1;
					}
				} else if ( mouseShoot ) {
					
					double angle = Math.atan2(mouseY - ( this.getY()+8 - Camera.y ), mouseX - ( this.getX()+8 - Camera.x));
					
					dx = Math.cos(angle);
					dy = Math.sin(angle);
				}
				
				BulletShoot bulletShoot = new BulletShoot(this.getX(), this.getY(), 3, 3, null, dx, dy);
				Game.bullets.add(bulletShoot);
			
			}
			
			shoot = false;
			mouseShoot = false;
		}
	}
	
	public void incrementLife( boolean increment, double value ) {
		if ( increment ) {
			if ( life < 100 ) {
				life += value;
			}
			
			if ( life > 100 ) {
				life = 100;
			}
		} else {
			life-= value;
			if ( Game.isSound )
				Sound.hurt.start();
			if ( life < 0 ) {
				life = 0;
			}
		}
		
		if ( life == 0 ) {
			System.out.println("Game Over");
			Game.state = "GAME_OVER";
			//Game.newGame("level1.png");
		}
		
		//System.out.println(life);
	}
	
	public void incrementAmmo( boolean increment, double value ) {
		if ( increment ) {
			ammo += value;
		} else {
			ammo-= value;
			if ( ammo < 0 ) {
				ammo = 0;
			}
		}
		
		System.out.println("Munição: " + ammo);
		
		if ( ammo == 0 ) {
			System.out.println("Sem munição");
		}
		
		//System.out.println(life);
	}
	
	@Override
	public void render ( Graphics g ) {
		if ( isJumping ) {
			g.setColor(Color.black);
			g.fillOval((int) this.getX() - Camera.x , (int) this.getY() - Camera.y + 15, 16, 5);
		}
			
		if ( r ) {
			g.drawImage(rPlayer[this.curAnimation], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z , null);
			if ( hasGun ) {
				g.drawImage(Entity.WEAPOM_RIGHT, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z, null);
			}
		} else if ( l ) {
			g.drawImage(lPlayer[this.curAnimation], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z , null);
			if ( hasGun ) {
				g.drawImage(Entity.WEAPOM_LEFT, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z, null);
			}
		} else if ( u ) {
			g.drawImage(uPlayer[this.curAnimation], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z , null);
			if ( hasGun ) {
				g.drawImage(Entity.WEAPOM_UP, (int) this.getX() - Camera.x + 5 , (int) this.getY() - Camera.y - z, null);
			}
			/*g.setColor(Color.black);
			g.fillRect((int) this.x - Camera.x, (int) this.y - Camera.y, uPlayer[this.curAnimation].getWidth(), uPlayer[this.curAnimation].getHeight() );
			*/
		} else if ( d ) {
			g.drawImage(dPlayer[this.curAnimation], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z , null);
			if ( hasGun ) {
				g.drawImage(Entity.WEAPOM_DOWN, (int) this.getX() - Camera.x + 5, (int) this.getY() - Camera.y - z, null);
			}
		} else {
			this.curAnimation = 0;
			g.drawImage(stopedPlayer, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y - z , null);
			if ( hasGun ) {
				g.drawImage(Entity.WEAPOM_EM, (int) this.getX() - Camera.x + 5, (int) this.getY() - Camera.y - z, null);
			}
		}
		
	}

}
