package com.andreluizbsn.world;

import com.andreluizbsn.main.Game;

public class Camera {

	public static int x = 0, y = 0;
	
	public static void posicionarCamera( int x, int y ) {
		Camera.x = x;
		Camera.y = y;
		
		//Esquerda
		if ( Camera.x < 0 ) {
			Camera.x = 0;
		}
		
		//Topo
		if ( Camera.y < 0 ) {
			Camera.y = 0;
		}
		
		//Direita
		if( (World.WIDTH * 16) < Camera.x + Game.WIDTH ) {
			Camera.x = (World.WIDTH * 16) - Game.WIDTH;
		}
		
		//Baixo
		if( (World.HEIGHT * 16) < Camera.y + Game.HEIGHT ) {
			Camera.y = (World.HEIGHT * 16) - Game.HEIGHT;
		}
		
	}
	
}
