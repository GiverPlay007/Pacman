package me.giverplay.pacman.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Spritesheet {
  private BufferedImage spritesheet;

  public Spritesheet(String path) {
    try {
      spritesheet = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
    } catch(IOException e) {
      System.out.println("Erro na leitura do Spritesheet");
      System.exit(1);
    }
  }

  public BufferedImage getSprite(int x, int y, int width, int height) {
    return spritesheet.getSubimage(x, y, width, height);
  }
}
