package dec09;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class AoC_09_A {

  private static final String FILE = "dec09.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1))).toList();

    var points = range(0, lines.size())
        .mapToObj(y -> range(0, lines.get(0).length())
            .mapToObj(x -> new Point(x, y, charToInt(lines.get(y).charAt(x))))
            .toList())
        .flatMap(Collection::stream)
        .toList();

    var lowPoints = points.stream()
        .filter(p -> p.isLowPoint(points))
        .toList();

    var count = lowPoints.stream()
        .mapToInt(pts -> pts.value + 1)
        .sum();
    System.out.println(count);
  }

  record Point(int x, int y, int value) {

    List<Point> closePoints(List<Point> points) {
      return points.stream()
          .filter(p -> dist(p) == 1)
          .toList();
    }

    int dist(Point p) {
      return Math.abs(p.x - x) + Math.abs(p.y - y);
    }

    boolean isLowPoint(List<Point> points) {
      return closePoints(points).stream()
          .allMatch(p -> p.value > value);
    }

  }


  static int charToInt(char c) {
    return Integer.parseInt(String.valueOf(c));
  }

}
