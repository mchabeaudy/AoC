package day17;

public class Day17A {

  private static final int Y_MIN = -118;

  public static void main(String[] args) {

    int xVelocityInitial = 22;
    int yVelocityInitial = 117;

    var probe = new Probe(0, 0, xVelocityInitial, yVelocityInitial);

    while (probe.y >= Y_MIN) {
      System.out.println("probe position: x=" + probe.x + " y=" + probe.y);
      probe.move();
    }
    System.out.println("probe position: x=" + probe.x + " y=" + probe.y);
  }

  static class Probe {

    int x;
    int y;
    int xVelocity;
    int yVelocity;

    public Probe(int x, int y, int xVelocity, int yVelocity) {
      this.x = x;
      this.y = y;
      this.xVelocity = xVelocity;
      this.yVelocity = yVelocity;
    }

    void move() {
      x += xVelocity;
      y += yVelocity;
      if (xVelocity > 0) {
        xVelocity--;
      } else if (xVelocity < 0) {
        xVelocity++;
      }
      yVelocity--;
    }
  }
}
