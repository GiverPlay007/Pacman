package me.giverplay.zelda.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.algorithms.Node;
import me.giverplay.zelda.algorithms.Vector2i;
import me.giverplay.zelda.world.World;

public class Entity
{
	private static Game game = Game.getGame();
	
	public static final BufferedImage ENTITY_LIFE_PACK = game.getSpritesheet().getSprite(0, 112, 16, 16);
	public static final BufferedImage ENTITY_BULLET = game.getSpritesheet().getSprite(16, 112, 16, 16);
	public static final BufferedImage ENTITY_WEAPON = game.getSpritesheet().getSprite(0, 128, 16, 16);
	public static final BufferedImage ENTITY_ENEMY = game.getSpritesheet().getSprite(0, 144, 16, 16);
	
	public static ArrayList<BufferedImage> sprites_enemies = new ArrayList<>();;
	public static ArrayList<BufferedImage> feedback_enemies = new ArrayList<>();;
	public static ArrayList<BufferedImage> sprites_gun = new ArrayList<>();
	
	protected List<Node> path;
	
	static
	{
		for(int i = World.TILE_SIZE; i <= World.TILE_SIZE * 4; i += World.TILE_SIZE)
		{
			sprites_gun.add(game.getSpritesheet().getSprite(i, 128, World.TILE_SIZE, World.TILE_SIZE));
		}
		
		for(int i = 0; i < 64; i += World.TILE_SIZE)
		{
			sprites_enemies.add(game.getSpritesheet().getSprite(i, 144, World.TILE_SIZE, World.TILE_SIZE));
			feedback_enemies.add(game.getSpritesheet().getSprite(i + (World.TILE_SIZE * 4), 144, World.TILE_SIZE, World.TILE_SIZE));
		}
	};
	
	protected double x;
	protected double y;
	private int width;
	private int height;
	private int depth;
	
	protected int mx, my, mw, mh;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.depth = 0;
		
		this.sprite = sprite;
		
		mx = 0;
		my = 0;
		mw = width;
		mh = height;
	}
	
	public void tick()
	{
		
	}
	
	public void render(Graphics g)
	{
		g.drawImage(sprite, (int) getX() - game.getCamera().getX(), (int) getY() - game.getCamera().getY(), null);
		//g.setColor(Color.BLUE);
		//g.fillRect(getX() + getMaskX() - Game.camera.getX(), getY() + getMaskY() - Game.camera.getY(), mw, mh);
	}
	
	public void destroy()
	{
		
	}
	
	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight)
	{
		this.mx = maskX;
		this.my = maskY;
		this.mw = maskWidth;
		this.mh = maskHeight;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setDepth(int toSet)
	{
		this.depth = toSet;
	}
	
	public void moveX(double d)
	{
		x += d;
	}
	
	public void moveY(double d)
	{
		y += d;
	}
	
	private int getMaskX()
	{
		return this.mx;
	}
	
	private int getMaskY()
	{
		return this.my;
	}
	
	private int getMaskWidth()
	{
		return this.mw;
	}
	
	private int getMaskHeight()
	{
		return this.mh;
	}
	
	public int getX()
	{
		return (int) this.x;
	}
	
	public int getY()
	{
		return (int) this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getDepth()
	{
		return this.depth;
	}
	
	public BufferedImage getSprite()
	{
		return this.sprite;
	}
	
	public void followPath(List<Node> path)
	{
		if(path != null)
		{
			if(path.size() > 0)
			{
				Vector2i target = path.get(path.size() - 1).getTile();
				
				if(x < target.x * World.TILE_SIZE && !isColliding(getX() +1, getY()))
				{
					x++;
				}
				else if(x > target.x * World.TILE_SIZE && !isColliding(getX() -1, getY()))
				{
					x--;
				}
				
				if(y < target.y * World.TILE_SIZE && !isColliding(getX(), getY() +1))
				{
					y++;
				}
				else if(y > target.y * World.TILE_SIZE && !isColliding(getX(), getY() -1))
				{
					y--;
				}
				
				if(x == target.x * World.TILE_SIZE && y == target.y * World.TILE_SIZE)
					path.remove(path.size() -1);
			}
		}
	}
	
	public boolean isCollidingPlayer()
	{
		Rectangle thisRec = new Rectangle(this.getX() + mx, this.getY() + my, mw, mh);
		Rectangle playerR = new Rectangle(Game.getGame().getPlayer().getX(), Game.getGame().getPlayer().getY(), 16, 16);
		
		return thisRec.intersects(playerR);
	}
	
	public boolean isColliding(int xn, int yn)
	{
		Rectangle thisRec = new Rectangle(xn + mx, yn + my, mw, mh);
		
		for(Enemy e : game.getEnemies())
		{
			if(e == this)
				continue;
			
			Rectangle rec = new Rectangle(e.getX() + mx, e.getY() + my, mw, mh);
			
			if(thisRec.intersects(rec)) return true;
		}
		
		return false;
	}
	
	public double pointDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x2 - x1) * (x2 - x1) + ((y2 - y1) * (y2 - y1)));
	}
	
	public static boolean isCollifingEntity(Entity e1, Entity e2)
	{
		Rectangle e1m = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getMaskWidth(), e1.getMaskHeight());
		Rectangle e2m = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getMaskWidth(), e2.getMaskHeight());
		
		return e1m.intersects(e2m);
	}
	
	public static Comparator<Entity> sortDepth = new Comparator<Entity>()
	{
		@Override
		public int compare(Entity e0, Entity e1)
		{
			return (e1.getDepth() < e0.getDepth() ? +1 : e1.getDepth() > e0.getDepth() ? -1 : 0);
		}
	};
}
