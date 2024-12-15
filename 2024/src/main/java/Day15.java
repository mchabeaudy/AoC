import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 extends AbstractDay {

  public Day15() {
    super("day15");
  }

  int height;
  int width;

  Point[][] points;
  List<Unit> units = new ArrayList<>();
  Unit robot;
  List<Direction> directions = new ArrayList<>();


  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      for (var i = 0; i < input.size(); i++) {
        var line = input.get(i);
        if (i == 0) {
          width = line.length();
        }
        if (line.startsWith("#")) {
          height++;
        }
      }
//      points = new Point[height][width];
//      range(0, height).forEach(y -> range(0, width).forEach(x -> initPoint(height - y - 1, x, input)));
//
      for (var i = height + 1; i < input.size(); i++) {
        var line = input.get(i);
        for (var j = 0; j < line.length(); j++) {
          directions.add(Direction.fromSymbol(line.charAt(j)));
        }
      }

      // part 1
//      directions.forEach(robot::move);
//      System.out.println(units.stream().mapToLong(u -> 100L * (height - u.y - 1) + u.x).sum());

      // part 2
      units.clear();
      points = new Point[height][width * 2];
      range(0, height).forEach(y -> range(0, width).forEach(x -> initPoint2(height - y - 1, x, input)));
      width *= 2;
      directions.forEach(robot::move);
      System.out.println(units.stream().mapToLong(u -> 100L * (height - u.y - 1) + u.x).sum());
    }
  }

  private void initPoint(int y, int x, List<String> input) {
    var c = input.get(height - y - 1).charAt(x);
    Point p;
    if (c == '#' || c == '.') {
      p = new Point(x, y, c);
    } else if (c == 'O') {
      p = new Point(x, y, '.');
      units.add(new Unit(x, y));
    } else {
      p = new Point(x, y, '.');
      robot = new Unit(x, y);
    }
    points[y][x] = p;
  }

  private void initPoint2(int y, int x, List<String> input) {
    var c = input.get(height - y - 1).charAt(x);
    Point p1;
    Point p2;
    if (c == '#' || c == '.') {
      p1 = new Point(2 * x, y, c);
      p2 = new Point(2 * x + 1, y, c);
    } else if (c == 'O') {
      p1 = new Point(2 * x, y, '.');
      p2 = new Point(2 * x + 1, y, '.');
      units.add(new Unit(2 * x, y, true));
    } else {
      p1 = new Point(2 * x, y, '.');
      p2 = new Point(2 * x + 1, y, '.');
      robot = new Unit(2 * x, y);
    }
    points[y][2 * x] = p1;
    points[y][2 * x + 1] = p2;
  }

  public static void main(String[] args) throws Exception {
    new Day15().solution();
  }

  record Point(int x, int y, char type) {

    boolean isWall() {
      return type == '#';
    }
  }

  class Unit {

    int x;
    int y;
    boolean brace;

    public Unit(int x, int y) {
      this(x, y, false);
    }

    public Unit(int x, int y, boolean brace) {
      this.x = x;
      this.y = y;
      this.brace = brace;
    }

    public void move(Direction direction) {
      var toMove = linkedMove(direction);
//      printState();
      if (toMove.stream().allMatch(u -> u.canMove(direction))) {
        toMove.forEach(u -> u.applyMove(direction));
      }
    }

    private void printState() {
      System.out.println("______");
      for (int y = height - 1; y >= 0; y--) {
        var l = "";
        for (var x = 0; x < width; x++) {
          if (points[y][x].type == '#') {
            l += '#';
          } else {
            int xf = x;
            int yf = y;
            if (units.stream().anyMatch(u -> u.contains(xf, yf))) {
              l += 'O';
            } else if (robot.contains(xf, yf)) {
              l += '@';
            } else {
              l += '.';
            }
          }
        }
        System.out.println(l);
      }
    }

    void applyMove(Direction direction) {
      x = nextX(direction);
      y = nextY(direction);
    }

    public boolean canMove(Direction direction) {
      boolean b = !points[nextY(direction)][nextX(direction)].isWall();
      if (brace) {
        b = b && !points[nextY(direction)][nextX(direction) + 1].isWall();
      }
      return b;
    }

    int nextX(Direction direction) {
      return switch (direction) {
        case UP, DOWN -> x;
        case LEFT -> x - 1;
        case RIGHT -> x + 1;
      };
    }

    int nextY(Direction direction) {
      return switch (direction) {
        case RIGHT, LEFT -> y;
        case DOWN -> y - 1;
        case UP -> y + 1;
      };
    }

    public Set<Unit> linkedMove(Direction direction) {
      Set<Unit> linkedUnits = new HashSet<>();
      linkedUnits.add(this);
      linkedUnits.addAll(units.stream()
          .filter(not(this::equals))
          .filter(u -> {
            boolean b = u.contains(nextX(direction), nextY(direction));
            if (brace) {
              b = b || u.contains(nextX(direction) + 1, nextY(direction));
            }
            return b;
          })
          .map(u -> u.linkedMove(direction))
          .flatMap(Collection::stream)
          .collect(Collectors.toSet())
      );
      return linkedUnits;
    }

    boolean contains(int x, int y) {
      boolean b = this.x == x && this.y == y;
      if (brace) {
        b = b || this.x + 1 == x && this.y == y;
      }
      return b;
    }
  }

  enum Direction {
    UP, RIGHT, LEFT, DOWN;

    public static Direction fromSymbol(char symbol) {
      return switch (symbol) {
        case '^' -> UP;
        case '>' -> RIGHT;
        case '<' -> LEFT;
        case 'v' -> DOWN;
        default -> throw new IllegalArgumentException();
      };
    }
  }

}
