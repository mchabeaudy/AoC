package dec03;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AoC_03_A {

  private static final String FILE = "dec03.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1))).toList();
    var length = lines.get(0).length();
    var map = range(0, length).boxed()
        .collect(Collectors.toMap(Function.identity(), index ->
            lines.stream()
                .map(line -> line.charAt(index))
                .filter(c -> c == '1')
                .count())
        );
    var a = new StringBuilder();
    var b = new StringBuilder();
    range(0, length).forEach(k -> {
      if (map.get(k) > lines.size() / 2) {
        a.append('1');
        b.append('0');
      } else {
        a.append('0');
        b.append('1');
      }
    });
    int out = parseInt(a.toString(), 2) * parseInt(b.toString(), 2);
    System.out.println("out: " + out);
  }

}
