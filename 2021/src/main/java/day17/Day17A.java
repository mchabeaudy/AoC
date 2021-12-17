package day17;

public class Day17A {

  private static int Y_MIN = -118;

  public static void main(String[] args) {

    int xVelocityInitial = 22;
    int yVelocityInitial = 117;

    Probe p = new Probe();
    p.position = new Point(0, 0);
    p.xVelocity = xVelocityInitial;
    p.yVelocity = yVelocityInitial;

    while (p.position.y >= Y_MIN) {
      System.out.println("probe position: " + p.position);
      p.move();
    }
    System.out.println("probe position: " + p.position);
  }

  record Point(int x, int y) {

  }

  static class Probe {

    Point position;
    int xVelocity;
    int yVelocity;

    void move() {
      position = new Point(position.x + xVelocity, position.y + yVelocity);
      if (xVelocity > 0) {
        xVelocity--;
      } else if (xVelocity < 0) {
        xVelocity++;
      }
      yVelocity--;
    }

  }
}
