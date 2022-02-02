package com.andreluizbsn.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;
import com.andreluizbsn.world.Camera;

public class BulletShoot extends Entity {

	private double dx, dy;
	private double speed = 4;
	
	private int life = 30, curLife = 0;
	
	public BulletShoot ( double x, double y, double w, double h, BufferedImage sprite, double dx, double dy ) {
		super(x, y, w, h, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx * speed;
		y+=dy * speed;
		curLife++;
		if( curLife == life ) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render( Graphics g ) {
		g.setColor(Color.yellow);
		g.fillOval((int) this.getX() - Camera.x, (int) this.getY() - Camera.y, 2, 2);
	}

}
