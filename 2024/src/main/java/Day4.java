import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.List;

public class Day4 extends AbstractDay {

  char[][] chars;
  int width;
  int height;

  public Day4() {
    super("day4");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      width = inputs.getFirst().length();
      height = inputs.size();
      chars = new char[height][width];
      range(0, height).forEach(i -> chars[i] = inputs.get(i).toCharArray());

      // part1
      long count = range(0, height).map(y -> range(0, width).map(x -> countXmas(y, x)).sum()).sum();
      System.out.println(count);

      // part2
      count = range(0, height).map(y -> range(0, width).map(x -> countXmas2(y, x)).sum()).sum();
      System.out.println(count);
    }
  }

  private int countXmas2(int y, int x) {
    var l0 = List.of(
        new Point(0, 0),
        new Point(-2, 0),
        new Point(-1, 1),
        new Point(0, 2),
        new Point(-2, 2)
    );
    var l1 = rotate(l0);
    var l2 = rotate(l1);
    var l3 = rotate(l2);
    var vect = List.of(l0, l1, l2, l3);
    List<Character> xmasChars = List.of('M', 'M', 'A', 'S', 'S');

    return (int) vect.stream()
        .filter(v -> isXmas2(y, x, v, xmasChars))
        .count();
  }

  List<Point> rotate(List<Point> list) {
    return list.stream().map(p -> new Point(-p.y, p.x)).toList();
  }

  private boolean isXmas2(int y, int x, List<Point> v, List<Character> xmasChars) {
    return range(0, xmasChars.size()).allMatch(i -> valueOf(v.get(i).x + x, v.get(i).y + y) == xmasChars.get(i));
  }

  private int countXmas(int y, int x) {
    var vect = List.of(
        new Point(-1, -1),
        new Point(-1, 0),
        new Point(-1, 1),
        new Point(0, 1),
        new Point(1, 1),
        new Point(1, 0),
        new Point(1, -1),
        new Point(0, -1)
    );
    List<Character> xmasChars = List.of('X', 'M', 'A', 'S');

    return (int) vect.stream()
        .filter(v -> range(0, xmasChars.size()).allMatch(i -> valueOf(x + i * v.x, y + i * v.y) == xmasChars.get(i)))
        .count();
  }

  char valueOf(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      return '.';
    }
    return chars[y][x];
  }

  public static void main(String[] args) throws Exception {
    new Day4().solution();
  }

  record Point(int x, int y) {

  }
}
