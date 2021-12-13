package day11;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class Day11B {

  public static void main(String[] args) {
    try (var lines = Files.lines(
        Paths.get(getSystemResource("day11.txt").getPath().substring(1)))) {
      var list = lines.toList();
      var octopuses = range(0, list.size())
          .mapToObj(y -> range(0, list.get(0).length())
              .mapToObj(x -> new Octopus(x, y, charToInt(list.get(y).charAt(x)), false))
              .toList())
          .flatMap(Collection::stream)
          .toList();

      int k = 0;
      while (!octopuses.stream().allMatch(o -> o.energy == 0)) {
        k++;
        octopuses.forEach(o -> o.setFlash(false));
        octopuses.forEach(Octopus::increaseEnergy);
        var newFlashes = new AtomicInteger(0);
        var process = true;
        while (process) {
          newFlashes.set(0);
          octopuses.stream()
              .filter(Octopus::isHighEnergy)
              .forEach(o -> {
                newFlashes.getAndAdd(1);
                o.setFlash(true);
                o.setEnergy(0);
                octopuses.stream()
                    .filter(other -> o.isNear(other) && !other.flash)
                    .forEach(Octopus::increaseEnergy);
              });
          if (newFlashes.get() == 0) {
            process = false;
          }
        }
      }

      System.out.println("result: " + k);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  static class Octopus {

    private int x;
    private int y;
    private int energy;
    private boolean flash;

    public Octopus(int x, int y, int energy, boolean flash) {
      this.x = x;
      this.y = y;
      this.energy = energy;
      this.flash = flash;
    }

    public int getX() {
      return x;
    }

    public void increaseEnergy() {
      energy += 1;
    }

    public boolean isHighEnergy() {
      return energy > 9;
    }


    public void setEnergy(int energy) {
      this.energy = energy;
    }


    public void setFlash(boolean flash) {
      this.flash = flash;
    }

    public boolean isNear(Octopus o) {
      var dx = Math.abs(o.x - x);
      var dy = Math.abs(o.y - y);
      return (dx == 1 && dy < 2) || (dy == 1 && dx < 2);
    }

    @Override
    public String toString() {
      return "Octopus{" +
          "x=" + x +
          ", y=" + y +
          ", energy=" + energy +
          ", flash=" + flash +
          '}';
    }
  }

  static int charToInt(char c) {
    return Integer.parseInt(String.valueOf(c));
  }

}
