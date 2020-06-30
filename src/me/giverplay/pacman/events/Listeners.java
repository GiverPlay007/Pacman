package me.giverplay.pacman.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import me.giverplay.pacman.Game;

public class Listeners implements MouseListener, KeyListener
{
	private Game game;
	
	public Listeners(Game game)
	{
		this.game = game;
		this.game.addKeyListener(this);
		this.game.addMouseListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		if(!game.morreu())
		{
			if(event.getKeyCode() == KeyEvent.VK_SPACE)
			{
				game.getPlayer().handleJump();
			}
			
			if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)
			{
				game.getPlayer().setWalkingRight(true);
			} 
			
			if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
			{
				game.getPlayer().setWalkingLeft(true);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W)
			{
				game.getPlayer().setWalkingUp(true);
			} 
			
			if(event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S)
			{
				game.getPlayer().setWalkingDown(true);
			}
		}
		else
		{
			if(event.getKeyCode() == KeyEvent.VK_ENTER)
			{
				Game.handleRestart();
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)
		{
			game.getPlayer().setWalkingRight(false);
		} 
		
		if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
		{
			game.getPlayer().setWalkingLeft(false);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W)
		{
			game.getPlayer().setWalkingUp(false);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S)
		{
			game.getPlayer().setWalkingDown(false);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent event)
	{

	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{

	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{

	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{

	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{

	}
}
