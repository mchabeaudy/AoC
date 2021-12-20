package day19;

import static java.lang.Integer.parseInt;

public record Point(int x, int y, int z) {

  public Point(String[] s) {
    this(parseInt(s[0]), parseInt(s[1]), parseInt(s[2]));
  }

  long dist2(Point p) {
    long dx = Math.abs(x - p.x);
    long dy = Math.abs(y - p.y);
    long dz = Math.abs(z - p.z);
    return dx * dx + dy * dy + dz * dz;
  }

  public Point translate(Point p) {
    return new Point(x + p.x, y + p.y, z + p.z);
  }

  long manhattanDist(Point p) {
    return Math.abs(x - p.x) + Math.abs(y - p.y) + Math.abs(z - p.z);
  }

}
