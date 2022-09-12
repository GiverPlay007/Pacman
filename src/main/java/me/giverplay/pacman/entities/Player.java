package me.giverplay.pacman.entities;

import me.giverplay.pacman.graphics.GraphicsUtils;
import me.giverplay.pacman.sound.Sounds;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static me.giverplay.pacman.world.World.canMove;

public class Player extends Entity {
  private static final int DIR_RIGHT = 0;
  private static final int DIR_DOWN = 90;
  private static final int DIR_LEFT = 180;
  private static final int DIR_UP = 270;

  private static final int MAX_FRAMES_ANIM = 5;

  private boolean up, down, left, right;

  private boolean closingMouth = false;
  private boolean canDamage = false;

  private int invincible = 0;

  private int fruits = 0;
  private int anim = 0;
  private int animFrames = 0;
  private int dir = 0;

  public Player(int x, int y, int width, int height) {
    super(x, y, width, height, 2, null);

    life = 5;
    maxLife = 5;

    setDepth(2);
  }

  @Override
  public void tick() {
    if(life == 0) {
      game.handleDeath();
      return;
    }

    if(!canDamage) {
      invincible++;

      if(invincible >= 30) {
        invincible = 0;
        canDamage = true;
      }
    }

    if(!(right && left)) {
      if(right) {
        if(canMove((int) (x + speed), getY())) moveX(speed);
      } else if(left) {
        if(canMove((int) (x - speed), getY())) moveX(-speed);
      }
    }

    if(!(up && down)) {
      if(up) {
        if(canMove(getX(), (int) (y - speed))) moveY(-speed);
      } else if(down) {
        if(canMove(getX(), (int) (y + speed))) moveY(speed);
      }
    }

    animFrames++;

    if(animFrames >= MAX_FRAMES_ANIM) {
      animFrames = 0;

      if(!closingMouth)
        anim++;
      else
        anim--;

      if(anim >= Entity.SPRITE_PLAYER.length) {
        anim--;
        closingMouth = !closingMouth;
      } else if(anim < 0) {
        anim++;
        closingMouth = !closingMouth;
      }
    }

    checkItems();
  }

  @Override
  public void render(Graphics g) {
    BufferedImage image = SPRITE_PLAYER[anim].getSubimage(0, 0, 16, 16);

    if(dir != DIR_RIGHT) {
      if(dir == DIR_LEFT) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(), 0);

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
      } else {
        image = GraphicsUtils.rotate(image, dir);
      }
    }

    g.drawImage(image, getX(), getY(), null);
  }

  public void setWalkingRight(boolean walking) {
    this.right = walking;
    this.dir = DIR_RIGHT;

    if(!walking && left)
      dir = DIR_LEFT;
  }

  public void setWalkingLeft(boolean walking) {
    this.left = walking;
    this.dir = DIR_LEFT;

    if(!walking && right)
      dir = DIR_RIGHT;
  }

  public void setWalkingUp(boolean walking) {
    this.up = walking;
    this.dir = DIR_UP;

    if(!walking && down)
      dir = DIR_DOWN;
  }

  public void setWalkingDown(boolean walking) {
    this.down = walking;
    this.dir = DIR_DOWN;

    if(!walking && up)
      dir = DIR_UP;
  }

  public int getLife() {
    return life;
  }

  public int getMaxLife() {
    return this.maxLife;
  }

  public void damage() {
    if(!canDamage)
      return;

    canDamage = false;

    life--;

    if(life < 0)
      life = 0;

    Sounds.hit.play();
  }

  public void checkItems() {
    for(int i = 0; i < game.getFruits().size(); i++) {
      Entity entity = game.getFruits().get(i);

      if(isColliding(entity)) {
        ((Collectible) entity).collect();
      }
    }
  }

  public void addFruit() {
    fruits++;
  }

  public int getFruits() {
    return this.fruits;
  }
}
