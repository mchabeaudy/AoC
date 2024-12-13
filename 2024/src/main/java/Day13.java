import static java.lang.Long.parseLong;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends AbstractDay {

  List<Game> games = new ArrayList<>();
  List<Game> games2 = new ArrayList<>();
  static long part2Diff = 10000000000000L;

  public Day13() {
    super("day13");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      String r1 = "X\\+(\\d+).*Y\\+(\\d+)";
      String r2 = "X=(\\d+).*Y=(\\d+)";
      Pattern p1 = Pattern.compile(r1);
      Pattern p2 = Pattern.compile(r2);
      for (int k = 0; k + 3 <= input.size(); k++) {

        var l1 = input.get(k);
        Matcher m1 = p1.matcher(l1);
        var l2 = input.get(k + 1);
        Matcher m2 = p1.matcher(l2);
        var l3 = input.get(k + 2);
        Matcher m3 = p2.matcher(l3);
        if (m1.find() && m2.find() && m3.find()) {
          games.add(new Game(
              new long[]{parseLong(m1.group(1)), parseLong(m1.group(2))},
              new long[]{parseLong(m2.group(1)), parseLong(m2.group(2))},
              new long[]{parseLong(m3.group(1)), parseLong(m3.group(2))}
          ));
        }
      }
      System.out.println(games.stream().mapToLong(Game::result).sum());

      games2 = games.stream().map(Game::game2).toList();
      System.out.println(games2.stream().mapToLong(Game::result).sum());
    }

  }

  public static void main(String[] args) throws Exception {
    new Day13().solution();
  }

  public record Game(long[] v1, long[] v2, long[] v3) {

    public static Game game2(Game g) {
      return new Game(g.v1, g.v2, new long[]{g.v3[0] + part2Diff, g.v3[1] + part2Diff});
    }

    long result() {
      long det = v1[0] * v2[1] - v1[1] * v2[0];
      if (det == 0) {
        return 0;
      }

      long k1Numerator = v3[0] * v2[1] - v3[1] * v2[0];
      long k2Numerator = v1[0] * v3[1] - v1[1] * v3[0];

      if (k1Numerator % det != 0 || k2Numerator % det != 0) {
        return 0;
      }

      long k1Particular = k1Numerator / det;
      long k2Particular = k2Numerator / det;

      long k1Dir = v2[1] / det;
      long k2Dir = -v1[1] / det;

      // minimize 3*k1 + k2
      long minValue = Long.MAX_VALUE;

      for (long t = -1000; t <= 1000; t++) {
        long k1 = k1Particular + t * k1Dir;
        long k2 = k2Particular + t * k2Dir;
        long value = 3 * k1 + k2;
        if (value < minValue) {
          minValue = value;
        }
      }
      return minValue;
    }
  }
}
