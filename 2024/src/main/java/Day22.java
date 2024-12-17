import java.nio.file.Files;

public class Day22 extends AbstractDay {

  public Day22() {
    super("day22");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
    }
  }

  public static void main(String[] args) throws Exception {
    new Day22().solution();
  }
}
