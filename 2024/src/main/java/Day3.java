import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 extends AbstractDay {

  public Day3() {
    super("day3");
  }

  @Override
  void solution() throws Exception {
    String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

    Pattern pattern = Pattern.compile(regex);
    int sum = 0;
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();

      // part 1
      for (var line : inputs) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
          sum += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }
      }
      System.out.println(sum);

      // part 2
      sum = 0;
      boolean enabled = true;
      for (var line : inputs) {
        String regexEnable = "do()";
        String regexDisable = "don't()";
        int i1 = line.indexOf(regexEnable);
        int i2 = line.indexOf(regexDisable);
        Matcher matcher = pattern.matcher(line);
        int i3 = matcher.find() ? matcher.start() : -1;
        while (i1 != -1 || i2 != -1 || i3 != -1) {
          var min = Stream.of(i1, i2, i3).filter(i -> i > 0).min(Integer::compare).orElse(0);
          if (min == i1) {
            enabled = true;
            line = line.substring(i1 + 3);
          } else if (min == i2) {
            enabled = false;
            line = line.substring(i2 + 3);
          } else {
            if (enabled) {
              sum += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
            line = line.substring(i3 + 3);
          }
          i1 = line.indexOf(regexEnable);
          i2 = line.indexOf(regexDisable);
          matcher = pattern.matcher(line);
          i3 = matcher.find() ? matcher.start() : -1;
        }
      }
      System.out.println(sum);

    }
  }

  public static void main(String[] args) throws Exception {
    new Day3().solution();
  }
}
