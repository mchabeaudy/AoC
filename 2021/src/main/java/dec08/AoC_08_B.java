package dec08;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AoC_08_B {

  private static final String FILE = "dec08.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)))
        .map(s -> s.split(" \\| "))
        .map(s -> new Entry(stringList(s[0], " "), stringList(s[1], " ")))
        .toList();
    lines.forEach(Entry::calculateMap);

    var sum = lines.stream().mapToInt(Entry::calculateOutput).sum();
    System.out.println(sum);
  }

  static List<String> stringList(String list, String split) {
    return Arrays.stream(list.split(split)).toList();
  }

  static class Entry {

    List<String> digits;
    List<String> output;
    Map<String, String> map = new HashMap<>();

    public Entry(List<String> digits, List<String> output) {
      this.digits = digits;
      this.output = output;
    }

    void calculateMap() {

      var one = digits.stream().filter(s -> s.length() == 2).findAny().orElseThrow();
      var four = digits.stream().filter(s -> s.length() == 4).findAny().orElseThrow();
      var seven = digits.stream().filter(s -> s.length() == 3).findAny().orElseThrow();
      var eight = digits.stream().filter(s -> s.length() == 7).findAny().orElseThrow();

      var three = digits.stream().filter(s -> s.length() == 5
              && Arrays.stream(s.split(""))
              .filter(k -> stringList(seven, "").contains(k))
              .count() == 3)
          .findAny()
          .orElseThrow();

      var nine = digits.stream().filter(s -> s.length() == 6 && Arrays.stream(s.split(""))
              .filter(k -> stringList(three, "").contains(k))
              .count() == 5)
          .findAny().orElseThrow();

      var eBar = Arrays.stream(eight.split(""))
          .filter(s -> !stringList(nine, "").contains(s))
          .findAny().orElseThrow();

      var two = digits.stream().filter(s -> s.length() == 5 && s.contains(eBar))
          .findAny()
          .orElseThrow();

      var six = digits.stream().filter(s -> s.length() == 6 && s.contains(eBar) &&
              Arrays.stream(s.split(""))
                  .filter(k -> stringList(one, "").contains(k))
                  .count() == 1)
          .findAny().orElseThrow();

      var zero = digits.stream().filter(s -> s.length() == 6 && s.contains(eBar) &&
              Arrays.stream(s.split(""))
                  .filter(k -> stringList(one, "").contains(k))
                  .count() == 2)
          .findAny()
          .orElseThrow();

      var five = digits.stream()
          .filter(s -> !List.of(zero, one, three, two, four, six, seven, eight, nine).contains(s))
          .findAny()
          .orElseThrow();

      map.put(zero, "0");
      map.put(one, "1");
      map.put(two, "2");
      map.put(three, "3");
      map.put(four, "4");
      map.put(five, "5");
      map.put(six, "6");
      map.put(seven, "7");
      map.put(eight, "8");
      map.put(nine, "9");
    }

    int calculateOutput() {
      return Integer.parseInt(output.stream().map(
              o -> map.get(map.keySet().stream()
                  .filter(key -> key.length() == o.length() && stringList(key, "")
                      .containsAll(stringList(o, "")))
                  .findAny()
                  .orElseThrow()))
          .collect(Collectors.joining()));
    }
  }


}
