import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8 extends AbstractDay {

  char[][] map;
  int width;
  int height;
  Map<Character, List<Point>> locations = new HashMap<>();

  public Day8() {
    super("day8");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      width = inputs.getFirst().length();
      height = inputs.size();
      map = new char[height][width];

      range(0, height).forEach(y -> range(0, width).forEach(x -> {
        map[x][y] = inputs.get(y).charAt(x);
        if (map[x][y] != '.') {
          locations.computeIfAbsent(map[x][y], k -> new ArrayList<>()).add(new Point(x, y));
        }
      }));

      // part1
      Set<Point> antinodes = new HashSet<>();
      locations.values().forEach(points -> antinodes.addAll(points.stream()
          .map(p -> antinodes(p, points))
          .flatMap(Collection::stream)
          .filter(this::isInMap)
          .toList())
      );
      System.out.println(antinodes.size());

      // part2
      antinodes.clear();
      locations.values().forEach(points -> antinodes.addAll(points.stream()
          .map(p -> antinodes2(p, points))
          .flatMap(Collection::stream)
          .filter(this::isInMap)
          .toList())
      );
      System.out.println(antinodes.size());
    }
  }

  private boolean isInMap(Point point) {
    return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
  }

  List<Point> antinodes(Point p0, List<Point> points) {
    return points.stream().filter(not(p0::equals)).map(p1 -> {
      int dx = p1.x - p0.x;
      int dy = p1.y - p0.y;
      return new Point(p1.x + dx, p1.y + dy);
    }).toList();
  }

  List<Point> antinodes2(Point p0, List<Point> points) {
    return points.stream().filter(not(p0::equals)).map(p1 -> {
          int dx = p1.x - p0.x;
          int dy = p1.y - p0.y;
          var list = new ArrayList<Point>();
          list.add(p1);
          int k = 1;
          while (isInMap(new Point(p1.x + k * dx, p1.y + k * dy))) {
            list.add(new Point(p1.x + k * dx, p1.y + k * dy));
            k++;
          }
          k = 1;
          while (isInMap(new Point(p1.x - k * dx, p1.y - k * dy))) {
            list.add(new Point(p1.x - k * dx, p1.y - k * dy));
            k++;
          }
          return list;
        })
        .flatMap(Collection::stream)
        .toList();
  }

  public static void main(String[] args) throws Exception {
    new Day8().solution();
  }

  record Point(int x, int y) {

  }
}
