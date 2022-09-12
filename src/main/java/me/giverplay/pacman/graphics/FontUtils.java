package me.giverplay.pacman.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FontUtils {
  private static Font font;

  static {
    try(InputStream stream = FontUtils.class.getResourceAsStream("/Font.ttf")) {
      font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(stream));
    } catch(FontFormatException e) {
      System.out.println("Falha no formato de fonte, inicializando com fonte padrão");
    } catch(IOException e) {
      System.out.println("Falha na leitura do arquivo de fonte, inicializando com fonte padrão");
    }
  }

  public static Font getFont(int size, int style) {
    return (font != null ? font.deriveFont((float) size).deriveFont(style) : new Font("arial", style, size));
  }
}
