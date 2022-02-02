package com.andreluizbsn.entities;

import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;

public class Emunition extends Entity {
	
	private int valAmmo = 2;

	public Emunition ( double x, double y, double w, double h, BufferedImage sprite, int valAmmo ) {
		super(x, y, w, h, sprite);
		// TODO Auto-generated constructor stub
		this.valAmmo = valAmmo;
	}
	
	public void tick() {
		if (isCollidingWithPlayer( this )) {
			Game.player.incrementAmmo(true, valAmmo);
			Game.entities.remove(this);
		}
	}
}
