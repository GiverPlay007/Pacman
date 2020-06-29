package me.giverplay.zelda.entities;

import java.awt.image.BufferedImage;

import me.giverplay.zelda.Game;

public class LifePack extends Entity implements Collectible
{
	public LifePack(double x, double y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, sprite);
	}

	@Override
	public void collect()
	{
		Game.getGame().getPlayer().modifyLife(10);
		destroy();
	}
}
