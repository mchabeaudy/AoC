import java.nio.file.Files;

public class AOC2025Day1 extends AbstractDay2025 {

  public AOC2025Day1() {
    super("day1");
  }

  static void main() throws Exception {
    new AOC2025Day1().solution();
  }

  void solution() throws Exception {
    var dial = new Dial();
    var zeros = 0;
    try (var lines = Files.lines(getPath())) {
      var list = lines.toList();
      for (var line : list) {
        int k = Integer.parseInt(line.substring(1));
        char c = line.charAt(0);
        dial.move(c, k);
        if (dial.pointer == 0) {
          zeros++;
        }
      }
    }

    // part 1
    System.out.println(zeros);
    System.out.println(dial.zeroNb);
  }

  Result eval(int n, int k, int mul) {
    int zeroCount = 0;
    int res = n + k * mul;
    while (res >= 100 || res < 0) {
      zeroCount++;
      if (res >= 100) {
        res -= 100;
      } else {
        res += 100;
      }
    }
    return new Result(res, zeroCount);
  }

  record Result(int result, int zeroCount) {

  }

  class Dial {

    int pointer = 50;
    int zeroNb = 0;

    void move(char c, int v) {
      int count = v;
      while (count > 0) {
        count--;
        pointer = (pointer + (c == 'R' ? 1 : -1));
        if (pointer == -1) {
          pointer = 99;
        }
        if (pointer == 100) {
          pointer = 0;
        }
        if (pointer == 0) {
          zeroNb++;
        }
      }
    }
  }

}
