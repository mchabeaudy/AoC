package day07;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Day07A {

  private static final String FILE = "day07.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1))).toList();

    var positions = Arrays.stream(lines.get(0).split(",")).map(Integer::valueOf).toList();

    int min = positions.stream().min(Comparator.naturalOrder()).orElseThrow();
    int max = positions.stream().max(Comparator.naturalOrder()).orElseThrow();

    int fuel = IntStream.range(min, max + 1)
        .map(i -> positions.stream().mapToInt(k -> Math.abs(i - k))
            .sum())
        .min()
        .orElseThrow();

    System.out.println(fuel);
  }


}
