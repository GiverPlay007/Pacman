package me.giverplay.zelda.graphics;

public class Camera
{
	private int x = 0;
	private int y = 0;
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int clamp(int atual, int min, int max)
	{
		
		if(atual < min)
		  atual = min;
		
		if(atual > max)
			atual = max;
		
		return atual;
	}
}
