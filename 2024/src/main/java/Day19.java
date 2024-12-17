import java.nio.file.Files;

public class Day19 extends AbstractDay {

  public Day19() {
    super("day19");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day19().solution();
  }
}
