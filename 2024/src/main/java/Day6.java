import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 extends AbstractDay {

  char[][] map;
  int width;
  int height;
  int[] p0;

  protected Day6() {
    super("day6");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      width = inputs.getFirst().length();
      height = inputs.size();
      map = new char[height][width];

      range(0, height).forEach(y -> range(0, width).forEach(x -> {
        map[y][x] = inputs.get(y).charAt(x);
        if (inputs.get(y).charAt(x) == '^') {
          p0 = new int[]{x, y};
        }
      }));

      int[] p = p0;
      int[] direction = new int[]{0, -1};
      map[p[1]][p[0]] = 'X';
      while (isInMap(p)) {
        var next = next(p, direction);
        while (isInMap(next) && map[next[1]][next[0]] == '#') {
          direction = rotateRight(direction);
          next = next(p, direction);
        }
        p = next;
        if (isInMap(p)) {
          map[p[1]][p[0]] = 'X';
        }
      }
      System.out.println(countX());

      List<int[]> possibleBlocks = new ArrayList<>();
      range(0, height).forEach(y -> range(0, width).filter(x -> map[y][x] == 'X')
          .forEach(x -> {
                if (!isP0(x, y)) {
                  possibleBlocks.add(new int[]{x, y});
                }
              }
          )
      );

      System.out.println(possibleBlocks.stream().filter(this::isBlocking).count());
    }
  }

  boolean isBlocking(int[] block) {
    map[block[1]][block[0]] = '#';
    int[] p = p0;
    int[] direction = new int[]{0, -1};
    int[] state = new int[]{p0[0], p0[1], direction[0], direction[1]};
    List<int[]> states = new ArrayList<>();
    states.add(state);
    while (isInMap(p)) {
      var next = next(p, direction);
      while (isInMap(next) && map[next[1]][next[0]] == '#') {
        direction = rotateRight(direction);
        next = next(p, direction);
      }
      p = next;
      int[] newState = new int[]{p[0], p[1], direction[0], direction[1]};
      if (!stateIsNew(newState, states)) {
        map[block[1]][block[0]] = 'X';
        return true;
      }
      states.add(newState);
    }
    map[block[1]][block[0]] = 'X';
    return false;
  }

  private boolean stateIsNew(int[] newState, List<int[]> states) {
    return states.stream().noneMatch(sate -> Arrays.equals(newState, sate));
  }

  boolean isP0(int x, int y) {
    return x == p0[0] && y == p0[1];
  }

  private int countX() {
    return range(0, height).map(y -> (int) range(0, width).filter(x -> map[y][x] == 'X').count()).sum();
  }

  int[] next(int[] p, int[] direction) {
    return new int[]{p[0] + direction[0], p[1] + direction[1]};
  }

  int[] rotateRight(int[] direction) {
    return new int[]{-direction[1], direction[0]};
  }

  boolean isInMap(int[] p) {
    return p[0] >= 0 && p[0] < width && p[1] >= 0 && p[1] < height;
  }

  public static void main(String[] args) throws Exception {
    new Day6().solution();
  }

}
