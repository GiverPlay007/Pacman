package me.giverplay.pacman.world;

import me.giverplay.pacman.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static me.giverplay.pacman.world.World.TILE_SIZE;

public class Tile {
  private static final Game game = Game.getGame();

  public static BufferedImage WALL_TILE = game.getSpritesheet().getSprite(0, 0, TILE_SIZE, TILE_SIZE);
  public static BufferedImage GRASS_TILE = game.getSpritesheet().getSprite(TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);

  private final BufferedImage sprite;

  private final int x;
  private final int y;

  public Tile(int x, int y, BufferedImage sprite) {
    this.x = x;
    this.y = y;
    this.sprite = sprite;
  }

  public void render(Graphics g) {
    g.drawImage(sprite, x, y, null);
  }
}
