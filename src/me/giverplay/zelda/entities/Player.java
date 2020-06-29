package me.giverplay.zelda.entities;

import static me.giverplay.zelda.world.World.TILE_SIZE;
import static me.giverplay.zelda.world.World.canMove;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.graphics.Camera;
import me.giverplay.zelda.utils.GameState;

public class Player extends Entity
{
	private static final int DIR_RIGHT = 0;
	private static final int DIR_LEFT = 1;
	private static final int DIR_UP = 3;
	private static final int DIR_DOWN = 2;
	private static final int MAX_FRAMES = 1;
	private static final int MAX_ANIMATIONS = 24;
	private static final int MAX_DAMAGED_FRAMES = 10;
	private static final int MAX_JUMPING_FRAMES = 15;
	
	private boolean up, down, left, right;
	
	private boolean moved = false;
	private boolean damaged = false;
	private boolean hasGun = false;
	private boolean shooting = false;
	private boolean mouseShooting = false;
	private boolean isJumping = false;
	private boolean jump = false;
	private boolean falling = false;
	
	private double speed = 2;
	
	private int frames = 0;
	private int currentDir = 0;
	private int current_animation = 0;
	private int jumpPos = 0;
	private int z = 0;
	
	private int dmgFrames = 0;
	private int maxVida = 100;
	private int vida = 100;
	private int ammo = 0;
	private int msx = 0;
	private int msy = 0;
	
	private ArrayList<BufferedImage> rightPlayer = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> upPlayer = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> leftPlayer = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> downPlayer = new ArrayList<BufferedImage>();
	
	private Game game;
	private Camera camera;
	
	public Player(int x, int y, int width, int height)
	{
		super(x, y, width, height, null);
		
		game = Game.getGame();
		camera = game.getCamera();
		
		for (int i = 0; i < 400; i += TILE_SIZE)
		{
			rightPlayer.add(game.getSpritesheet().getSprite(i, TILE_SIZE, getWidth(), getHeight()));
			leftPlayer.add(game.getSpritesheet().getSprite(i, TILE_SIZE * 2, getWidth(), getHeight()));
			downPlayer.add(game.getSpritesheet().getSprite(i, TILE_SIZE * 3, getWidth(), getHeight()));
			upPlayer.add(game.getSpritesheet().getSprite(i, TILE_SIZE * 4, getWidth(), getHeight()));
		}
		
		setDepth(2);
	}
	
