package me.giverplay.zelda.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.utils.GameState;

public class Menu
{
	private String[] options = {"Novo Jogo", "Carregar Jogo", "Sair do Jogo"};
	
	private int currentOption = 0;
	private int max = options.length - 1;
	
	public void render(Graphics g)
	{
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		g.setColor(Color.YELLOW);
		g.setFont(FontUtils.getFont(32, Font.BOLD));
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		
		int x = (Game.WIDTH * Game.SCALE - metrics.stringWidth("[ O Jogo ]")) / 2;
		int y = Game.HEIGHT * Game.SCALE / 2 - 50;
		
		g.drawString("[ O Jogo ]", x, y);
		
		g.setFont(FontUtils.getFont(18, Font.PLAIN));
		g.setColor(new Color(200, 200, 200));
		
		metrics = g.getFontMetrics(g.getFont());
		
		String cur = options[currentOption];
		int yStart = y + 60;
		
		if(Game.getGame().getState() == GameState.PAUSED)
		{
			options[0] = "Continuar Jogo";
		}
		else
		{
			options[0] = "Novo Jogo";
		}
		
		for(String a : options)
		{
			if(a.equals(cur))
			{
				String dr = "> " + cur + " <";
				x = (Game.WIDTH * Game.SCALE - metrics.stringWidth(dr)) / 2;
				g.drawString(dr, x, yStart);
				yStart += 40;
				
				continue;
			}
			
			x = (Game.WIDTH * Game.SCALE - metrics.stringWidth(a)) / 2;
			g.drawString(a, x, yStart);
			yStart += 40;
		}
	}
	
	public void handleOptionDown()
	{
		if(currentOption + 1 > max)
		{
			currentOption = 0;
			return;
		}
		
		currentOption++;
	}
	
	public void handleOptionUp()
	{
		if(currentOption - 1 < 0)
		{
			currentOption = max;
			return;
		}
		
		currentOption--;
	}

	public void handleSelect()
	{
		switch (currentOption)
		{
			case 0:
				Game.getGame().setState(GameState.NORMAL);
				break;
			
			case 2:
				System.exit(1);
			default:
				break;
		}
	}
}
