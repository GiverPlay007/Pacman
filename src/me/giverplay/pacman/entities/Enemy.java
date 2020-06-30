package me.giverplay.pacman.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.giverplay.pacman.Game;
import me.giverplay.pacman.algorithms.AStar;
import me.giverplay.pacman.algorithms.Vector2i;

public class Enemy extends Entity
{
	private int life = 10;
	
	private Player player;
	private Game game;
	
	public Enemy(double x, double y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, 1, sprite);
		
		game = Game.getGame();
		player = game.getPlayer();
		
		setDepth(1);
	}
	
	@Override
	public void tick()
	{
		if(path == null || path.size() == 0)
		{
			path = AStar.findPath(game.getWorld(), new Vector2i((int) (getX() / 16), (int) (getY() / 16)), new Vector2i((int) (player.getX() / 16), (int) (player.getY() / 16)));
		}
		
		followPath(path);
	}
	
	@Override
	public void render(Graphics g)
	{
		
	}
	
	public int getLife()
	{
		return this.life;
	}
	
	public void damage(int toDamage)
	{
		life += toDamage;
		
		if(life < 0) life = 0;
		if(life > 10) life = 10;
	}
}
