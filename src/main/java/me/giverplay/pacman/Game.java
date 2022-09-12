package me.giverplay.pacman;

import me.giverplay.pacman.entities.Enemy;
import me.giverplay.pacman.entities.Entity;
import me.giverplay.pacman.entities.Player;
import me.giverplay.pacman.events.Listeners;
import me.giverplay.pacman.graphics.FontUtils;
import me.giverplay.pacman.graphics.Spritesheet;
import me.giverplay.pacman.graphics.UI;
import me.giverplay.pacman.world.World;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable {
  public static final int WIDTH = 320;
  public static final int HEIGHT = 240;
  public static final int SCALE = 2;

  private static final long serialVersionUID = 1L;

  private static final int MAX_GAME_OVER_FRAMES = 30;

  private static Game game;
  private static int FPS = 0;

  private List<Entity> entities;
  private List<Entity> toRemoveEntities;
  private List<Entity> fruits;

  private Spritesheet sprite;
  private World world;
  private Player player;
  private UI ui;

  private BufferedImage image;
  private Thread thread;
  private JFrame frame;

  private boolean isRunning = false;
  private boolean showGameOver = true;
  private boolean isDied = false;
  private boolean won = false;

  private int gameOverFrames = 0;

  public Game() {
    setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

    setupFrame();
    setupAssets();
  }

  public static Game getGame() {
    return game;
  }

  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }

  public static void handleRestart() {
    game.restart();
  }

  private void setupFrame() {
    frame = new JFrame("Game 02 - PacMano");
    frame.add(this);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    new Listeners(this);
  }

  private void setupAssets() {
    game = this;

    entities = new ArrayList<>();
    fruits = new ArrayList<>();
    toRemoveEntities = new ArrayList<>();

    sprite = new Spritesheet("/Spritesheet.png");
    player = new Player(1, 1, 16, 16);
    world = new World("/World.png");

    ui = new UI();

    entities.add(player);
    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);

    won = false;
    isDied = false;
  }

  public synchronized void start() {
    isRunning = true;
    thread = new Thread(this);
    thread.start();
  }

  public synchronized void stop() {
    isRunning = false;

    try {
      thread.join();
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void restart() {
    setupAssets();
  }

  @Override
  public void run() {
    requestFocus();

    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();

    double ticks = 60.0D;
    double ns = 1000000000 / ticks;
    double delta = 0.0D;

    int fps = 0;

    while(isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;

      if(delta >= 1) {
        tick();
        render();

        delta--;
        fps++;
      }

      if(System.currentTimeMillis() - timer >= 1000) {
        FPS = fps;
        fps = 0;
        timer = System.currentTimeMillis();
      }
    }

    stop();
  }

  public synchronized void tick() {
    if(!isDied && !won) {
      entities.forEach(Entity::tick);
      fruits.forEach(Entity::tick);

      toRemoveEntities.forEach(e -> entities.remove(e));
      toRemoveEntities.clear();

      if(fruits.size() == 0) {
        won = true;
      }
    }
  }

  public synchronized void render() {
    BufferStrategy bs = this.getBufferStrategy();

    if(bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = image.getGraphics();

    g.setColor(new Color(0, 0, 0));
    g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

    world.render(g);

    entities.sort(Enemy.sortDepth);

    for(Entity fruit : fruits) fruit.render(g);
    for(Entity entity : entities) entity.render(g);

    g.dispose();
    g = bs.getDrawGraphics();
    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

    renderSmooth(g);

    if(isDied || won) {
      Graphics2D g2 = (Graphics2D) g;

      g2.setColor(new Color(0, 0, 0, 100));
      g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

      String txt = isDied ? "Game Over" : "Você Venceu!";
      g.setColor(Color.WHITE);
      g.setFont(FontUtils.getFont(32, Font.BOLD));
      g.drawString(txt, (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth(txt)) / 2, HEIGHT * SCALE / 2);

      gameOverFrames++;

      if(gameOverFrames > MAX_GAME_OVER_FRAMES) {
        gameOverFrames = 0;
        showGameOver = !showGameOver;
      }

      if(showGameOver) {
        g.setFont(FontUtils.getFont(24, Font.BOLD));
        g.drawString("> Aperte ENTER para reiniciar <", (WIDTH * SCALE - g.getFontMetrics(g.getFont()).stringWidth("> Aperte ENTER para reiniciar <")) / 2, HEIGHT * SCALE / 2 + 28);
      }
    }

    bs.show();
  }

  public void renderSmooth(Graphics g) {
    ui.render(g);

    g.setColor(new Color(100, 100, 100));
    g.setFont(FontUtils.getFont(18, Font.PLAIN));

    g.setColor(Color.WHITE);
    g.setFont(FontUtils.getFont(11, Font.PLAIN));
    g.drawString("FPS: " + FPS, 2, 12);
  }

  public Player getPlayer() {
    return this.player;
  }

  public Spritesheet getSpritesheet() {
    return this.sprite;
  }

  public World getWorld() {
    return this.world;
  }

  public boolean isDied() {
    return this.isDied;
  }

  public boolean won() {
    return this.won;
  }

  public List<Entity> getFruits() {
    return this.fruits;
  }

  public void handleDeath() {
    this.isDied = true;
  }

  public void removeEntity(Entity entity) {
    toRemoveEntities.add(entity);
  }

  public void addEntity(Entity entity) {
    entities.add(entity);
  }
}
