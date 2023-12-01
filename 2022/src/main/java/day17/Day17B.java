package day17;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.AbstractDay;

public class Day17B extends AbstractDay {

  public Day17B() {
    super("day17");
  }

  public static void main(String[] args) throws Exception {
    new Day17B().solution();
  }

  Map<Integer, List<Shape>> shapes = new HashMap<>();

  List<Shape> allShapes = new ArrayList<>();

  String line;

  long totalCount = 1_000_000_000_000L;

  int index;

  boolean lookForPeriod = false;

  int size;

  Shape shape;

  long nbShape;

  @Override
  public void solution() throws Exception {
    line = Files.readString(getPath());
    size = line.length();

    index = 0;

    List<Shape> ground = new ArrayList<>();
    ground.add(new Minus(0, 0));
    ground.add(new Minus(4, 0));
    shapes.put(0, ground);
    int k = 0;
    long max = 0;
    while (nbShape < totalCount) {
      if (nbShape % 2000 == 0) {
        System.out.println("nbShape:" + nbShape);
      }
      int highest = shapes.keySet().stream().max(Comparator.naturalOrder()).orElseThrow();
      var highestY = range(highest - 4, highest + 4)
        .map(i -> shapes.getOrDefault(i, new ArrayList<>())
          .stream()
          .map(Shape::getPoints)
          .flatMap(Collection::stream)
          .mapToInt(p -> p.y)
          .max().orElse(0))
        .max().orElse(0);
      nextShape(highestY + 4);
      boolean fall = true;
      while (fall) {
        List<Point> pts = around();

        char c = nextChar();
        if (c == '<') {
          shape.moveLeft(pts);
        } else {
          shape.moveRight(pts);
        }
        fall = shape.moveDown(pts);
      }
      var l = shapes.getOrDefault(shape.y, new ArrayList<>());
      l.add(shape);
      shapes.put(shape.y, l);
      allShapes.add(shape);
    }

    System.out.println(max);
  }

  void print(String s, int yMax) {
    System.out.println();
    System.out.println(s);
    range(-4, yMax).map(i -> yMax - i)
      .forEach(y -> {
        String l = "|";
        var pts = shapes.values()
          .stream()
          .flatMap(Collection::stream)
          .map(Shape::getPoints)
          .flatMap(Collection::stream)
          .toList();
        l += range(1, 8).mapToObj(x -> {
          if (shape.getPoints().stream().anyMatch(Point.of(x, y)::equals)) {
            return "@";
          }
          if (pts.stream().anyMatch(Point.of(x, y)::equals)) {
            return "#";
          }
          return ".";
        }).collect(Collectors.joining());
        l += "|";
        System.out.println(l);
      });
    System.out.println("+-------+");
  }

  List<Point> around() {
    List<Point> points = new ArrayList<>();
    range(shape.y - 4, shape.y + 5).forEach(y -> {
      points.add(Point.of(0, y));
      points.add(Point.of(8, y));
    });
    range(shape.y - 4, shape.y + 5).forEach(y -> {
      shapes.getOrDefault(y, Collections.emptyList())
        .stream()
        .map(Shape::getPoints)
        .forEach(points::addAll);
    });
    return points;
  }

  void nextShape(int y) {
    nbShape++;
    if (shape == null || shape instanceof Square) {
      shape = new Minus(3, y);
    } else if (shape instanceof Minus) {
      shape = new Plus(3, y);
    } else if (shape instanceof Plus) {
      shape = new LastShape(3, y);
    } else if (shape instanceof LastShape) {
      shape = new Bar(3, y);
    } else {
      shape = new Square(3, y);
    }
  }

  char nextChar() {
    if (index == line.length()) {
      index = 0;
      lookForPeriod = true;
    }
    char c = line.charAt(index);
    index++;
    return c;
  }

  abstract class Shape {
    int x;

    int y;

    public Shape(int x, int y) {
      this.x = x;
      this.y = y;
    }

    abstract boolean moveDown(List<Point> walls);

    abstract void moveRight(List<Point> walls);

    abstract void moveLeft(List<Point> walls);

    abstract List<Point> getPoints();
  }

  class Plus extends Shape {
    public Plus(int x, int y) {
      super(x, y);
    }

    @Override
    boolean moveDown(List<Point> walls) {
      boolean b1 = walls.stream().noneMatch(Point.of(x, y)::equals);
      boolean b2 = walls.stream().noneMatch(Point.of(x + 1, y - 1)::equals);
      boolean b3 = walls.stream().noneMatch(Point.of(x + 2, y)::equals);
      if (b1 && b2 && b3) {
        y--;
        return true;
      }
      return false;
    }

    @Override
    void moveRight(List<Point> walls) {
      boolean b1 = walls.stream().noneMatch(Point.of(x + 2, y + 2)::equals);
      boolean b2 = walls.stream().noneMatch(Point.of(x + 3, y + 1)::equals);
      boolean b3 = walls.stream().noneMatch(Point.of(x + 2, y)::equals);
      if (b1 && b2 && b3) {
        x++;
      }
    }

