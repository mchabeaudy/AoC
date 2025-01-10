import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day20 extends AbstractDay {

  public Day20() {
    super("day20");
  }

  Point[][] points;
  int width;
  int height;
  Point start;
  Point end;
  List<Point> walls = new ArrayList<>();

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      width = input.getFirst().length();
      height = input.size();
      points = new Point[height][width];
      range(0, height).forEach(y -> range(0, width).forEach(x -> {
        char c = input.get(y).charAt(x);
        Point p = new Point(x, y, c == '#');
        points[y][x] = p;
        if (p.isWall()) {
          walls.add(p);
        }
        if (c == 'S') {
          start = p;
          start.setPathPosition(0);
        } else if (c == 'E') {
          end = p;
        }
      }));
      // initiate
      Point p = start;
      while (p != end) {
        p = next(p);
      }
      Map<Integer, Integer> map = new HashMap<>();
      for (Point wall : walls) {
        List<Point> around = getAround(wall).stream().filter(not(Point::isWall)).toList();
        if (around.size() > 1) {
          Point min = around.stream().min(Comparator.comparing(Point::getPathPosition)).orElseThrow();
          Point max = around.stream().max(Comparator.comparing(Point::getPathPosition)).orElseThrow();
          int cheatValue = max.getPathPosition() - min.getPathPosition() - 2;
          if (cheatValue > 0) {
            wall.setCheatValue(cheatValue);
            if (map.containsKey(wall.getCheatValue())) {
              map.put(wall.getCheatValue(), map.get(wall.getCheatValue()) + 1);
            } else {
              map.put(wall.getCheatValue(), 1);
            }
          }
        }
      }
      System.out.println(map.keySet().stream().filter(k -> k >= 100).mapToInt(map::get).sum());
    }
  }

  public Point next(Point point) {
    List<Point> around = getAround(point);
    var nextPoint = around.stream().filter(p -> p != start && !p.isWall() && p.getPathPosition() == 0).findFirst()
        .orElseThrow(() -> new IllegalStateException("cannot determine next path position"));
    nextPoint.setPathPosition(point.getPathPosition() + 1);
    return nextPoint;
  }

  private List<Point> getAround(Point point) {
    List<Point> around = new ArrayList<>();
    if (point.getX() > 0) {
      around.add(points[point.getY()][point.getX() - 1]);
    }
    if (point.getX() < width - 1) {
      around.add(points[point.getY()][point.getX() + 1]);
    }
    if (point.getY() > 0) {
      around.add(points[point.getY() - 1][point.getX()]);
    }
    if (point.getY() < height - 1) {
      around.add(points[point.getY() + 1][point.getX()]);
    }
    return around;
  }

  public static void main(String[] args) throws Exception {
    new Day20().solution();
  }

  public class Point {

    int x;
    int y;
    boolean wall;
    int cheatValue;
    int pathPosition;

    public Point(int x, int y, boolean wall) {
      this.x = x;
      this.y = y;
      this.wall = wall;
    }

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

    public boolean isWall() {
      return wall;
    }

    public void setWall(boolean wall) {
      this.wall = wall;
    }

    public int getCheatValue() {
      return cheatValue;
    }

    public void setCheatValue(int cheatValue) {
      this.cheatValue = cheatValue;
    }

    public int getPathPosition() {
      return pathPosition;
    }

    public void setPathPosition(int pathPosition) {
      this.pathPosition = pathPosition;
    }

    @Override
    public String toString() {
      return "Point{" +
          "x=" + x +
          ", y=" + y +
          ", wall=" + wall +
          ", cheatValue=" + cheatValue +
          ", pathPosition=" + pathPosition +
          '}';
    }
  }
}
