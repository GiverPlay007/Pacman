package me.giverplay.zelda.events;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import me.giverplay.zelda.Game;
import me.giverplay.zelda.utils.GameState;

public class Listeners implements MouseListener, KeyListener
{
	@Override
	public void keyPressed(KeyEvent event)
	{
		if(Game.getGame().getState() == GameState.NORMAL)
		{
			if(event.getKeyCode() == KeyEvent.VK_SPACE)
			{
				Game.getGame().getPlayer().handleJump();
			}
			
			if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)
			{
				Game.getGame().getPlayer().setWalkingRight(true);
			} 
			
			if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
			{
				Game.getGame().getPlayer().setWalkingLeft(true);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W)
			{
				Game.getGame().getPlayer().setWalkingUp(true);
			} 
			
			if(event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S)
			{
				Game.getGame().getPlayer().setWalkingDown(true);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_F)
			{
				Game.getGame().getPlayer().setShooting(true);
			}
			
			if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
			{
				Game.getGame().setState(GameState.PAUSED);
			}
		}
		else if(Game.getGame().getState() == GameState.MENU || Game.getGame().getState() == GameState.PAUSED)
		{
			if(event.getKeyCode() == KeyEvent.VK_UP)
			{
				Game.getGame().getMenu().handleOptionUp();
			}
			
			if(event.getKeyCode() == KeyEvent.VK_DOWN)
			{
				Game.getGame().getMenu().handleOptionDown();
			}
			
			if(event.getKeyCode() == KeyEvent.VK_ENTER)
			{
				Game.getGame().getMenu().handleSelect();
			}
		}
		else if(Game.getGame().getState() == GameState.GAME_OVER)
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
			Game.getGame().getPlayer().setWalkingRight(false);
		} 
		
		if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
		{
			Game.getGame().getPlayer().setWalkingLeft(false);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_W)
		{
			Game.getGame().getPlayer().setWalkingUp(false);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_S)
		{
			Game.getGame().getPlayer().setWalkingDown(false);
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
		Game.getGame().getPlayer().setMouseShooting(true, e.getX(), e.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{

	}
}
