package me.giverplay.zelda.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.entities.Bullet;
import me.giverplay.zelda.entities.Collectible;
import me.giverplay.zelda.entities.Enemy;
import me.giverplay.zelda.entities.Entity;
import me.giverplay.zelda.entities.LifePack;
import me.giverplay.zelda.entities.Player;
import me.giverplay.zelda.entities.Weapon;
import me.giverplay.zelda.utils.Cores;

public class World
{
	public static final int TILE_SIZE = 16;
	
	private Game game;
	
	private int width;
	private int height;
	
	private static Tile[] tiles;
	
	public World()
	{
		game = Game.getGame();
		game.getPlayer().setX(TILE_SIZE);
		game.getPlayer().setY(TILE_SIZE);
		
		width = 100;
		height = 100;
		
		tiles = new Tile[width * height];
		
		generateRandomMap();
	}
	
	public World(String path, boolean old)
	{
		game = Game.getGame();
		
		try
		{
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			width = map.getWidth();
			height = map.getHeight();
			
			int lenght = width * height;
			int[] pixels = new int[lenght];
			
			tiles = new Tile[lenght];
			
			map.getRGB(0, 0, width, height, pixels, 0, width);
			
			for(int xx = 0; xx < width; xx++)
			{
				for(int yy = 0; yy < height; yy++)
				{
					int index = xx + (yy * width);
					
					tiles[index] = new GrassTile(xx * TILE_SIZE, yy * TILE_SIZE);
					switch (pixels[index])
					{	
						case Cores.MAPA_PAREDE:
							tiles[index] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE);
							break;
							
						case Cores.MAPA_BULLET:
							Bullet bull = new Bullet(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENTITY_BULLET);
							bull.setMask(6, 8, 4, 8);
							game.getEntities().add(bull);
							break;
							
						case Cores.MAPA_VIDA:
							LifePack pack = new LifePack(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENTITY_LIFE_PACK);
							pack.setMask(3, 7, 10, 9);
							game.getEntities().add(pack);
							break;
							
						case Cores.MAPA_INIMIGO:
							Enemy enemy = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENTITY_ENEMY);
							game.getEntities().add(enemy);
							game.getEnemies().add(enemy);
							break;
							
						case Cores.MAPA_WEAPON:
							Weapon wea = new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENTITY_WEAPON);
							wea.setMask(0, 6, 16, 6);
							game.getEntities().add(wea);
							break;
							
						case Cores.MAPA_JOGADOR:
							game.getPlayer().setX(xx * TILE_SIZE);
							game.getPlayer().setY(yy * TILE_SIZE);
							break;
							
						default:
							tiles[index] = new Tile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_GRAMA);
							break;
					}
					
				}
			}
			
		} catch (IOException e)
		{
			System.out.println("Falha ao ler o mapa");
		}
	}
	
	public void generateRandomMap()
	{
		int dir = 0;
		
		for(int xx = 0; xx < width; xx++)
		{
			for(int yy = 0; yy < height; yy++)
			{
				tiles[xx + yy * width] = new WaterTile(xx * TILE_SIZE, yy * TILE_SIZE);
			}
		}
		
		for(int xn = 0; xn < width; xn++)
		{
			tiles[xn] = new WallTile(xn * TILE_SIZE, 0);
			tiles[xn + (width - 1)* height] = new WallTile(xn * TILE_SIZE, (height - 1) * TILE_SIZE);
		}
		
		for(int yn = 1; yn < width; yn++)
		{
			tiles[width * yn] = new WallTile(0, yn * TILE_SIZE);
			tiles[(width - 1) + yn * height] = new WallTile((width - 1) * TILE_SIZE, yn * TILE_SIZE);
		}
		
		int xx = 1;
		int yy = 1;
		
		TileType[] materials = new TileType[] {TileType.BARRO, TileType.DIRT, TileType.GRASS, TileType.SAND};
		TileType mat = materials[game.getRandom().nextInt(materials.length)];
		
		for(int i = 0; i < 5000; i++)
		{
			if(dir == 0)
			{
				if(xx < width - 1)
				{
					xx++;
				}
			}
			else if(dir == 1)
			{
				if(xx > 1)
				{
					xx--;
				}
			}
			else if(dir == 2)
			{
				if(yy < height - 1)
				{
					yy++;
				}
			}
			else if(dir == 3)
			{
				if(yy > 1)
				{
					yy--;
				}
			}
			
			if(game.getRandom().nextInt(100) < 30)
			{
				dir = game.getRandom().nextInt(4);
			}
			
			switch (mat)
			{
				case BARRO:
					tiles[xx + yy * width] = new BarroTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				case DIRT:
					tiles[xx + yy * width] = new DirtTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				case BUSH:
					tiles[xx + yy * width] = new BushTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				case GRASS:
					tiles[xx + yy * width] = new GrassTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				case SAND:
					tiles[xx + yy * width] = new SandTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				case WALL:
					tiles[xx + yy * width] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE);
					break;
					
				default:
					break;
			}
			
			if(game.getRandom().nextInt(100) < 10)
			{
				mat = materials[game.getRandom().nextInt(materials.length)];
			}
		}
		
		tiles[1 + width] = new GrassTile(TILE_SIZE, TILE_SIZE);
	}
	
	public void render(Graphics g)
	{
		int xs = game.getCamera().getX() >> 4;
			int ys = game.getCamera().getY() >> 4;
		int xf = xs + (Game.WIDTH * Game.SCALE >> 4);
		int yf = ys + (Game.HEIGHT * Game.SCALE >> 4);
		
		
		for(int xx = xs; xx <= xf; xx++)
		{
			for(int yy = ys; yy <= yf; yy++)
			{
				
				if(xx < 0 || yy < 0 || xx >= width || yy >= height)
					continue;
				
				tiles[xx + (yy * width)].render(g);
			}
		}
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public Tile[] getTiles()
	{
		return tiles;
	}
	
	public void renderMinimap()
	{
		for(int i = 0; i < game.getMinimap().length; i++)
		{
			game.getMinimap()[i] = 0xffffffff;
		}
		
		for(int xx = 0; xx < width; xx++)
		{
			for(int yy = 0; yy < height; yy++)
			{
				int curP = xx + (yy * width);
				
				if(tiles[curP] instanceof WallTile)
				{
					game.getMinimap()[curP] = Cores.WALL_TILE;
				}
				
				if(tiles[curP] instanceof BushTile)
				{
					game.getMinimap()[curP] = Cores.BUSH_TILE;
				}
				
				if(tiles[curP] instanceof WaterTile)
				{
					game.getMinimap()[curP] = Cores.WATER_TILE;
				}
				
				if(tiles[curP] instanceof SandTile)
				{
					game.getMinimap()[curP] = Cores.SAND_TILE;
				}
				
				if(tiles[curP] instanceof DirtTile)
				{
					game.getMinimap()[curP] = Cores.DIRT_TILE;
				}
				
				if(tiles[curP] instanceof BarroTile)
				{
					game.getMinimap()[curP] = Cores.BARRO_TILE;
				}
				
				if(tiles[curP] instanceof GrassTile)
				{
					game.getMinimap()[curP] = Cores.GRASS_TILE;
				}
			}
		}
		
		for(int i = 0; i < game.getEntities().size(); i++)
		{
			Entity ent = game.getEntities().get(i);
			int index = ent.getX() / 16 + (ent.getY() / 16 * width);
			
			if(ent instanceof Enemy)
			{
				game.getMinimap()[index] = 0xffff0000;
			}
			
			if(ent instanceof Player)
			{
				game.getMinimap()[index] = 0xff0000ff;
			}
			
			if(ent instanceof Collectible)
			{
				game.getMinimap()[index] = 0xffffffff;
			}
		}
	}
	
	public static boolean canMove(int xn, int yn)
	{
		int x1 = xn / TILE_SIZE;
		int y1 = yn / TILE_SIZE;
		
		int x2 = (xn + TILE_SIZE -1) / TILE_SIZE;
		int y2 = yn / TILE_SIZE;
		
		int x3 = xn / TILE_SIZE;
		int y3 = (yn + TILE_SIZE -1) / TILE_SIZE;
		
		int x4 = (xn + TILE_SIZE -1) / TILE_SIZE;
		int y4 = (yn + TILE_SIZE -1) / TILE_SIZE;
		
		World world = Game.getGame().getWorld();
		
		int index1 = x1 + (y1 * world.getWidth());
		int index2 = x2 + (y2 * world.getWidth());
		int index3 = x3 + (y3 * world.getWidth());
		int index4 = x4 + (y4 * world.getWidth());
		
		return !(tiles[index1] instanceof WallTile 
				|| tiles[index2] instanceof WallTile
				|| tiles[index3] instanceof WallTile
				|| tiles[index4] instanceof WallTile
				|| tiles[index1] instanceof BushTile 
				|| tiles[index2] instanceof BushTile 
				|| tiles[index3] instanceof BushTile 
				|| tiles[index4] instanceof BushTile 
				|| tiles[index1] instanceof WaterTile 
				|| tiles[index2] instanceof WaterTile 
				|| tiles[index3] instanceof WaterTile 
				|| tiles[index4] instanceof WaterTile);
	}
}
