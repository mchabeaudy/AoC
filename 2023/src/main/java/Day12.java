import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day12 extends AbstractDay {

  Map<Input, Long> values = new HashMap<>();

  record Input(String springs, List<Integer> nums) {
  }

  public Day12() {
    super("day12");
  }

  public static void main(String[] args) throws Exception {
    new Day12().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      var sum1 = inputs.stream().mapToLong(line -> {
        var split = line.split(" ");
        return count(new Input(split[0], Arrays.stream(split[1].split(",")).map(Integer::parseInt).toList()));
      }).sum();
      var sum2 = inputs.stream().mapToLong(line -> {
        var split = line.split(" ");
        var springs = split[0] + '?' + split[0] + '?' + split[0] + '?' + split[0] + '?' + split[0];
        var list = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toList();
        var list5 = range(0, 5).mapToObj(i -> list).flatMap(Collection::stream).toList();
        return count(new Input(springs, list5));
      }).sum();
      System.out.println(sum1);
      System.out.println(sum2);
    }
  }

  long count(Input input) {
    var s = input.springs;
    var nums = new ArrayList<>(input.nums);
    if ("".equals(s)) {
      return nums.isEmpty() ? 1 : 0;
    }
    if (nums.isEmpty()) {
      return s.contains("#") ? 0 : 1;
    }
    if (values.containsKey(input)) {
      return values.get(input);
    }
    long total = 0;
    char c = s.charAt(0);
    if (c == '?' || c == '.') {
      total += count(new Input(s.substring(1), nums));
    }
    if (c == '?' || c == '#') {
      var newNums = new LinkedList<>(nums);
      var n = newNums.pollFirst();
      if (n <= s.length() && !s.substring(0, n).contains(".") && (s.length() == n || s.charAt(n) != '#')) {
        var newString = n == s.length() ? "" : s.substring(n + 1);
        total += count(new Input(newString, newNums));
      }
    }
    values.put(input, total);
    return total;
  }
}
