import static java.lang.Long.parseLong;

import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.BiFunction;

public class Day7 extends AbstractDay {

  protected Day7() {
    super("day7");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var operators = lines.map(line -> {
        var split = line.split(": ");
        long result = parseLong(split[0]);
        var operator = Arrays.stream(split[1].split(" ")).map(Long::parseLong).toList();
        return new Operation(result, operator);
      }).toList();

      // part 1
      System.out.println(operators.stream().filter(Operation::isValidPart1).mapToLong(Operation::result).sum());
      // part 2
      System.out.println(operators.stream().filter(Operation::isValidPart2).mapToLong(Operation::result).sum());
    }
  }

  public static void main(String[] args) throws Exception {
    new Day7().solution();
  }

  record Operation(long result, List<Long> operators) {

    public boolean isValidPart1() {
      return isValid(List.of(
              Long::sum,
              (v0, v1) -> v0 * v1
          )
      );
    }

    public boolean isValidPart2() {
      return isValid(List.of(
              Long::sum,
              (v0, v1) -> v0 * v1,
              (v0, v1) -> parseLong(String.valueOf(v0) + v1)
          )
      );
    }

    public boolean isValid(List<BiFunction<Long, Long, Long>> operations) {
      List<Long> results = new ArrayList<>();
      Deque<Long> queue = new ArrayDeque<>(operators);
      results.add(queue.pop());
      while (!queue.isEmpty()) {
        var operator = queue.pop();
        results = results.stream().map(
                res -> operations.stream()
                    .map(operation -> operation.apply(res, operator))
                    .filter(newResult -> newResult <= result)
                    .toList())
            .flatMap(List::stream)
            .toList();
      }
      return results.stream().anyMatch(r -> r == result);
    }

  }
}
