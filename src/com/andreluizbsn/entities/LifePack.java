package com.andreluizbsn.entities;

import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;

public class LifePack extends Entity {
	
	private int valLife;

	public LifePack ( double x, double y, double w, double h, BufferedImage sprite, int valLife ) {
		super(x, y, w, h, sprite);
		// TODO Auto-generated constructor stub
		this.valLife = valLife;
	}
	
	public void tick() {
		if (isCollidingWithPlayer( this )) {
			Game.player.incrementLife(true, valLife);
			Game.entities.remove(this);
		}
	}
	
}
