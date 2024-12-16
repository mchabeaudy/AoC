import static java.lang.Math.min;
import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day16 extends AbstractDay {

  public Day16() {
    super("day16");
  }

  int height;
  int width;
  Point[][] points;
  Point start;
  Point end;

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      height = input.size();
      width = input.getFirst().length();
      points = new Point[height][width];
      range(0, height).forEach(y -> range(0, width).forEach(x -> parsePoint(y, x, input.get(y).charAt(x))));

      var paths = bestPaths();
      // part1
      System.out.println(paths.getFirst().score());
      // part2
      System.out.println(paths.stream().map(p -> p.points).flatMap(Collection::stream).distinct().count());
    }
  }

  private void parsePoint(int y, int x, char c) {
    points[x][y] = new Point(x, y, c);
    if (c == 'E') {
      end = points[x][y];
    } else if (c == 'S') {
      start = points[x][y];
    }
  }

  List<Path> bestPaths() {
    List<Path> endPaths = new ArrayList<>();
    List<Path> paths = List.of(new Path(start));
    Map<Point, Map<Direction, Long>> scores = new HashMap<>();
    while (!paths.isEmpty()) {
      // expand paths
      var expanded = paths.stream().map(this::expand).flatMap(Collection::stream).toList();

      // get completed paths
      var completePaths = expanded.stream().filter(this::isComplete).toList();
      if (!completePaths.isEmpty()) {
        long newBestScore = evalNewBestScore(endPaths, completePaths);
        endPaths.addAll(completePaths.stream().filter(p -> p.score() == newBestScore).toList());
        endPaths.removeIf(p -> p.score() > newBestScore);
      }

      // process new paths
      paths = expanded.stream().filter(not(this::isComplete))
          .filter(p -> isPathBetterThenExistings(p, scores))
          .toList();
    }
    return endPaths;
  }

  private long evalNewBestScore(List<Path> endPaths, List<Path> completePaths) {
    var currentScore = bestScore(endPaths);
    var newScore = completePaths.stream().mapToLong(Path::score).min().getAsLong();
    return currentScore == 0 ? newScore : min(currentScore, newScore);
  }

  boolean isComplete(Path path) {
    return path.points.getLast().equals(end);
  }

  private boolean isPathBetterThenExistings(Path path, Map<Point, Map<Direction, Long>> scores) {
    Point currentPathLast = path.points.getLast();
    long pathScore = path.score();
    Direction pathDirection = path.direction();
    if (scores.containsKey(currentPathLast)) {
      var lastScore = scores.get(currentPathLast);
      if (lastScore.containsKey(pathDirection)) {
        if (lastScore.get(pathDirection) >= pathScore) {
          lastScore.put(pathDirection, pathScore);
          return true;
        }
      } else {
        lastScore.put(pathDirection, pathScore);
        return true;
      }
    } else {
      Map<Direction, Long> scoreMap = new EnumMap<>(Direction.class);
      scores.put(currentPathLast, scoreMap);
      scoreMap.put(pathDirection, pathScore);
      return true;
    }
    return false;
  }


  long bestScore(List<Path> paths) {
    return paths.stream()
        .filter(p -> p.points.getLast().equals(end))
        .mapToLong(Path::score)
        .min().orElse(0L);
  }

  List<Point> around(Point p) {
    List<Point> around = new ArrayList<>();
    if (p.x > 0) {
      around.add(points[p.x - 1][p.y]);
    }
    if (p.x < width - 1) {
      around.add(points[p.x + 1][p.y]);
    }
    if (p.y > 0) {
      around.add(points[p.x][p.y - 1]);
    }
    if (p.y < height - 1) {
      around.add(points[p.x][p.y + 1]);
    }
    return around;
  }

  public static void main(String[] args) throws Exception {
    new Day16().solution();
  }

  record Point(int x, int y, char type) {

    boolean isWall() {
      return type == '#';
    }

  }

  List<Path> expand(Path path) {
    return around(path.points.getLast()).stream()
        .filter(not(Point::isWall))
        .filter(not(path.points::contains))
        .map(newEnd -> {
          List<Point> pathPoints = new ArrayList<>(path.points);
          pathPoints.add(newEnd);
          return new Path(pathPoints, Direction.of(pathPoints));
        }).toList();
  }

  record Path(List<Point> points, Direction direction) {

    Path(Point start) {
      this(new ArrayList<>(List.of(start)), Direction.RIGHT);
    }

    public long score() {
      long score = points.size() - 1L;
      for (int k = 1; k < points.size() - 1; k++) {
        if (k == 1) {
          var p1 = points.getFirst();
          var p2 = points.get(1);
          if (p2.x - p1.x != 1) {
            score += 1000;
          }
          continue;
        }
        var p1 = points.get(k - 1);
        var p2 = points.get(k);
        var p3 = points.get(k + 1);
        int dx1 = p3.x - p2.x;
        int dx2 = p2.x - p1.x;
        int dy1 = p3.y - p2.y;
        int dy2 = p2.y - p1.y;
        if (dx1 != dx2 || dy1 != dy2) {
          score += 1000;
        }
      }
      return score;
    }

  }

  enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction of(List<Point> points) {
      int x0 = points.getLast().x;
      int x1 = points.get(points.size() - 2).x;
      int y0 = points.getLast().y;
      int y1 = points.get(points.size() - 2).y;
      if (x0 > x1) {
        return RIGHT;
      } else if (x0 < x1) {
        return LEFT;
      } else if (y0 > y1) {
        return DOWN;
      } else if (y0 < y1) {
        return UP;
      }
      throw new IllegalArgumentException("invalid direction");
    }
  }
}
