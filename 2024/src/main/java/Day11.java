import static java.lang.Long.parseLong;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day11 extends AbstractDay {

  Map<Long, Map<Long, Long>> results = new HashMap<>();

  public Day11() {
    super("day11");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var line = lines.findAny().orElseThrow();

      // part1
      var sum = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong)
          .map(l -> count(l, 25))
          .sum();
      System.out.println(sum);

      // part2
      sum = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong)
          .map(l -> count(l, 75))
          .sum();
      System.out.println(sum);


    }
  }

  long count(long n, long times) {
    if (results.containsKey(n) && results.get(n).containsKey(times)) {
      return results.get(n).get(times);
    }
    if (times == 0) {
      results.computeIfAbsent(n, k -> new HashMap<>()).put(times, 1L);
      return 1;
    }
    if (n == 0) {
      var result = count(1, times - 1);
      results.computeIfAbsent(n, k -> new HashMap<>()).put(times, result);
      return result;
    }
    var s = String.valueOf(n);
    if (s.length() % 2 == 0) {
      var result = count(parseLong(s.substring(0, s.length() / 2)), times - 1)
          + count(parseLong(s.substring(s.length() / 2)), times - 1);
      results.computeIfAbsent(n, k -> new HashMap<>()).put(times, result);
      return result;
    }
    var result = count(n * 2024, times - 1);
    results.computeIfAbsent(n, k -> new HashMap<>()).put(times, result);
    return result;
  }

  public static void main(String[] args) throws Exception {
    new Day11().solution();
  }

}
