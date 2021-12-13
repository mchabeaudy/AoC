package day05;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day05B {

  private static final String FILE = "day05.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));
    var vents = lines.map(s -> {
          var aCoordinates = s.split(" -> ")[0].split(",");
          var bCoordinates = s.split(" -> ")[1].split(",");
          var pointA = new Point(parseInt(aCoordinates[0]), parseInt(aCoordinates[1]));
          var pointB = new Point(parseInt(bCoordinates[0]), parseInt(bCoordinates[1]));
          return new Vent(pointA, pointB);
        })
        .toList();

    var allPoints = vents.stream()
        .map(Vent::points)
        .flatMap(Collection::stream)
        .toList();

    Map<Point, Integer> occurrences = new HashMap<>();
    allPoints.forEach(p -> {
      if (occurrences.containsKey(p)) {
        occurrences.put(p, occurrences.get(p) + 1);
      } else {
        occurrences.put(p, 1);
      }
    });

    System.out.println(occurrences.values().stream().filter(i -> i > 1).count());
  }


  static class Point {

    int x;
    int y;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  static class Vent {

    Point a;
    Point b;

    public Vent(Point a, Point b) {
      this.a = a;
      this.b = b;
    }

    public Point getA() {
      return a;
    }

    public void setA(Point a) {
      this.a = a;
    }

    public Point getB() {
      return b;
    }

    public void setB(Point b) {
      this.b = b;
    }

    public List<Point> points() {
      List<Point> points;
      int minX = Math.min(a.x, b.x);
      int minY = Math.min(a.y, b.y);
      int maxX = Math.max(a.x, b.x);
      int maxY = Math.max(a.y, b.y);
      if (minX == maxX) {
        points = range(minY, maxY + 1)
            .mapToObj(y -> new Point(minX, y))
            .toList();
      } else if (minY == maxY) {
        points = range(minX, maxX + 1)
            .mapToObj(x -> new Point(x, minY))
            .toList();
      } else {
        var x = new AtomicInteger(0);
        var y = new AtomicInteger(0);
        int dx = a.getX() < b.getX() ? 1 : -1;
        int dy = a.getY() < b.getY() ? 1 : -1;
        points = IntStream.range(minX, maxX + 1)
            .mapToObj(k -> new Point(a.getX() + x.getAndAdd(dx), a.getY() + y.getAndAdd(dy)))
            .toList();
      }
      return points;
    }
  }
}
