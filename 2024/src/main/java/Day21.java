import java.nio.file.Files;

public class Day21 extends AbstractDay {

  public Day21() {
    super("day21");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day21().solution();
  }
}
