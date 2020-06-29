package me.giverplay.zelda.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import me.giverplay.zelda.Game;

public class FireShot extends Entity
{
	private static final int MAX_LIFE = 60;
	
	private int life = 0;
	
	private double dx = 0;
	private double dy = 0;
	private double spd = 5.0D;
	
	private Game game;
	
	public FireShot(double x, double y, int width, int height, double dx, double dy)
	{
		super(x, y, width, height, null);
		
		game = Game.getGame();
		
		this.dx = dx;
		this.dy = dy;
	}	
	
	@Override
	public void tick()
	{
		life++;
		
		if(life >= MAX_LIFE)
		{
			destroy();
		}
		
		moveX(dx * spd);
		moveY(dy * spd);
		
		checkCollisionEnemy();
	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(new Color(0xFF900AFF));
		g.fillOval(getX() - game.getCamera().getX(), getY() - game.getCamera().getY(), getWidth(), getHeight());
	}
	
	@Override
	public void destroy()
	{
		game.getShoots().remove(this);
	}
	
	public int getDX()
	{
		return (int) this.dx;
	}
	
	public int getDY()
	{
		return (int) this.dy;
	}
	
	public void setDX(double toSet)
	{
		this.dx = toSet;
	}
	
	public void setDY(double toSet)
	{
		this.dy = toSet;
	}
	
	public void moveDX(double toMove)
	{
		this.dx += toMove;
	}
	
	public void moveDY(double toMove)
	{
		this.dy += toMove;
	}
	
	private void checkCollisionEnemy()
	{
		for(Entity e : game.getEntities())
		{
			if(e instanceof Enemy)
			{
				Enemy en = (Enemy) e;
				Rectangle rec = new Rectangle(en.getX() + en.getMaskX(), en.getY() + en.getMaskY(), en.getWidth(), en.getHeight());
				Rectangle mask = new Rectangle(getX(), getY(), getWidth(), getHeight());
				
				if(rec.intersects(mask))
				{
					en.damage(-2);
					destroy();
					break;
				}
			}
		}
	}
}
