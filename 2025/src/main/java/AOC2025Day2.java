import java.nio.file.Files;
import java.util.Arrays;

public class AOC2025Day2 extends AbstractDay2025 {

  public AOC2025Day2() {
    super("day2");
  }

  static void main() throws Exception {
    new AOC2025Day2().solution();
  }

  void solution() throws Exception {
    long invalidCount = 0;
    long invalidCount2 = 0;
    try (var lines = Files.lines(getPath())) {
      var list = Arrays.stream(lines.toList().getFirst().split(",")).toList();
      for (var line : list) {
        var ranges = line.split("-");
        long min = Long.parseLong(ranges[0]);
        long max = Long.parseLong(ranges[1]);
        for (long i = min; i <= max; i++) {
          if (isInvalid(i)) {
            invalidCount += i;
          }
          if (isInvalid2(i)) {
            invalidCount2 += i;
          }
        }
      }
    }

    // part 1
    System.out.println(invalidCount);
    System.out.println(invalidCount2);
  }

  boolean isInvalid(long n) {
    String s = Long.toString(n);
    if (s.length() % 2 != 0) {
      return false;
    }
    String half = s.substring(0, s.length() / 2);
    String otherHalf = s.substring(s.length() / 2);
    return half.equals(otherHalf);
  }

  boolean isInvalid2(long n) {
    String s = Long.toString(n);
    var length = s.length();
    for (int k = 1; k < length; k++) {
      var sub = s.substring(0, k);
      var val = "";
      for (int l = 0; l < length / k; l++) {
        val += sub;
      }
      if (val.equals(s)) {
        return true;
      }
    }
    return false;
  }

}
