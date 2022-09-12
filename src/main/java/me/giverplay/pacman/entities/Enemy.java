package me.giverplay.pacman.entities;

import me.giverplay.pacman.algorithms.AStar;
import me.giverplay.pacman.algorithms.Vector2i;

import java.awt.Graphics;

public class Enemy extends Entity {
  private int ghostFrames = 0;
  private int maxGhostFrames = random.nextInt(60 * 5 - 60 * 3) + 60 * 3;

  private boolean ghostMode = false;

  private final Player player;

  public Enemy(double x, double y, int width, int height) {
    super(x, y, width, height, 1, SPRITE_GHOST[random.nextInt(4)]);

    maxLife = 1;
    life = 1;

    player = game.getPlayer();
    setDepth(1);
  }

  @Override
  public void tick() {
    ghostFrames++;

    if(ghostFrames >= maxGhostFrames) {
      maxGhostFrames = random.nextInt(60 * 5 - 60 * 3) + 60 * 3;
      ghostFrames = 0;
      ghostMode = !ghostMode;
    }

    if(ghostMode)
      return;

    if(isColliding(player)) {
      player.damage();
    }

    if(path == null || path.size() == 0) {
      path = AStar.findPath(
        game.getWorld(),
        new Vector2i(getX() / 16, getY() / 16),
        new Vector2i(player.getX() / 16, player.getY() / 16)
      );
    }

    followPath(path);
  }

  @Override
  public void render(Graphics g) {
    super.render(g);

    if(ghostMode) {
      g.drawImage(GHOST, getX(), getY(), null);
    }
  }

  public int getLife() {
    return this.life;
  }

  public void damage(int toDamage) {
    life += toDamage;

    if(life < 0) life = 0;
    if(life > 10) life = 10;
  }
}
