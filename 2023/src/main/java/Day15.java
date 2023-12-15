import java.nio.file.Files;
import java.util.Arrays;

public class Day15 extends AbstractDay {

  public Day15() {
    super("day15");
  }

  public static void main(String[] args) throws Exception {
    new Day15().solution();
  }

  @Override
  public void solution() throws Exception {
    var line = Files.readString(getPath());
    System.out.println(Arrays.stream(line.split(",")).mapToInt(this::val).sum());
  }

  int val(String s) {
    int k = 0;
    for (char c : s.toCharArray()) {
      int v = k + c;
      k = next(v);
    }
    return k;
  }

  int next(int k) {
    k *= 17;
    k = k % 256;
    return k;
  }

}
