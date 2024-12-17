import java.nio.file.Files;

public class Day20 extends AbstractDay {

  public Day20() {
    super("day20");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day20().solution();
  }
}
