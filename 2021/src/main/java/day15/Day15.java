package day15;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day15 {

  static List<Point> points;

  public static void main(String[] args) {
    try (var linesStream = Files.lines(
        Paths.get(getSystemResource("day15.txt").getPath().substring(1)))) {
      var lines = linesStream.toList();

      int lineLength = lines.get(0).length();
      int linesHeight = lines.size();

      points = range(0, linesHeight).mapToObj(y -> range(0, lineLength)
              .mapToObj(x -> new Point(x, y, charToInt(lines.get(y).charAt(x)))).toList())
          .flatMap(Collection::stream)
          .toList();

      var lastPoint = points.stream().filter(p -> p.x == lineLength - 1 && p.y == linesHeight - 1)
          .findAny().orElseThrow();

      Collection<Path> paths = new ArrayList<>();
      var p0 = new Path();
      p0.addPoint(getPoint(0, 0));
      paths.add(p0);

      var process = true;
      while (process) {
        Map<Point, Path> m = new HashMap<>();
        paths.forEach(p -> m.put(p.getLast(), p));
        paths.stream().filter(path -> !path.getLast().equals(lastPoint))
            .forEach(path -> {
              var nearPoints = nearPoints(path.getLast());
              nearPoints.removeIf(path::contains);
              List<Path> nPaths = nearPoints.stream()
                  .map(p -> new Path(path, p))
                  .toList();
              nPaths.forEach(nPath -> {
                if (m.containsKey(nPath.getLast())) {
                  var ePath = m.get(nPath.getLast());
                  if (ePath.risk > nPath.risk) {
                    m.put(nPath.getLast(), nPath);
                  }
                } else {
                  m.put(nPath.getLast(), nPath);
                }
              });
            });
        var newPaths = m.values();
        if (newPaths.stream().mapToInt(Path::getRisk).sum() ==
            paths.stream().mapToInt(Path::getRisk).sum()) {
          process = false;
        }
        paths = newPaths;
      }

      var bPath = paths.stream()
          .filter(p -> p.getLast().equals(lastPoint))
          .findAny()
          .orElse(null);

      System.out.println("risk: " + bPath.risk);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Point getPoint(int x, int y) {
    return points.stream().filter(p -> p.x == x && p.y == y).findAny().orElse(null);
  }

  static List<Point> nearPoints(Point p) {
    return points.stream().filter(o -> p.dist(o) == 1).collect(Collectors.toList());
  }

  record Point(int x, int y, int risk) {

    public int dist(Point p) {
      return Math.abs(p.x - x) + Math.abs(p.y - y);
    }

    @Override
    public String toString() {
      return "[" +
          "x=" + x +
          ", y=" + y +
          ']';
    }
  }

  static class Path {

    List<Point> path = new ArrayList<>();
    int risk = 0;

    Path() {

    }

    Path(Path p, Point point) {
      path = new ArrayList<>(p.path);
      risk = p.risk;
      addPoint(point);
    }

    Point getLast() {
      return path.get(path.size() - 1);
    }

    boolean contains(Point p) {
      return path.contains(p);
    }

    void addPoint(Point p) {
      path.add(p);
      risk += p.risk;
    }

    public int getRisk() {
      return risk;
    }

    @Override
    public String toString() {
      return "Path{" +
          String.join("-", path.stream().map(Objects::toString).toList()) +
          '}';
    }
  }

  static int charToInt(char c) {
    return Integer.parseInt(String.valueOf(c));
  }

}