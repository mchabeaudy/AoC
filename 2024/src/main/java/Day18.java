import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 extends AbstractDay {

  public Day18() {
    super("day18");
  }

  List<Point> walls;
  Point[][] map;
  int width = 71;
  int height = 71;
  Set<Point> visited;
  Point end = new Point(width - 1, height - 1);

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      walls = lines.map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toList()).map(Point::new).toList();

      int k = 2200;
      var shortest = shortest(k);
      System.out.println(shortest);

      while (shortest(k) != 0) {
        k++;
        if (k % 15 == 0) {
          System.out.println(k);
        }
      }
      System.out.println(k);
      System.out.println(walls.get(k-1));
    }
  }

  void initMap(int k) {
    map = new Point[height][width];
    range(0, height).forEach(y -> range(0, width).forEach(
        x -> map[x][y] = range(0, k).filter(i -> walls.get(i).eq(x, y)).mapToObj(walls::get).findAny()
            .orElseGet(() -> new Point(x, y))));
  }

  int shortest(int k) {
    initMap(k);
    List<List<Point>> paths = new ArrayList<>();
    Point initial = new Point(0, 0);
    visited = new HashSet<>();
    visited.add(initial);
    paths.add(List.of(initial));
    while (!(paths.isEmpty() || visited.contains(end))) {
      paths = paths.stream().map(this::expand).flatMap(Collection::stream).toList();
    }
    var best = paths.stream().filter(l -> l.contains(end)).findFirst();
    return best.map(l -> l.size() - 1).orElse(0);
  }

  List<List<Point>> expand(List<Point> path) {
    List<Point> around = new ArrayList<>();
    var last = path.getLast();
    if (last.x > 0 && !map[last.x - 1][last.y].wall && !visited.contains(map[last.x - 1][last.y])) {
      around.add(map[last.x - 1][last.y]);
    }
    if (last.x < width - 1 && !map[last.x + 1][last.y].wall && !visited.contains(map[last.x + 1][last.y])) {
      around.add(map[last.x + 1][last.y]);
    }
    if (last.y > 0 && !map[last.x][last.y - 1].wall && !visited.contains(map[last.x][last.y - 1])) {
      around.add(map[last.x][last.y - 1]);
    }
    if (last.y < height - 1 && !map[last.x][last.y + 1].wall && !visited.contains(map[last.x][last.y + 1])) {
      around.add(map[last.x][last.y + 1]);
    }
    return around.stream().map(p -> {
      List<Point> newPath = new ArrayList<>(path);
      newPath.add(p);
      visited.add(p);
      return newPath;
    }).toList();
  }

  public static void main(String[] args) throws Exception {
    new Day18().solution();
  }

  record Point(int x, int y, boolean wall) {

    public Point(int x, int y) {
      this(x, y, false);
    }

    Point(List<Integer> l) {
      this(l.get(0), l.get(1), true);
    }

    public boolean eq(int x, int y) {
      return this.x == x && this.y == y;
    }
  }
}
