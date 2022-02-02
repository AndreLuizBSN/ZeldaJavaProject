package com.andreluizbsn.entities;

import java.awt.image.BufferedImage;

import com.andreluizbsn.main.Game;

public class Weapom extends Entity {

	public Weapom ( double x, double y, double w, double h, BufferedImage sprite ) {
		super(x, y, w, h, sprite);
		// TODO Auto-generated constructor stub
	}
		
	public void tick() {
		if (isCollidingWithPlayer( this )) {
			System.out.println("Pegou a arma");
			Game.entities.remove(this);
			Game.player.hasGun = true;
		}
	}
	
}