    @Override
    void moveLeft(List<Point> walls) {
      boolean b1 = walls.stream().noneMatch(Point.of(x, y + 2)::equals);
      boolean b2 = walls.stream().noneMatch(Point.of(x - 1, y + 1)::equals);
      boolean b3 = walls.stream().noneMatch(Point.of(x, y)::equals);
      if (b1 && b2 && b3) {
        x--;
      }
    }

    @Override
    List<Point> getPoints() {
      return List.of(Point.of(x + 1, y + 2), Point.of(x + 1, y + 1), Point.of(x + 1, y), Point.of(x, y + 1),
        Point.of(x + 2, y + 1));
    }

    @Override
    public String toString() {
      return "Plus{" +
        "x=" + x +
        ", y=" + y +
        '}';
    }
  }

  class Minus extends Shape {

    public Minus(int x, int y) {
      super(x, y);
    }

    @Override
    boolean moveDown(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.y == y - 1 && p.x > x - 1 && p.x < x + 4)) {
        y--;
        return true;
      }
      return false;
    }

    @Override
    void moveRight(List<Point> walls) {
      if (walls.stream().noneMatch(Point.of(x + 4, y)::equals)) {
        x++;
      }
    }

    @Override
    void moveLeft(List<Point> walls) {
      if (walls.stream().noneMatch(Point.of(x - 1, y)::equals)) {
        x--;
      }
    }

    @Override
    List<Point> getPoints() {
      return List.of(Point.of(x, y), Point.of(x + 1, y), Point.of(x + 2, y), Point.of(x + 3, y));
    }

    @Override
    public String toString() {
      return "Minus{" +
        "x=" + x +
        ", y=" + y +
        '}';
    }
  }

  class Bar extends Shape {

    public Bar(int x, int y) {
      super(x, y);
    }

    @Override
    boolean moveDown(List<Point> walls) {
      if (walls.stream().noneMatch(Point.of(x, y - 1)::equals)) {
        y--;
        return true;
      }
      return false;
    }

    @Override
    void moveRight(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.x == x + 1 && p.y > y - 1 && p.y < y + 4)) {
        x++;
      }
    }

    @Override
    void moveLeft(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.x == x - 1 && p.y > y - 1 && p.y < y + 4)) {
        x--;
      }
    }

    @Override
    List<Point> getPoints() {
      return List.of(Point.of(x, y), Point.of(x, y + 1), Point.of(x, y + 2), Point.of(x, y + 3));
    }

    @Override
    public String toString() {
      return "Bar{" +
        "x=" + x +
        ", y=" + y +
        '}';
    }
  }

  class Square extends Shape {
    public Square(int x, int y) {
      super(x, y);
    }

    @Override
    boolean moveDown(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.y == y - 1 && p.x > x - 1 && p.x < x + 2)) {
        y--;
        return true;
      }
      return false;
    }

    @Override
    void moveRight(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.x == x + 2 && p.y > y - 1 && p.y < y + 2)) {
        x++;
      }
    }

    @Override
    void moveLeft(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.x == x - 1 && p.y > y - 1 && p.y < y + 2)) {
        x--;
      }
    }

    @Override
    List<Point> getPoints() {
      return List.of(Point.of(x, y), Point.of(x, y + 1), Point.of(x + 1, y + 1), Point.of(x + 1, y));
    }

    @Override
    public String toString() {
      return "Square{" +
        "x=" + x +
        ", y=" + y +
        '}';
    }
  }

  class LastShape extends Shape {
    public LastShape(int x, int y) {
      super(x, y);
    }

    @Override
    boolean moveDown(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.y == y - 1 && p.x > x - 1 && p.x < x + 3)) {
        y--;
        return true;
      }
      return false;
    }

    @Override
    void moveRight(List<Point> walls) {
      if (walls.stream().noneMatch(p -> p.x == x + 3 && p.y > y - 1 && p.y < y + 3)) {
        x++;
      }
    }

    @Override
    void moveLeft(List<Point> walls) {
      boolean b1 = walls.stream().noneMatch(Point.of(x - 1, y)::equals);
      boolean b2 = walls.stream().noneMatch(p -> p.x == x + 1 && p.y > y && p.y < y + 3);
      if (b1 && b2) {
        x--;
      }
    }

    @Override
    List<Point> getPoints() {
      return List.of(Point.of(x, y), Point.of(x + 1, y), Point.of(x + 2, y), Point.of(x + 2, y + 1),
        Point.of(x + 2, y + 2));
    }

    @Override
    public String toString() {
      return "LastShape{" +
        "x=" + x +
        ", y=" + y +
        '}';
    }
  }

  record Point(int x, int y) {
    static Point of(int x, int y) {
      return new Point(x, y);
    }
  }
}

