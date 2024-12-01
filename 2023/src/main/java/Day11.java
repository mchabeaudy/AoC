import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Day11 extends AbstractDay {
  Point[][] pointsArray;

  List<Point> points;

  class Point {
    long x;

    long y;

    char c;

    public Point(long x, long y, char c) {
      this.x = x;
      this.y = y;
      this.c = c;
    }

    @Override
    public String toString() {
      return "Point{" + "x=" + x + ", y=" + y + '}';
    }
  }

  class Galaxy extends Point {
    public Galaxy(Point p) {
      super(p.x, p.y, '#');
    }

    long dist(Galaxy g, List<Integer> xVoid, List<Integer> yVoid, long voidDist) {
      long dist = Math.abs(g.x - x) + Math.abs(g.y - y);
      long minY = Math.min(y, g.y);
      long minX = Math.min(x, g.x);
      long maxY = Math.max(y, g.y);
      long maxX = Math.max(x, g.x);
      long nby = yVoid.stream().filter(v -> v > minY && v < maxY).count();
      long nbx = xVoid.stream().filter(v -> v > minX && v < maxX).count();
      dist += (nby + nbx) * voidDist;
      return dist;
    }
  }

  public Day11() {
    super("day11");
  }

  public static void main(String[] args) throws Exception {
    new Day11().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      int width = inputs.getFirst().length();
      int height = inputs.size();

      pointsArray = new Point[height][width];
      points = range(0, height).mapToObj(y -> range(0, width).mapToObj(x -> {
        var p = new Point(x, y, inputs.get(y).charAt(x));
        pointsArray[y][x] = p;
        return p;
      }).toList()).flatMap(Collection::stream).toList();
      var xVoid = range(0, width).filter(x -> range(0, height).allMatch(y -> pointsArray[y][x].c == '.')).boxed()
        .toList();
      var yVoid = range(0, height).filter(y -> range(0, width).allMatch(x -> pointsArray[y][x].c == '.')).boxed()
        .toList();

      var galaxies = points.stream().filter(p -> p.c == '#').map(Galaxy::new).toList();
      long dist = 0;
      var gList = new ArrayList<>(galaxies);
      while (gList.size() > 1) {
        var first = gList.getFirst();
        gList.remove(first);
        dist += gList.stream().mapToLong(g -> first.dist(g, xVoid, yVoid, 999999)).sum();
      }
      System.out.println(dist);
    }
  }

}
