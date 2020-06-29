package me.giverplay.zelda;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import me.giverplay.zelda.entities.Enemy;
import me.giverplay.zelda.entities.Entity;
import me.giverplay.zelda.entities.FireShot;
import me.giverplay.zelda.entities.Player;
import me.giverplay.zelda.events.Listeners;
import me.giverplay.zelda.graphics.Camera;
import me.giverplay.zelda.graphics.FontUtils;
import me.giverplay.zelda.graphics.Menu;
import me.giverplay.zelda.graphics.Spritesheet;
import me.giverplay.zelda.graphics.UI;
import me.giverplay.zelda.sound.Sound;
import me.giverplay.zelda.utils.Cores;
import me.giverplay.zelda.utils.GameState;
import me.giverplay.zelda.world.World;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private List<Entity> entities;
	private List<Enemy> enemies;
	private List<FireShot> shoots;
	
	private static Game game;
	private static int FPS = 0;
	
	private Listeners listeners;
	private GameState state = GameState.NORMAL;
	private Spritesheet sprite;
	private Camera camera;
	private World world;
	private Player player;
	private Menu menu;
	private UI ui;
	
	private BufferedImage lightmap;
	private BufferedImage image;
	private BufferedImage map;
	private Thread thread;
	private Random random;
	private JFrame frame;
	
	private boolean isRunning = false;
	private boolean showGameOver = true;
	
	private int[] lightPixels;
	private int[] pixels;
	private int[] mapPixels;
	
	private int level = 1;
	//private int max_levels = 2;
	
	private int gameOverFrames = 0;
	private int maxGameOverFrames = 30;
	
	public static Game getGame()
	{
		return game;
	}
	
	// M�todos Startup | TODO
	public Game()
	{
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		setState(GameState.MENU);
		setupFrame();
		setupAssets();
	}
	
	public static void main(String[] args)
	{
		FontUtils.init();
		
		Game game = new Game();
		game.start();
	}
	
	private void setupFrame()
	{
		frame = new JFrame("Tec tec tec tec tec tec tec tec tec");
		frame.add(this);
		frame.setResizable(false);
		frame.setUndecorated(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void setupAssets()
	{
		listeners = new Listeners();
		addKeyListener(listeners);
		addMouseListener(listeners);
		
		game = this;
		
		random = new Random();
		camera = new Camera();
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		shoots = new ArrayList<FireShot>();
		
		sprite = new Spritesheet("/Spritesheet.png");
		player = new Player(1, 1, 16, 16);
		world = new World();
		//world = new World("/level" + level + ".png");
		
		ui = new UI();
		menu = new Menu();
		
		entities.add(player);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		map = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		try
		{
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		}
		catch(IOException e)
		{
			System.out.println("Falha ao carregar LightMap");
			System.exit(0);
		}
		
		mapPixels = ((DataBufferInt) map.getRaster().getDataBuffer()).getData();
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		lightPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightPixels, 0, lightmap.getWidth());
		
		Sound.theme.play();
	}
	
	// Metodos de Controle do Fluxo | TODO
	
	public synchronized void start()
	{
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning = false;
		
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void restart()
	{
		removeKeyListener(listeners);
		removeMouseListener(listeners);
		setupAssets();
	}
	
	public static void handleRestart()
	{
		if(getGame().getState() == GameState.GAME_OVER || getGame().getState() == GameState.LEVEL_UP)
		{
			if(getGame().getState() == GameState.GAME_OVER) getGame().setLevel(1);
			getGame().setState(GameState.NORMAL);
			game.restart();
		}
	}
	
	// Core | TODO
	
	@Override
	public void run()
	{
		requestFocus();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		double ticks = 60.0D;
		double ns = 1000000000 / ticks;
		double delta = 0.0D;
		
		int fps = 0;
		
		while(isRunning)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1)
			{
				tick();
				render();
				
				delta--;
				fps++;
			}
			
			if(System.currentTimeMillis() - timer >= 1000)
			{
				FPS = fps;
				fps = 0;
				timer = System.currentTimeMillis();
			}
		}
		
		stop();
	}
	
	public synchronized void tick()
	{
		switch(getState())
		{
			case NORMAL:
				for(int i = 0; i < entities.size(); i++) entities.get(i).tick();
				for(int i = 0; i < shoots.size(); i++) shoots.get(i).tick();
				
				/*if(enemies.size() == 0)
				{
					level++;
					
					if(level > max_levels) level = 1;
					
					setState(GameState.LEVEL_UP);
					
					handleRestart();
				}*/
				break;
	
			case GAME_OVER:
			case LEVEL_UP:
			case PAUSED:
			case MENU:
				break;
		}
	}
	
	public synchronized void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
		
		/** Renderiaza��o do Jogo **/
		
		world.render(g);
		
		Collections.sort(entities, Enemy.sortDepth);
		
		for(Entity e : entities)
		{
			e.render(g);
		}
		
		for(FireShot fs : shoots)
		{
			fs.render(g);
		}
		
		//renderLight(g);
		ui.render(g);
		
		/******/
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		world.renderMinimap();
		g.drawImage(map, WIDTH * SCALE - map.getWidth() - 10 - 100, +10, 200, 200, null);
		
		renderSmooth(g);
		
		if(getState() == GameState.GAME_OVER)
		{
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			
			String txt = "Game Over";
			g.setColor(Color.WHITE);
			g.setFont(FontUtils.getFont(32, Font.BOLD));
			g.drawString(txt, (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth(txt)) / 2, WIDTH * SCALE / 2);
			
			gameOverFrames++;
			
			if(gameOverFrames > maxGameOverFrames)
			{
				gameOverFrames = 0;
				showGameOver = !showGameOver;
			}
			
			if(showGameOver)
			{
				g.setFont(FontUtils.getFont(24, Font.BOLD));
				g.drawString("> Aperte ENTER para reiniciar <", (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth("> Aperte ENTER para reiniciar <")) / 2, HEIGHT * SCALE / 2 + 28);
			}
		}
		else if(getState() == GameState.MENU || getState() == GameState.PAUSED)
		{
			menu.render(g);
		}
		
		bs.show();
	}
	
	public void renderLight(Graphics g)
	{
		for(int xx = 0; xx < lightmap.getWidth(); xx++)
		{
			for(int yy = 0; yy < lightmap.getHeight(); yy++)
			{
				int coord = xx + (yy * lightmap.getWidth());
				
				if(lightPixels[coord] == Cores.LIGHT_EXCLUDE)
				{
					pixels[coord] = Cores.LIGHT_REPLACE;
				}
			}
		}
	}
	
	public void renderSmooth(Graphics g)
	{
		g.setColor(new Color(100, 100, 100));
		g.setFont(FontUtils.getFont(18, Font.PLAIN));
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String txt = "Ammo: " + player.getAmmo();
		
		g.drawString(txt, WIDTH * SCALE - 10 - metrics.stringWidth(txt), HEIGHT * SCALE);
		
		// FPS
		g.setColor(Color.WHITE);
		g.setFont(FontUtils.getFont(11, Font.PLAIN));
		g.drawString("FPS: " + FPS, 2, 20);
	}
	
	// Getters e Setters | TODO
	
	public Camera getCamera()
	{
		return this.camera;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public Random getRandom()
	{
		return this.random;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public GameState getState()
	{
		return this.state;
	}
	
	public Spritesheet getSpritesheet()
	{
		return this.sprite;
	}
	
	public Menu getMenu()
	{
		return this.menu;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public int[] getMinimap()
	{
		return this.mapPixels;
	}
	
	public List<Enemy> getEnemies()
	{
		return this.enemies;
	}
	
	public List<Entity> getEntities()
	{
		return this.entities;
	}
	
	public List<FireShot> getShoots()
	{
		return this.shoots;
	}
	
	public void setState(GameState state)
	{
		this.state = state;
	}
	
	public void setLevel(int i)
	{
		level = 1;
	}
}
