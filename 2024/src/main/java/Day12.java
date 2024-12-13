import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

public class Day12 extends AbstractDay {

  List<List<Point>> areas = new ArrayList<>();
  Point[][] points;
  int height;
  int width;

  public Day12() {
    super("day12");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      height = input.size();
      width = input.getFirst().length();
      points = new Point[width][height];
      range(0, height).forEach(
          y -> range(0, width).forEach(x -> points[x][y] = new Point(x, y, input.get(y).charAt(x))));

      range(0, height).forEach(y -> range(0, width).forEach(x -> addToArea(points[x][y])));

      System.out.println(areas.stream().mapToInt(this::countBorder).sum());
      System.out.println(areas.stream().mapToInt(area -> area.size() * countSides(area)).sum());
    }

  }

  private void addToArea(Point point) {
    List<List<Point>> commonAreas = areas.stream().filter(l -> l.stream().anyMatch(point::isInSameArea))
        .toList();
    List<Point> newPoints = new ArrayList<>();
    if (commonAreas.isEmpty()) {
      newPoints.add(point);
    } else {
      commonAreas.forEach(newPoints::addAll);
      newPoints.add(point);
      areas.removeAll(commonAreas);
    }
    areas.add(newPoints);
  }

  public int countSides(List<Point> area) {
    List<Side> areaSides = new ArrayList<>();
    Queue<Point> queue = new ArrayDeque<>(area);
    while (!queue.isEmpty()) {
      Point areaPoint = queue.poll();
      processSide(area, areaPoint, areaSides, areaPoint::isRightTo, Orientation.RIGHT);
      processSide(area, areaPoint, areaSides, areaPoint::isLeftTo, Orientation.LEFT);
      processSide(area, areaPoint, areaSides, areaPoint::isTopTo, Orientation.TOP);
      processSide(area, areaPoint, areaSides, areaPoint::isBottomTo, Orientation.BOTTOM);
    }
    return areaSides.size();
  }

  private void processSide(List<Point> area, Point point, List<Side> sides, Predicate<Point> filter, Orientation o) {
    if (area.stream().noneMatch(filter)) {
      List<Side> nearSides = sides.stream()
          .filter(l -> l.orientation == o && l.points.stream().anyMatch(p -> p.dist2(point) == 1))
          .toList();
      if (nearSides.isEmpty()) {
        List<Point> pts = new ArrayList<>();
        pts.add(point);
        sides.add(new Side(pts, o));
      } else if (nearSides.size() == 1) {
        nearSides.getFirst().points.add(point);
      } else {
        List<Point> pts = new ArrayList<>();
        nearSides.forEach(l -> pts.addAll(l.points));
        sides.removeAll(nearSides);
        sides.add(new Side(pts, o));
      }
    }
  }

  public int countBorder(List<Point> area) {
    return area.stream().mapToInt(this::countFence).sum() * area.size();
  }

  public int countFence(Point p) {
    int count = 0;
    if (p.x == 0 || (points[p.x - 1][p.y].plant != p.plant)) {
      count++;
    }
    if (p.x == width - 1 || (points[p.x + 1][p.y].plant != p.plant)) {
      count++;
    }
    if (p.y == 0 || (points[p.x][p.y - 1].plant != p.plant)) {
      count++;
    }
    if (p.y == height - 1 || (points[p.x][p.y + 1].plant != p.plant)) {
      count++;
    }
    return count;
  }

  public static void main(String[] args) throws Exception {
    new Day12().solution();
  }

  public record Point(int x, int y, char plant) {

    boolean isInSameArea(Point p) {
      boolean isSame = plant == p.plant;
      if (isSame) {
        int dx = Math.abs(p.x - x);
        int dy = Math.abs(p.y - y);
        return dx + dy == 1;
      }
      return false;
    }

    boolean isLeftTo(Point p) {
      return p.x - x == 1 && p.y == y;
    }

    boolean isRightTo(Point p) {
      return x - p.x == 1 && y == p.y;
    }

    boolean isTopTo(Point p) {
      return y - p.y == 1 && p.x == x;
    }

    boolean isBottomTo(Point p) {
      return p.y - y == 1 && p.x == x;
    }

    public int dist2(Point pp) {
      int dx = pp.x - x;
      int dy = pp.y - y;
      return dx * dx + dy * dy;
    }
  }

  public record Side(List<Point> points, Orientation orientation) {

  }

  public enum Orientation {
    TOP, BOTTOM, LEFT, RIGHT
  }
}
