package me.giverplay.zelda.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import me.giverplay.zelda.Game;

public class UI
{
	private Game game;
	
	public UI()
	{
		game = Game.getGame();
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillRect(10, 9, (int) (game.getPlayer().getMaxLife() * 0.70), 9);
		
		g.setColor(Color.GREEN);
		g.fillRect(10, 9, (int) (game.getPlayer().getLife() * 0.70), 9);
		
		g.setColor(new Color(20, 50, 20));
		g.setFont(FontUtils.getFont(8, Font.PLAIN));
		g.drawString(game.getPlayer().getLife() + " / " + game.getPlayer().getMaxLife(), 26, 22);
		
		g.setColor(Color.BLACK);
		g.drawRect(9, 8, 70, 10);
	}
}
