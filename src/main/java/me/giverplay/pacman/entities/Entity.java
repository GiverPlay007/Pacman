package me.giverplay.pacman.entities;

import me.giverplay.pacman.Game;
import me.giverplay.pacman.algorithms.Node;
import me.giverplay.pacman.algorithms.Vector2i;
import me.giverplay.pacman.world.World;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static me.giverplay.pacman.world.World.TILE_SIZE;

public class Entity {
  public static final BufferedImage[] SPRITE_PLAYER;
  public static final BufferedImage[] SPRITE_GHOST;
  public static final BufferedImage[] SPRITE_FRUIT;

  public static final BufferedImage GHOST = Game.getGame().getSpritesheet().getSprite(TILE_SIZE * 2, 0, TILE_SIZE, TILE_SIZE);
  public static Comparator<Entity> sortDepth = Comparator.comparingInt(Entity::getDepth);

  protected static Random random = new Random();

  private final int width;
  private final int height;

  protected Game game = Game.getGame();
  protected List<Node> path;

  protected int life = 0;
  protected int maxLife = 0;

  protected double x;
  protected double y;
  protected double speed;

  private final BufferedImage sprite;
  private int depth;

  public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.speed = speed;
    this.depth = 0;

    this.sprite = sprite;
  }

  public boolean isColliding(Entity e) {
    Rectangle eRect = new Rectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
    Rectangle selfRect = new Rectangle(getX(), getY(), getWidth(), getHeight());

    return eRect.intersects(selfRect);
  }

  public void tick() {
  }

  public void render(Graphics g) {
    g.drawImage(sprite, getX(), getY(), null);
  }

  public void destroy() {
    game.removeEntity(this);
  }

  public void moveX(double d) {
    x += d;
  }

  public void moveY(double d) {
    y += d;
  }

  public int getX() {
    return (int) this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return (int) this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public int getDepth() {
    return this.depth;
  }

  public void setDepth(int toSet) {
    this.depth = toSet;
  }

  public BufferedImage getSprite() {
    return this.sprite;
  }

  public void followPath(List<Node> path) {
    if(path != null) {
      if(path.size() > 0) {
        Vector2i target = path.get(path.size() - 1).getTile();

        if(x < target.x * World.TILE_SIZE) {
          x++;
        } else if(x > target.x * World.TILE_SIZE) {
          x--;
        }

        if(y < target.y * World.TILE_SIZE) {
          y++;
        } else if(y > target.y * World.TILE_SIZE) {
          y--;
        }

        if(x == target.x * World.TILE_SIZE && y == target.y * World.TILE_SIZE)
          path.remove(path.size() - 1);
      }
    }
  }

  static {
    SPRITE_GHOST = new BufferedImage[4];
    SPRITE_FRUIT = new BufferedImage[4];
    SPRITE_PLAYER = new BufferedImage[4];

    for(int i = 0; i < 4; i++) {
      SPRITE_PLAYER[i] = Game.getGame().getSpritesheet().getSprite(i * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE);
      SPRITE_FRUIT[i] = Game.getGame().getSpritesheet().getSprite(i * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      SPRITE_GHOST[i] = Game.getGame().getSpritesheet().getSprite(i * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }
}
