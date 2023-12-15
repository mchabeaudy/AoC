import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day14 extends AbstractDay {

  class State {
    String value;

    State next;

    long val;

    public State(String value, State next, long val) {
      this.value = value;
      this.next = next;
      this.val = val;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      State state = (State) o;
      return Objects.equals(value, state.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  class RockMap {
    char[][] chars;

    Map<State,State> values = new HashMap<>();

    int width;

    public RockMap(int width, char[][] chars) {
      this.width = width;
      this.chars = chars;
    }

    private long value() {
      return range(0, width).mapToLong(y -> (width - y) * range(0, width).filter(i -> chars[y][i] == 'O').count())
        .sum();
    }

    long val() {
      int k = 0;
      State s = new State(stringValue(), null, value());
      while (k < 1000000000) {
        if (values.containsKey(s)) {
          s = values.get(s);
        } else {
          fullRotate();
          var newState = new State(stringValue(), null, value());
          s.next = newState;
          values.put(s, newState);
          s = newState;
        }
        k++;
      }
      return s.val;
    }

    private String stringValue() {
      return range(0, width).mapToObj(y -> new String(chars[y])).collect(Collectors.joining());
    }

    void fullRotate() {
      move();
      rotate();
      move();
      rotate();
      move();
      rotate();
      move();
      rotate();
    }

    void rotate() {
      char[][] newChars = new char[width][width];
      range(0, width).forEach(y -> range(0, width).forEach(x -> newChars[x][width - 1 - y] = chars[y][x]));
      chars = newChars;
    }

    private void move() {
      int x = 0;
      int y = 0;
      while (x < width && y < width) {
        if (y == 0) {
          y++;
        } else {
          if (chars[y][x] == 'O' && chars[y - 1][x] == '.') {
            chars[y - 1][x] = 'O';
            chars[y][x] = '.';
            y--;
          } else {
            if (y == width - 1) {
              y = 0;
              x++;
            } else {
              y++;
            }
          }
        }
      }
    }
  }

  public Day14() {
    super("day14");
  }

  public static void main(String[] args) throws Exception {
    new Day14().solution();
  }

  @Override
  public void solution() throws Exception {
    var allLines = Files.readAllLines(getPath());
    int width = allLines.size();
    char[][] chars = new char[width][width];
    range(0, width).forEach(y -> chars[y] = allLines.get(y).toCharArray());
    var map = new RockMap(width, chars);
    System.out.println(map.val());
  }

}
