package day15;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day15B {

  static Point[][] points;
  static int xMax;
  static int yMax;

  public static void main(String[] args) {
      try (var linesStream = Files.lines(
          Paths.get(getSystemResource("day15.txt").getPath().substring(1)))) {
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
        xMax = lineLength - 1;
        int linesHeight = lines.size();
        yMax = linesHeight - 1;
        points = new Point[lineLength][linesHeight];
        range(0, linesHeight).forEach(y -> range(0, lineLength)
            .forEach(x -> points[x][y] = new Point(x, y, charToInt(lines.get(y).charAt(x)))));
        var lastPoint = points[lineLength - 1][linesHeight - 1];

        Path[][] pathsMap = new Path[lineLength][linesHeight];
        Integer[][] riskMap = new Integer[lineLength][linesHeight];

        var p0 = new Path();
        p0.addPoint(points[0][0]);
        pathsMap[0][0] = p0;
        riskMap[0][0] = 0;

        var process = true;
        while (process) {
          Path[][] nPathsMap = new Path[lineLength][linesHeight];
          range(0, lineLength).forEach(
              x -> range(0, linesHeight).forEach(y -> nPathsMap[x][y] = pathsMap[x][y]));
          Integer[][] nRiskMap = new Integer[lineLength][linesHeight];
          range(0, lineLength).forEach(
              x -> range(0, linesHeight).forEach(y -> nRiskMap[x][y] = riskMap[x][y]));
          Arrays.stream(pathsMap).map(Arrays::stream)
              .flatMap(Function.identity())
              .filter(path -> Objects.nonNull(path) && !path.last.equals(lastPoint))
              .forEach(path -> {
                var nearPoints = nearPoints(path.last);
                nearPoints.removeIf(path::contains);
                nearPoints.forEach(p -> {
                  if (nPathsMap[p.coord.x][p.coord.y] == null) {
                    nPathsMap[p.coord.x][p.coord.y] = new Path(path, p);
                    nRiskMap[p.coord.x][p.coord.y] = nPathsMap[p.coord.x][p.coord.y].risk;
                  } else {
                    if (nRiskMap[p.coord.x][p.coord.y] > path.risk + p.risk) {
                      nPathsMap[p.coord.x][p.coord.y] = new Path(path, p);
                      nRiskMap[p.coord.x][p.coord.y] = nPathsMap[p.coord.x][p.coord.y].risk;
                    }
                  }
                });
              });
          var newSize = Arrays.stream(nRiskMap).map(Arrays::stream)
              .flatMap(Function.identity())
              .filter(Objects::nonNull)
              .mapToInt(Integer::intValue)
              .sum();
          var oldSize = Arrays.stream(riskMap).map(Arrays::stream)
              .flatMap(Function.identity())
              .filter(Objects::nonNull)
              .mapToInt(Integer::intValue)
              .sum();
          if (oldSize == newSize) {
            process = false;
          }

          range(0, lineLength).forEach(
              x -> range(0, linesHeight).forEach(y -> pathsMap[x][y] = nPathsMap[x][y]));
          range(0, lineLength).forEach(
              x -> range(0, linesHeight).forEach(y -> riskMap[x][y] = nRiskMap[x][y]));
        }

        System.out.println("risk: " + riskMap[xMax][yMax]);
      } catch (Exception e) {
        e.printStackTrace();
      }

  }


  static List<Point> nearPoints(Point p) {
    List<Point> near = new ArrayList<>();
    if (p.coord.x == 0) {
      near.add(points[p.coord.x + 1][p.coord.y]);
    } else if (p.coord.x == xMax) {
      near.add(points[p.coord.x - 1][p.coord.y]);
    } else {
      near.add(points[p.coord.x - 1][p.coord.y]);
      near.add(points[p.coord.x + 1][p.coord.y]);
    }
    if (p.coord.y == 0) {
      near.add(points[p.coord.x][p.coord.y + 1]);
    } else if (p.coord.y == yMax) {
      near.add(points[p.coord.x][p.coord.y - 1]);
    } else {
      near.add(points[p.coord.x][p.coord.y - 1]);
      near.add(points[p.coord.x][p.coord.y + 1]);
    }
    return near;
  }

  record Coordinate(int x, int y) {

  }

  static class Point {

    Coordinate coord;
    int risk, dist;

    public Point(int x, int y, int risk) {
      coord = new Coordinate(x, y);
      this.risk = risk;
      dist = 0;
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
    List<Coordinate> coordinates = new ArrayList<>();
    Point last;
    int risk = 0;

    Path() {

    }

    Path(Path p, Point point) {
      path = new ArrayList<>(p.path);
      coordinates = new ArrayList<>(p.coordinates);
      risk = p.risk;
      addPoint(point);
    }

    boolean contains(Point p) {
      return coordinates.contains(p.coord);
    }

    void addPoint(Point p) {
      if(path.size()>12){
        path = range(6,path.size()).mapToObj(path::get).collect(Collectors.toList());
        coordinates = range(6,coordinates.size()).mapToObj(coordinates::get).collect(Collectors.toList());
      }
      path.add(p);
      coordinates.add(p.coord);
      risk += p.risk;
      last = p;
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