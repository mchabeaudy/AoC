package day15;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day15B {

  static Map<Coordinate, Point> pointsMap = new HashMap<>();

  public static void main(String[] args) {
    try (var linesStream = Files.lines(
        Paths.get(getSystemResource("day15.txt").getPath()))) {
      var lines0 = linesStream.toList();

      var newLines0 = lines0.stream()
          .map(line -> Arrays.stream(line.split(""))
              .map(s -> new Digit(parseInt(s)))
              .toList())
          .map(digits -> range(0, 5)
              .mapToObj(i -> digits.stream().map(d -> d.next(i).stringValue())
                  .collect(Collectors.joining()))
              .collect(Collectors.joining()))
          .toList();

      var lines = range(0, 5).mapToObj(i -> newLines0.stream()
              .map(line -> Arrays.stream(line.split(""))
                  .map(s -> new Digit(parseInt(s)).next(i).stringValue())
                  .collect(Collectors.joining()))
              .toList())
          .flatMap(Collection::stream)
          .toList();

      int lineLength = lines.get(0).length();
      int linesHeight = lines.size();
      range(0, linesHeight).forEach(y -> range(0, lineLength)
          .forEach(x -> pointsMap.put(new Coordinate(x, y),
              new Point(x, y, charToInt(lines.get(y).charAt(x))))
          ));
      var lastPoint = getPoint(lineLength - 1, linesHeight - 1);

      Map<Coordinate,Path> paths = new HashMap<>();
      var p0 = new Path();
      p0.addPoint(getPoint(0, 0));
      paths.put(p0.getLast().coord, p0);

      var process = true;
      while (process) {
        Map<Coordinate, Path> m = new HashMap<>(paths);
        paths.values().stream().filter(path -> !path.getLast().equals(lastPoint))
            .forEach(path -> {
              var nearPoints = nearPoints(path.getLast());
              nearPoints.removeIf(path::contains);
              List<Path> nPaths = nearPoints.stream()
                  .map(p -> new Path(path, p))
                  .toList();
              nPaths.forEach(nPath -> {
                if (m.containsKey(nPath.getLast().coord)) {
                  var ePath = m.get(nPath.getLast().coord);
                  if (ePath.risk > nPath.risk) {
                    m.put(nPath.getLast().coord, nPath);
                  }
                } else {
                  m.put(nPath.getLast().coord, nPath);
                }
              });
            });
        var newPaths = m.values();
        if (newPaths.stream().mapToInt(Path::getRisk).sum() ==
            paths.values().stream().mapToInt(Path::getRisk).sum()) {
          process = false;
        }
        paths = m;
        if (paths.size() % 100 == 0) {
          System.out.println("coucou");
        }
      }

      var bPath = paths.get(lastPoint.coord);

      System.out.println("risk: " + bPath.risk);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static Point getPoint(int x, int y) {
//    return points.stream().filter(p -> p.x == x && p.y == y).findAny().orElse(null);
    return pointsMap.get(new Coordinate(x, y));
  }


  static List<Point> nearPoints(Point p) {
    List<Point> near = new ArrayList<>();
    Optional.ofNullable(pointsMap.getOrDefault(new Coordinate(p.coord.x - 1, p.coord.y), null))
        .ifPresent(near::add);
    Optional.ofNullable(pointsMap.getOrDefault(new Coordinate(p.coord.x, p.coord.y - 1), null))
        .ifPresent(near::add);
    Optional.ofNullable(pointsMap.getOrDefault(new Coordinate(p.coord.x, p.coord.y + 1), null))
        .ifPresent(near::add);
    Optional.ofNullable(pointsMap.getOrDefault(new Coordinate(p.coord.x + 1, p.coord.y), null))
        .ifPresent(near::add);
    return near;
  }

  record Coordinate(int x, int y) {
    public int dist(Coordinate p) {
      return Math.abs(p.x - x) + Math.abs(p.y - y);
    }
  }

  static class Point {

    Coordinate coord;
    int risk, dist;

    public Point(int x, int y, int risk) {
      coord = new Coordinate(x, y);
      this.risk = risk;
      dist = 0;
    }

    public int dist(Point p) {
      return coord.dist(p.coord);
    }

    public int getRisk() {
      return risk;
    }

    public void setDist(int dist) {
      this.dist = dist;
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
      return coord.x == point.coord.x && coord.y == point.coord.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(coord.x, coord.y);
    }

    @Override
    public String toString() {
      return "Point{" +
          "x=" + coord.x +
          ", y=" + coord.y +
          ", risk=" + risk +
          ", dist=" + dist +
          '}';
    }
  }

  record Digit(int d) {

    Digit next() {
      if (d == 9) {

        return new Digit(1);
      }
      return new Digit(d + 1);
    }

    Digit next(int k) {
      Digit d = this;
      for (int i = 0; i < k; i++) {
        d = d.next();
      }
      return d;
    }

    String stringValue() {
      return String.valueOf(d);
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
    return parseInt(String.valueOf(c));
  }

}