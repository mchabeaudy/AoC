import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day19 extends AbstractDay {

  public Day19() {
    super("day19");
  }

  List<String> towels;
  List<String> desiredDesigns;

  Map<String, Long> results = new HashMap<>();

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      towels = Arrays.stream(input.getFirst().split(", ")).toList();

      desiredDesigns = range(2, input.size()).mapToObj(input::get).toList();

      // part 1
      System.out.println(count1());

      // part 2
      System.out.println(desiredDesigns.stream().mapToLong(this::count2).sum());
    }
  }

  private int count1() {
    int count = 0;
    for (String design : desiredDesigns) {
      Set<String> designs = new HashSet<>();
      designs.add(design);
      while (!(designs.isEmpty() || designs.contains(""))) {
        designs = new HashSet<>(designs.stream().map(this::expand).flatMap(Collection::stream).toList());
      }
      if (designs.contains("")) {
        count++;
      }
    }
    return count;
  }

  private long count2(String design) {
    if ("".equals(design)) {
      return 1;
    }
    if (results.containsKey(design)) {
      return results.get(design);
    }
    var res = expand(design).stream().mapToLong(this::count2).sum();
    results.put(design, res);
    return res;
  }

  List<String> expand(String design) {
    return towels.stream().filter(design::startsWith).map(d -> design.substring(d.length())).toList();
  }

  public static void main(String[] args) throws Exception {
    new Day19().solution();
  }
}