	@Override
	public void tick()
	{
		if (jump)
		{
			jump = false;
			
			if (!isJumping)
			{
				isJumping = true;
			}
		}
		
		if (isJumping)
		{
			z = jumpPos;
			
			if (!falling)
				jumpPos++;
			
			if (jumpPos == MAX_JUMPING_FRAMES)
				falling = true;
			
			if (falling)
			{
				jumpPos--;
				z = jumpPos;
				
				if (jumpPos <= 0)
				{
					falling = false;
					isJumping = false;
				}
			}
		}
		
		moved = false;
		
		if(!(right && left))
		{
			if (right && canMove((int) (x + speed), getY()))
			{
				moveX(speed);
				moved = true;
			}
			
			if (left && canMove((int) (x - speed), getY()))
			{
				moveX(-speed);
				moved = true;
			}
		}
		
		if (!(up && down))
		{
			if (up && canMove(getX(), (int) (y - speed)))
			{
				moveY(-speed);
				moved = true;
			}
			
			if (down && canMove(getX(), (int) (y + speed)))
			{
				moveY(speed);
				moved = true;
			}
		}
		
		if (moved)
		{
			frames++;
			
			if (frames == MAX_FRAMES)
			{
				current_animation++;
				
				if (current_animation == MAX_ANIMATIONS)
				{
					current_animation = 0;
				}
				
				frames = 0;
			}
		}
		
		if (damaged)
		{
			dmgFrames++;
			
			if (dmgFrames >= MAX_DAMAGED_FRAMES)
			{
				dmgFrames = 0;
				setDamaged(false);
			}
		}
		
		if (isShooting())
		{
			if (getAmmo() > 0)
			{
				FireShot fs = new FireShot(getX() + TILE_SIZE / 2, getY() + TILE_SIZE / 2, 3, 3, 0, 0);
				fs.setDX(dirRight() ? 1 : (dirLeft() ? -1 : 0));
				fs.setDY(dirDown() ? 1 : (dirUp() ? -1 : 0));
				
				modifyAmmo(-1);
				game.getShoots().add(fs);
				
			}
			
			setShooting(false);
		} else if (isMouseShooting())
		{
			if (getAmmo() > 0)
			{
				
				double angle = Math.atan2(msy - (getY() + 8 - camera.getY()), msx - (getX() + 8 - camera.getX()));
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				FireShot fs = new FireShot(getX() + TILE_SIZE / 2, getY() + TILE_SIZE / 2, 3, 3, dx, dy);
				
				modifyAmmo(-1);
				game.getShoots().add(fs);
				
			}
			
			setMouseShooting(false, 0, 0);
		}
		
		checkItems();
		
		if (getLife() <= 0)
		{
			Game.getGame().setState(GameState.GAME_OVER);
		}
		
		camera.setX(camera.clamp(getX() - (Game.WIDTH / 2), 0, game.getWorld().getHeight() * TILE_SIZE - Game.WIDTH));
		camera.setY(camera.clamp(getY() - (Game.HEIGHT / 2), 0, game.getWorld().getWidth() * TILE_SIZE - Game.HEIGHT));
	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(new Color(50, 50, 50));
		
		g.fillOval(getX() - camera.getX() + TILE_SIZE / 2 - 4, getY() - camera.getY() + 11, 8, 4);
		
		if (dirRight())
		{
			g.drawImage(rightPlayer.get(damaged ? rightPlayer.size() - 1 : (moved ? current_animation : 0)),
					getX() - camera.getX(), getY() - camera.getY() - z, null);
		}
		
		if (dirLeft())
		{
			g.drawImage(leftPlayer.get(damaged ? leftPlayer.size() - 1 : (moved ? current_animation : 0)),
					getX() - camera.getX(), getY() - camera.getY() - z, null);
		}
		
		if (dirUp())
		{
			g.drawImage(upPlayer.get(damaged ? upPlayer.size() - 1 : (moved ? current_animation : 0)), getX() - camera.getX(),
					getY() - camera.getY() - z, null);
		}
		
		if (dirDown())
		{
			g.drawImage(downPlayer.get(damaged ? downPlayer.size() - 1 : (moved ? current_animation : 0)),
					getX() - camera.getX(), getY() - camera.getY() - z, null);
		}
		
		if (hasGun())
		{
			g.drawImage(Entity.sprites_gun.get(currentDir), getX() - camera.getX(), getY() - camera.getY() + 1 - z, null);
		}
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
		if (walking)
			currentDir = DIR_RIGHT;
		if (left)
			currentDir = DIR_LEFT;
	}
	
	public void setWalkingLeft(boolean walking)
	{
		this.left = walking;
		if (walking)
			currentDir = DIR_LEFT;
		if (right)
			currentDir = DIR_RIGHT;
	}
	
	public void setWalkingUp(boolean walking)
	{
		this.up = walking;
		if (walking)
			currentDir = DIR_UP;
		if (down)
			currentDir = DIR_DOWN;
	}
	
	public void setWalkingDown(boolean walking)
	{
		this.down = walking;
		if (walking)
			currentDir = DIR_DOWN;
		if (up)
			currentDir = DIR_UP;
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
	
	public int getAmmo()
	{
		return this.ammo;
	}
	
	public void modifyAmmo(int toModify)
	{
		ammo += toModify;
		
		if (ammo < 0)
			ammo = 0;
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
	
	public boolean hasGun()
	{
		return this.hasGun;
	}
	
	public void setGun(boolean toSet)
	{
		this.hasGun = toSet;
	}
	
	public void setShooting(boolean toSet)
	{
		if (toSet)
		{
			if (hasGun)
			{
				this.shooting = toSet;
			}
			
			return;
		}
		
		this.shooting = toSet;
	}
	
	public void setMouseShooting(boolean toSet, int xPos, int yPos)
	{
		this.msx = xPos;
		this.msy = yPos;
		
		if (toSet)
		{
			if (hasGun)
			{
				this.mouseShooting = toSet;
			}
			
			return;
		}
		
		this.mouseShooting = toSet;
	}
	
	public void handleJump()
	{
		if (!isJumping)
			jump = true;
	}
	
	public boolean isJumping()
	{
		return this.isJumping;
	}
	
	public boolean isShooting()
	{
		return this.shooting;
	}
	
	public boolean isMouseShooting()
	{
		return this.mouseShooting;
	}
	
	public boolean dirLeft()
	{
		return currentDir == DIR_LEFT;
	}
	
	public boolean dirUp()
	{
		return currentDir == DIR_UP;
	}
	
	public boolean dirDown()
	{
		return currentDir == DIR_DOWN;
	}
	
	public boolean dirRight()
	{
		return currentDir == DIR_RIGHT;
	}
	
	private void checkItems()
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
