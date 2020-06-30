package me.giverplay.pacman.entities;

import static me.giverplay.pacman.world.World.canMove;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.giverplay.pacman.Game;

public class Player extends Entity
{
	private boolean up, down, left, right;
	
	private boolean damaged = false;
	private boolean isJumping = false;
	private int maxVida = 100;
	private int vida = 100;
	
	private Game game;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, 2, sprite);
		game = Game.getGame();
		
		setDepth(2);
	}
	
	@Override
	public void tick()
	{
		if(!(right && left))
		{
			if(right)
			{
				if(canMove((int) (x + speed), getY())) moveX(speed);
			}
			else if(left)
			{
				if(canMove((int) (x - speed), getY())) moveX(-speed);
			}
		}
		
		if(!(up && down))
		{
			if(up)
			{
				if(canMove(getX(), (int) (y - speed))) moveY(-speed);
			}
			else if(down)
			{
				if(canMove(getX(), (int) (y + speed))) moveY(speed);
			}
		}
	}
	
	@Override
	public void render(Graphics g)
	{
		super.render(g);
	}
	
	public boolean walkingRight()
	{
		return this.right;
	}
	
	public boolean walkingLeft()
	{
		return this.left;
	}
	
	public boolean walkingDown()
	{
		return this.down;
	}
	
	public boolean walkingUp()
	{
		return this.up;
	}
	
	public void setWalkingRight(boolean walking)
	{
		this.right = walking;
	}
	
	public void setWalkingLeft(boolean walking)
	{
		this.left = walking;
	}
	
	public void setWalkingUp(boolean walking)
	{
		this.up = walking;
	}
	
	public void setWalkingDown(boolean walking)
	{
		this.down = walking;
	}
	
	public int getLife()
	{
		return vida;
	}
	
	public void modifyLife(int toModify)
	{
		vida += toModify;
		
		if (vida < 0)
			vida = 0;
		if (vida > maxVida)
			vida = maxVida;
	}
	
	public int getMaxLife()
	{
		return this.maxVida;
	}
	
	public boolean isDamaged()
	{
		return this.damaged;
	}
	
	public void setDamaged(boolean toDamage)
	{
		if (toDamage && isJumping)
			return;
		
		this.damaged = toDamage;
	}
	
	public void handleJump()
	{
		if (!isJumping)
		{
		}
	}
	
	public boolean isJumping()
	{
		return this.isJumping;
	}
	
	public void checkItems()
	{
		for (int i = 0; i < game.getEntities().size(); i++)
		{
			Entity entity = game.getEntities().get(i);
			
			if (entity != this && !(entity instanceof Enemy) && isCollifingEntity(this, entity))
			{
				if(entity instanceof Collectible)
					((Collectible) entity).collect();
			}
		}
	}
}
