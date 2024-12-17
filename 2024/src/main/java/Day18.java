import java.nio.file.Files;

public class Day18 extends AbstractDay {

  public Day18() {
    super("day18");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day18().solution();
  }
}
