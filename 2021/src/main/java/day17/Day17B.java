package day17;

public class Day17B {

  private static final int X_MIN = 235;
  private static final int X_MAX = 259;
  private static final int Y_MIN = -118;
  private static final int Y_MAX = -62;

  public static void main(String[] args) {

    int xVelocityInitial = 22;

    int count = 0;
    for (int k = -400; k < 400; k++) {
      for (int l = -400; l < 400; l++) {
        var probe = new Probe(0, 0, xVelocityInitial + l, k);
        while (probe.y >= Y_MIN) {
          probe.move();
        }
        if (probe.isGood()) {
          count++;
        }
      }
    }
    System.out.println(count);
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

    private boolean good = false;

    void move() {
      x += xVelocity;
      y += yVelocity;
      if (x >= X_MIN && x <= X_MAX && y >= Y_MIN && y <= Y_MAX) {
        good = true;
      }
      if (xVelocity > 0) {
        xVelocity--;
      } else if (xVelocity < 0) {
        xVelocity++;
      }
      yVelocity--;
    }

    boolean isGood() {
      return good;
    }
  }
}
