import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Day2 extends AbstractDay {

  public Day2() {
    super("day2");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var reports = lines.map(line -> Arrays.stream(line.split(" "))
              .map(Integer::parseInt).toList())
          .map(Report::new).toList();

      // part 1
      System.out.println(reports.stream().filter(Report::isSafe).count());

      // part 2
      System.out.println(reports.stream().filter(Report::isSafe2).count());
    }
  }

  record Report(List<Integer> levels) {

    boolean isSafe() {
      boolean increasing = levels.get(0) < levels.get(1);
      int k = 0;
      while (k < levels.size() - 1) {
        k++;
        int diff = levels.get(k) - levels.get(k - 1);
        if (increasing) {
          if (diff > 3 || diff < 1) {
            return false;
          }
        } else {
          if (diff < -3 || diff > -1) {
            return false;
          }
        }
      }
      return true;
    }

    List<Report> subReports() {
      return range(0, levels.size())
          .mapToObj(i -> range(0, levels.size()).filter(k -> k != i).map(levels::get).boxed().toList())
          .map(Report::new).toList();
    }

    boolean isSafe2() {
      return subReports().stream().anyMatch(Report::isSafe);
    }
  }

  public static void main(String[] args) throws Exception {
    new Day2().solution();
  }
}
