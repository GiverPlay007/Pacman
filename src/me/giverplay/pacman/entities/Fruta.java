package me.giverplay.pacman.entities;

import java.awt.Graphics;

import me.giverplay.pacman.Game;
import me.giverplay.pacman.sound.Sound;

public class Fruta extends Entity implements Collectible
{
	public Fruta(int x, int y)
	{
		super(x, y, 16, 16, 1, Entity.SPRITE_FRUTA[random.nextInt(4)]);
	}
	
	public void collect() 
	{
		Game.getGame().getPlayer().addFruit();
		Sound.collect.play();
		destroy();
	}
	
	@Override
	public void destroy()
	{
		Game.getGame().getFruits().remove(this);
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(super.getSprite(), getX() + 4, getY() + 4, 12, 12, null);
	}
}
