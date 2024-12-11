import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Day10 extends AbstractDay {

  Point[][] points;
  int width;
  int height;

  protected Day10() {
    super("day10");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      height = input.size();
      width = input.getFirst().length();
      points = new Point[width][height];
      List<Point> zeros = new ArrayList<>();
      range(0, height).forEach(y -> range(0, width).forEach(x -> {
        var p = new Point(x, y, parseInt("" + input.get(y).charAt(x)));
        points[x][y] = p;
        if (p.height == 0) {
          zeros.add(p);
        }
      }));

      // part1
      long count = zeros.stream().map(this::trails)
          .mapToLong(trails -> trails.stream().map(List::getLast).distinct().count()).sum();
      System.out.println(count);
      // part2
      count = zeros.stream().map(this::trails).mapToLong(List::size).sum();
      System.out.println(count);
    }
  }

  List<List<Point>> trails(Point zero) {
    List<List<Point>> trails = new ArrayList<>();
    List<Point> initial = new ArrayList<>();
    initial.add(zero);
    trails.add(initial);
    while (true) {
      trails = trails.stream().map(trail -> around(trail.getLast(), 1).stream()
              .map(newLocation -> {
                List<Point> newTrail = new ArrayList<>(trail);
                newTrail.add(newLocation);
                return newTrail;
              })
              .toList()
          )
          .flatMap(Collection::stream)
          .toList();
      if (trails.isEmpty() || trails.stream().anyMatch(t -> t.size() == 10)) {
        break;
      }
    }

    return trails.stream().filter(t -> t.size() == 10).toList();
  }

  List<Point> around(Point p, int delta) {
    List<Point> around = new ArrayList<>();
    if (p.x - 1 >= 0 && points[p.x - 1][p.y].height - p.height == delta) {
      around.add(points[p.x - 1][p.y]);
    }
    if (p.x + 1 < width && points[p.x + 1][p.y].height - p.height == delta) {
      around.add(points[p.x + 1][p.y]);
    }
    if (p.y - 1 >= 0 && points[p.x][p.y - 1].height - p.height == delta) {
      around.add(points[p.x][p.y - 1]);
    }
    if (p.y + 1 < height && points[p.x][p.y + 1].height - p.height == delta) {
      around.add(points[p.x][p.y + 1]);
    }
    return around;
  }

  public static void main(String[] args) throws Exception {
    new Day10().solution();
  }


  record Point(int x, int y, int height) {

  }
}
