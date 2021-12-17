package day17;

public class Day17B {

  private static int X_MIN = 235;
  private static int X_MAX = 259;
  private static int Y_MIN = -118;
  private static int Y_MAX = -62;

  public static void main(String[] args) {

    int xVelocityInitial = 22;

    int count = 0;
    for (int k = -400; k < 400; k++) {
      for (int l = -400; l < 400; l++) {
        Probe p = new Probe();
        p.position = new Point(0, 0);
        p.xVelocity = xVelocityInitial + l;
        p.yVelocity = k;
        while (p.position.y >= Y_MIN) {
          p.move();
        }
        if (p.isGood()) {
          count++;
        }
      }
    }
    System.out.println(count);
  }

  record Point(int x, int y) {

  }

  static class Probe {

    Point position;
    int xVelocity;
    int yVelocity;
    private boolean good = false;

    void move() {
      position = new Point(position.x + xVelocity, position.y + yVelocity);
      if (position.x >= X_MIN && position.x <= X_MAX && position.y >= Y_MIN
          && position.y <= Y_MAX) {
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
