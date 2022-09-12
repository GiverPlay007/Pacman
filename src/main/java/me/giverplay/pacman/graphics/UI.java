package me.giverplay.pacman.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.giverplay.pacman.Game;
import me.giverplay.pacman.entities.Entity;
import me.giverplay.pacman.entities.Player;

import static me.giverplay.pacman.Game.HEIGHT;
import static me.giverplay.pacman.Game.WIDTH;

public class UI {
  private static final BufferedImage filledHeart = Game.getGame().getSpritesheet().getSprite(48, 0, 8, 8);
  private static final BufferedImage heart = Game.getGame().getSpritesheet().getSprite(56, 0, 8, 8);

  private final Game game;

  public UI() {
    game = Game.getGame();
  }

  public void render(Graphics g) {
    g.drawImage(Entity.SPRITE_FRUIT[0], 2, HEIGHT * 2 - 34, 28, 34, null);

    Player player = game.getPlayer();

    g.setColor(Color.yellow);
    g.setFont(FontUtils.getFont(14, Font.BOLD));
    g.drawString(player.getFruits() + "/" + (game.getFruits().size() + player.getFruits()), 36, HEIGHT * 2 - 8);

    int coe = player.getMaxLife() - player.getLife();

    for(int i = player.getMaxLife() * 8; i >= 0; i -= 10) {
      g.drawImage(i <= coe * 8 ? heart : filledHeart, WIDTH * 2 - 32 - i * 4, HEIGHT * 2 - 32, 28, 28, null);
    }
  }
}
