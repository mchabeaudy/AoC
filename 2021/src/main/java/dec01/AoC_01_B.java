package dec01;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;

public class AoC_01_B {

  private static final String FILE = "dec01.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));
    var measures = lines.map(Integer::valueOf).toList();
    var count = range(3, measures.size())
        .filter(index -> measures.get(index) > measures.get(index - 3))
        .count();
    System.out.println(count);
  }


}
