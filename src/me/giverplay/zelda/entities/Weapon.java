package me.giverplay.zelda.entities;

import java.awt.image.BufferedImage;

import me.giverplay.zelda.Game;

public class Weapon extends Entity implements Collectible
{
	public Weapon(double x, double y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, sprite);
	}

	@Override
	public void collect()
	{
		Game.getGame().getPlayer().setGun(true);
		Game.getGame().getEntities().remove(this);
	}
}
