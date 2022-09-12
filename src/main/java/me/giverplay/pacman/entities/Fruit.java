package me.giverplay.pacman.entities;

import java.awt.Graphics;

import me.giverplay.pacman.Game;
import me.giverplay.pacman.sound.Sounds;

public class Fruit extends Entity implements Collectible {

  public Fruit(int x, int y) {
    super(x, y, 16, 16, 1, Entity.SPRITE_FRUIT[random.nextInt(4)]);
  }

  public void collect() {
    game.getPlayer().addFruit();
    Sounds.collect.play();
    destroy();
  }

  @Override
  public void destroy() {
    Game.getGame().getFruits().remove(this);
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(super.getSprite(), getX() + 4, getY() + 4, 12, 12, null);
  }
}
