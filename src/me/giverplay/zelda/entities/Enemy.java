package me.giverplay.zelda.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.algorithms.AStar;
import me.giverplay.zelda.algorithms.Vector2i;
import me.giverplay.zelda.world.World;

public class Enemy extends Entity
{
	private static int MAX_FRAMES = 8;
	private static int MAX_DAMAGED_FRAMES = 10;
	
	private int spriteAtual = 0;
	private int frame = 0;
	private int life = 10;
	private int damaged_frames = 0;
	
	//private double speed = 1;
	
	private boolean damaged = false;
	
	private Player player;
	private Game game;
	
	public Enemy(double x, double y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, null);
		
		setMask(4, 6, 8, 10);
		
		game = Game.getGame();
		player = game.getPlayer();
		
		setDepth(1);
	}
	
	@Override
	public void tick()
	{
		/*
		if(pointDistance(getX(), getY(), player.getX(), player.getY()) < 100)
		{
			if(!isCollidingPlayer())
			{
				if(getX() < player.getX() 
						&& canMove((int) (x + speed), getY())
						&& !isColliding((int) (x + speed), getY())){
					moveX(speed);
				}
				else if(getX() > player.getX() 
						&& canMove((int) (x - speed), getY())
						&& !isColliding((int) (x - speed), getY())){
					moveX(-speed);
				}
				
				if(getY() < player.getY() 
						&& canMove(getX(), (int) (y + speed))
						&& !isColliding(getX(), (int) (y + speed))){
					moveY(speed);
				}
				else if(getY() > player.getY() 
						&& canMove(getX(), (int) (y - speed))
						&& !isColliding(getX(), (int) (y - speed))){
					moveY(-speed);
				}
			}
			else
			{
				if(game.getRandom().nextInt(100) < 10)
				{
					player.modifyLife(-game.getRandom().nextInt(5));
					player.setDamaged(true);
				}
			}
		}
		*/
		
		if(path == null || path.size() == 0)
		{
			path = AStar.findPath(game.getWorld(), new Vector2i((int) (getX() / 16), (int) (getY() / 16)), new Vector2i((int) (player.getX() / 16), (int) (player.getY() / 16)));
		}
		
		followPath(path);
		
		if(damaged)
		{
			damaged_frames++;
			
			if(damaged_frames >= MAX_DAMAGED_FRAMES)
			{
				damaged = false;
				damaged_frames = 0;
			}
		}
		
		if(life == 0)
		{
			destroy();
			return;
		}
		
		frame++;
		
		if(frame >= MAX_FRAMES)
		{
			spriteAtual++;
			frame = 0;
		}
		
		if(spriteAtual >= Entity.sprites_enemies.size())
			spriteAtual = 0;
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage((!damaged ? sprites_enemies : feedback_enemies).get(spriteAtual), getX() - game.getCamera().getX(), getY() - game.getCamera().getY(), World.TILE_SIZE, World.TILE_SIZE, null);
	}
	
	@Override
	public void destroy()
	{
		game.getEnemies().remove(this);
		game.getEntities().remove(this);
	}
	
	public int getLife()
	{
		return this.life;
	}
	
	public int getMaskX()
	{
		return mx;
	}
	
	public int getMaskY()
	{
		return my;
	}
	
	public int getMaskWidth()
	{
		return mw;
	}
	
	public int getMaskHeight()
	{
		return mh;
	}
	
	public void damage(int toDamage)
	{
		life += toDamage;
		
		damaged = true;
		
		if(life < 0) life = 0;
		if(life > 10) life = 10;
	}
}
