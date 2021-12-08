package dec06;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class AoC_06_AB {

  private static final String FILE = "dec06.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));
    var fishes = Arrays.stream(lines
            .toList().get(0)
            .split(",")).map(Integer::valueOf)
        .map(Fish::new)
        .toList();

    var game = new Game();
    game.setNbFish0(fishes.stream().filter(f -> f.getTimer() == 0).count());
    game.setNbFish1(fishes.stream().filter(f -> f.getTimer() == 1).count());
    game.setNbFish2(fishes.stream().filter(f -> f.getTimer() == 2).count());
    game.setNbFish3(fishes.stream().filter(f -> f.getTimer() == 3).count());
    game.setNbFish4(fishes.stream().filter(f -> f.getTimer() == 4).count());
    game.setNbFish5(fishes.stream().filter(f -> f.getTimer() == 5).count());
    game.setNbFish6(fishes.stream().filter(f -> f.getTimer() == 6).count());
    game.setNbFish7(fishes.stream().filter(f -> f.getTimer() == 7).count());
    game.setNbFish8(fishes.stream().filter(f -> f.getTimer() == 8).count());

    System.out.println(game.count(256));
  }

  static class Game {

    long nbFish0, nbFish1, nbFish2, nbFish3, nbFish4, nbFish5, nbFish6, nbFish7, nbFish8;

    public void increase() {
      long newO = nbFish1;
      long new1 = nbFish2;
      long new2 = nbFish3;
      long new3 = nbFish4;
      long new4 = nbFish5;
      long new5 = nbFish6;
      long new6 = nbFish7 + nbFish0;
      long new7 = nbFish8;
      long new8 = nbFish0;

      nbFish0 = newO;
      nbFish1 = new1;
      nbFish2 = new2;
      nbFish3 = new3;
      nbFish4 = new4;
      nbFish5 = new5;
      nbFish6 = new6;
      nbFish7 = new7;
      nbFish8 = new8;
    }

    public long count(int k) {
      range(0, k).forEach(i -> increase());
      return nbFish0 + nbFish1 + nbFish2 + nbFish3 + nbFish4 + nbFish5 + nbFish6 + nbFish7
          + nbFish8;
    }

    public void setNbFish0(long nbFish0) {
      this.nbFish0 = nbFish0;
    }

    public void setNbFish1(long nbFish1) {
      this.nbFish1 = nbFish1;
    }

    public void setNbFish2(long nbFish2) {
      this.nbFish2 = nbFish2;
    }

    public void setNbFish3(long nbFish3) {
      this.nbFish3 = nbFish3;
    }

    public void setNbFish4(long nbFish4) {
      this.nbFish4 = nbFish4;
    }

    public void setNbFish5(long nbFish5) {
      this.nbFish5 = nbFish5;
    }

    public void setNbFish6(long nbFish6) {
      this.nbFish6 = nbFish6;
    }

    public void setNbFish7(long nbFish7) {
      this.nbFish7 = nbFish7;
    }

    public void setNbFish8(long nbFish8) {
      this.nbFish8 = nbFish8;
    }
  }

  static class Fish {

    int timer;

    public Fish() {
      timer = 8;
    }

    public Fish(int timer) {
      this.timer = timer;
    }

    public int getTimer() {
      return timer;
    }

    @Override
    public String toString() {
      return "Fish{" +
          "timer=" + timer +
          '}';
    }
  }

}
