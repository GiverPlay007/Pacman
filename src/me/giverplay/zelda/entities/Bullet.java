package me.giverplay.zelda.entities;

import java.awt.image.BufferedImage;

import me.giverplay.zelda.Game;

public class Bullet extends Entity implements Collectible
{
	public Bullet(double x, double y, int width, int height, BufferedImage sprite)
	{
		super(x, y, width, height, sprite);
	}

	@Override
	public void collect()
	{
		Game.getGame().getPlayer().modifyAmmo(50);
		Game.getGame().getEntities().remove(this);
	}
}
