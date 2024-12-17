import java.nio.file.Files;

public class Day24 extends AbstractDay {

  public Day24() {
    super("day24");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day24().solution();
  }
}
