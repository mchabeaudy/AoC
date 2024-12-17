import static java.lang.Math.min;
import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 extends AbstractDay {

  public Day17() {
    super("day17");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day17().solution();
  }
}
