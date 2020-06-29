package me.giverplay.zelda.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontUtils
{
	private static InputStream stream = FontUtils.class.getResourceAsStream("/Fontezinha.ttf");
	private static Font font;
	
	public static void init()
	{
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} 
		catch (FontFormatException e)
		{
			System.out.println("Falha no formato de fonte, inicializando com fonte padrão");
		} 
		catch (IOException e)
		{
			System.out.println("Falha na leitura do arquivo de fonte, inicializando com fonte padrão");
		}
	}
	
	public static Font getFont(int size, int style)
	{
		return (font != null ? font.deriveFont((float) size).deriveFont(style) : new Font("arial", style, size));
	}
}
