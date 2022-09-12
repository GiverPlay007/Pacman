package me.giverplay.pacman.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class Sounds {

  public static Sound collect = load("/collect.wav");
  public static Sound hit = load("/hit.wav");

  private static Sound load(String name) {
    try(
      DataInputStream dis = new DataInputStream(Objects.requireNonNull(Sounds.class.getResourceAsStream(name)));
      ByteArrayOutputStream baos = new ByteArrayOutputStream()
    ) {
      byte[] buffer = new byte[1024];

      int read;

      while((read = dis.read(buffer)) >= 0) {
        baos.write(buffer, 0, read);
      }

      byte[] data = baos.toByteArray();

      try {
        return new Sound(data);
      } catch(Exception e) {
        System.out.println("Falha ao inicializar áudio: " + name);
      }
    } catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static class Sound {
    public Clip clip;

    public Sound(byte[] buffer) throws Exception {
      Objects.requireNonNull(buffer);

      clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
    }

    public void play() {
      clip.stop();
      clip.setFramePosition(0);
      clip.start();
    }
  }
}
