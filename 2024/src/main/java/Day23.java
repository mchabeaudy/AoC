import java.nio.file.Files;

public class Day23 extends AbstractDay {

  public Day23() {
    super("day23");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day23().solution();
  }
}
