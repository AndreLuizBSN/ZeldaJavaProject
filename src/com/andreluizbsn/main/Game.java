package com.andreluizbsn.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.andreluizbsn.entities.BulletShoot;
import com.andreluizbsn.entities.Emunition;
import com.andreluizbsn.entities.Enemy;
import com.andreluizbsn.entities.Entity;
import com.andreluizbsn.entities.FinalDoor;
import com.andreluizbsn.entities.LifePack;
import com.andreluizbsn.entities.Player;
import com.andreluizbsn.entities.Weapom;
import com.andreluizbsn.graphics.GameOver;
import com.andreluizbsn.graphics.Spritesheet;
import com.andreluizbsn.graphics.UI;
import com.andreluizbsn.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	public static JFrame frame;
	public static final int WIDTH = 480, HEIGHT = 240, SCALE = 2;
	private boolean isRunning;
	private Thread thread;
	
	private BufferedImage image;
	
	public static List< Entity > entities;
	
	public static Spritesheet spritesheet;
	
	public static Player player;
	public static Enemy enemy;
	public static ArrayList<Enemy> enemiesCtrl;
	public static ArrayList<BulletShoot> bullets;
	public static ArrayList<LifePack> lifePackCtrl;
	public static LifePack lifePack;
	public static Emunition emunition;
	public static Weapom weapom;
	public static FinalDoor finalDoor;
	
	public static World world;
	
	public static Random rand;
	
	public UI ui;
	public GameOver gameOver;
	public static String state = "MENU";
	
	private static int CUR_LEVEL = 1;

	private int MAX_LEVEL = 3;
	
	public static Menu menu;
	
	public boolean saveGame = false;
	
	public int mx, my;
	
	public int[] pixels;
	
	public BufferedImage lightMap;
	public int[] lightMapPixels;
	
	public static boolean isSound = true;
	
	public static BufferedImage minimap;
	
	public static int[] minimapPixels;
	
	public static boolean isFullscreen = false;
	
	public Game() {
		
		rand = new Random();
		
		//player = sheet.getSprite(0, 0, 32, 64);
		//Fullscreen
		if ( isFullscreen )
			this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		else
			this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseMotionListener(this);
		this.initFrame();
		
		ui = new UI();
		
		//inicializando objetos
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			lightMap = ImageIO.read(getClass().getResource("/lightmap2.png"));
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lightMapPixels = new int[ lightMap.getWidth() * lightMap.getHeight() ];
		lightMap.getRGB(0, 0, lightMap.getWidth(), lightMap.getHeight(), lightMapPixels, 0, lightMap.getWidth());
		
		pixels = ( ( DataBufferInt ) image.getRaster().getDataBuffer() ).getData();
		
		Game.newGame("level1.png", 100, false, 0);
	}
	
	public static void newGame( String level, double life, boolean hasGun, int ammo ) {
		
		if ( Game.entities != null )
			Game.entities.clear();
		if ( Game.enemiesCtrl != null )
			Game.enemiesCtrl.clear();
		Game.entities = new ArrayList<>();
		Game.enemiesCtrl = new ArrayList<>();
		Game.bullets = new ArrayList<>();
		Game.spritesheet = new Spritesheet("/spritesheets.png");
		Game.world = new World("/" + level);
		
		minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		minimapPixels = ((DataBufferInt) minimap.getRaster().getDataBuffer()).getData();
		
		Game.player = new Player(Game.world.getPlayerPosition()[0], Game.world.getPlayerPosition()[1], 16, 16, Game.spritesheet.getSprite(128, 0, 16, 16));
		
		player.life = life;
		player.hasGun = hasGun;
		player.ammo = ammo;
		
		for ( int[] e : Game.world.getEnemyPositions() ) {
			Game.enemy = new Enemy( e[0], e[1], 16, 16, Entity.ENEMY_EM);
			Game.entities.add(Game.enemy);
			Game.enemiesCtrl.add(Game.enemy);
		}		
		for ( int[] e : Game.world.getLifePacks() ) {
			Game.lifePack = new LifePack( e[0], e[1], 16, 16, Entity.LIFEPACK_EM, e[2]);
			Game.entities.add(Game.lifePack);
			//lifePackCtrl.add(lifePack);
		}
		
		for ( int[] e : Game.world.getEmunition() ) {
			Game.emunition = new Emunition( e[0], e[1], 16, 16, Entity.WEAPOMRELOAD_EM, e[2]);
			Game.entities.add(Game.emunition);
		}
		
		Game.weapom = new Weapom(Game.world.getWeapom()[0], Game.world.getWeapom()[1], 16, 16, Entity.WEAPOM_EM);
		Game.entities.add(Game.weapom);
		
		if ( Game.world.getFinalDoor()[2] == 1 ) {
			Game.finalDoor = new FinalDoor(Game.world.getFinalDoor()[0], Game.world.getFinalDoor()[1], 16, 16, Entity.FINALDOOR_EM);
			Game.entities.add(Game.finalDoor);
		}
		
		//Game.player.life = 100;
		
		Game.entities.add(Game.player);
		
		Game.menu = new Menu();
	}

	public void initFrame() {
		frame = new JFrame("Game 01");
		frame.add(this);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		//icone do app do game
		Image image = null;
		try {
			image = ImageIO.read(getClass().getResource("/icon.png"));
		} catch ( Exception e ) {
			// TODO: handle exception
		}
		frame.setIconImage(image);
		//cursor mause
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cImage = toolkit.getImage(getClass().getResource("/cursor.png"));
		Cursor c = toolkit.createCustomCursor(cImage, new Point(0,0), "img");
		frame.setCursor(c);
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		this.isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		this.isRunning = false;
		try {
			thread.join();
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void saveStatus() {
		this.saveGame = false;
		String[] opt1 = {
				"level", 
				"weapom", 
				"ammo",
				"life"
		};				
		int[] opt2 = {
				CUR_LEVEL, 
				player.hasGun ? 1 : 0, 
				player.ammo,
				(int) player.life
		};
		Menu.saveGame( opt1, opt2, 10 );
		System.out.println("Jogo Salvo!");
	}

	public void tick() {
		
		if ( Game.state.equals("NORMAL") ) {
						
			if ( isSound ) {
				Sound.backgroundSound.loop();
			}
			
			if ( this.saveGame ) {
				saveStatus();
			}
			
			for ( int i = 0; i < Game.entities.size(); i++ ) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for ( int i = 0; i < Game.bullets.size(); i++ ) {
				Game.bullets.get(i).tick();
			}
			
			if ( player.isCollidingEntity( player, finalDoor ) ) {
				
				CUR_LEVEL++;
	
				System.out.println(CUR_LEVEL);
				if ( CUR_LEVEL > MAX_LEVEL ) {
					CUR_LEVEL = 1;
				}
				
				String newWorld = "level" + CUR_LEVEL + ".png";
				
				saveStatus();
								
				newGame(newWorld, player.life, player.hasGun, player.ammo);
				
			}
		} else if ( Game.state.equals("MENU") ) {
			
			if ( isSound ) {
				Sound.backgroundSound.stop();
			}
			
			menu.tick();
		}
			
	}
	
	public void drawRectangleExample() {
		/*for( int xx = 0; xx < 32; xx++ ) {
			for( int yy = 0; yy < 32; yy++ ) {
				pixels[ xx + ( yy * WIDTH ) ] = 0xff0000;
			}
		}*/
	}
	
	public void applyLight() {
		for( int xx = 0; xx < Game.WIDTH; xx ++ ) {
			for( int yy = 0; yy < Game.HEIGHT; yy ++ ) {
				if ( lightMapPixels[ xx + ( yy * Game.WIDTH ) ] == -1 ) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0, 0);
					pixels[ xx + ( yy * Game.WIDTH ) ] = pixel;
				} else if ( lightMapPixels[ xx + ( yy * Game.WIDTH ) ] == -11776948 ) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x2D2D2D, 0);
					pixels[ xx + ( yy * Game.WIDTH ) ] = pixel;
				} else if ( lightMapPixels[ xx + ( yy * Game.WIDTH ) ] == -13290187 ) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x4C4C4C, 0);
					pixels[ xx + ( yy * Game.WIDTH ) ] = pixel;
				} else if ( lightMapPixels[ xx + ( yy * Game.WIDTH ) ] == -14145496 ) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x595959, 0);
					pixels[ xx + ( yy * Game.WIDTH ) ] = pixel;
				} else if ( lightMapPixels[ xx + ( yy * Game.WIDTH ) ] == -15132391 ) {
					int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0x595959, 0);
					pixels[ xx + ( yy * Game.WIDTH ) ] = pixel;
				}
			}
 		}
	}

	public void render() {
		
		Graphics g = image.getGraphics();
			
		BufferStrategy bs = this.getBufferStrategy();
		if ( bs == null ) {
			this.createBufferStrategy(3);
			return;
		}
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		Game.world.render(g);
		Collections.sort(entities, Entity.nodeSorted);
		
		for ( int i = 0; i < Game.entities.size(); i++ ) {
			Entity e = entities.get(i);
			e.render( g );
		}
		
		for ( int i = 0; i < Game.bullets.size(); i++ ) {
			Game.bullets.get(i).render(g);
		}
		

		//applyLight();
		
		g.dispose();
				
		
		g = bs.getDrawGraphics();
		
		if ( isFullscreen )
			g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
		else
			g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
			
		
		ui.render(g);
		
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.setColor(Color.black);
		g.drawString("Ammo : " + player.ammo, 110, 9);
			
		if ( Game.state.equals("GAME_OVER") ){
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("Arial", Font.BOLD, 28));
			g.setColor(Color.white);
			g.drawString("Game Over", 160, 120);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.setColor(Color.white);
			g.drawString("Press N to restart game", 150, 140);
			if ( isSound ) {
				Sound.backgroundSound.stop();
			}
		} else if ( Game.state.equals("MENU") ) {
			menu.render(g);
		}
				
		//rotacionaro objeto
		/*Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(my - 75, mx - 75);
		g2.rotate( angleMouse, 50+25,50+25);
		g.setColor(Color.RED);
		g.fillRect(50, 50, 50, 50);*/

		
		//minimap
		else if ( Game.state.equals("NORMAL") ){
			World.renderMiniMap();
			g.drawImage(minimap, 10, 130, World.WIDTH * 5, World.HEIGHT * 5, null);
		}
		bs.show();

		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastTime = System.nanoTime();
		double amonthOfTicks = 60.0;
		double ns = 1000000000 / amonthOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {				
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		this.stop();
	}

	@Override
	public void keyPressed ( KeyEvent e ) {
		if ( ( e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D ) && Game.state.equals("NORMAL") ) {
			player.r = true;
		} else if ( ( e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A ) && Game.state.equals("NORMAL") ) {
			player.l = true;
		}
		
		if ( ( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) && Game.state.equals("NORMAL") ) {
			player.u = true;
		} else if ( ( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S ) && Game.state.equals("NORMAL") ) {
			player.d = true;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_E && Game.state.equals("NORMAL") ) {
			player.jump = true;
		}
		
		if ( ( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) && Game.state.equals("MENU") ) {
			menu.up = true;
		} else if ( ( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S ) && Game.state.equals("MENU") ) {
			menu.down = true;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_ENTER && Game.state.equals("MENU") ) {
			menu.enter = true;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_ESCAPE && Game.state.equals("NORMAL") ) {
			Menu.options[0] = "Continue";
			Game.state = "MENU";
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_SPACE && Game.state.equals("NORMAL") ) {
			player.shoot = true;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_N && Game.state.equals("GAME_OVER") ) {
			Game.state = "NORMAL";
			CUR_LEVEL = 1;
			newGame("level1.png", 100, false, 0);
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_P && Game.state.equals("NORMAL") ) {
			saveGame = true;
		}
	}

	@Override
	public void keyReleased ( KeyEvent e ) {
		if ( e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D ) {
			player.r = false;
		} else if ( e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A ) {
			player.l = false;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) {
			player.u = false;
		} else if ( e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S ) {
			player.d = false;
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
			player.shoot = false;
		}
		
	}

	@Override
	public void keyTyped ( KeyEvent e ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked ( MouseEvent arg0 ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered ( MouseEvent arg0 ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited ( MouseEvent arg0 ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed ( MouseEvent e ) {
		player.mouseShoot = true;
		player.mouseX = e.getX() / SCALE;
		player.mouseY = e.getY() / SCALE;
		
	}

	@Override
	public void mouseReleased ( MouseEvent arg0 ) {
		player.mouseShoot = false;
		
	}
	
	public static void setCurLevel ( int cUR_LEVEL ) {
		CUR_LEVEL = cUR_LEVEL;
	}

	@Override
	public void mouseDragged ( MouseEvent arg0 ) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved ( MouseEvent e ) {
		// TODO Auto-generated method stub
		this.mx = e.getX();
		this.my = e.getY();
	}

}
