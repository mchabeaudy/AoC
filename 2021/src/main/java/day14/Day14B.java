package day14;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Day14B {

  public static void main(String[] args) {
    try (var lines = Files.lines(
        Paths.get(getSystemResource("day14.txt").getPath().substring(1)))) {
      var in = lines.toList();
      var sequence = in.get(0);
      var insertions = range(2, in.size())
          .mapToObj(in::get)
          .map(s -> new Insertion(Pair.of(s.charAt(0), s.charAt(1)), s.charAt(6)))
          .toList();

      Map<Pair, Long> m = new HashMap<>();
      range(1, sequence.length())
          .mapToObj(i -> Pair.of(sequence.charAt(i - 1), sequence.charAt(i)))
          .forEach(p -> m.put(p, m.getOrDefault(p, 0L) + 1));

      Map<Pair, Long> map = m;
      for (int k = 0; k < 40; k++) {
        Map<Pair, Long> newMap = new HashMap<>();
        map.forEach((key, value) -> insertions.stream()
            .filter(i -> i.p.equals(key))
            .findAny()
            .ifPresentOrElse(i -> {
                  newMap.put(i.pa(), newMap.getOrDefault(i.pa(), 0L) + value);
                  newMap.put(i.pb(), newMap.getOrDefault(i.pb(), 0L) + value);
                },
                () -> newMap.put(key, newMap.getOrDefault(key, 0L) + 1))
        );
        map = newMap;
      }

      Map<Character, Long> countMap = new HashMap<>();
      map.forEach((key, value) -> {
        countMap.put(key.a, countMap.getOrDefault(key.a, 0L) + value);
        countMap.put(key.b, countMap.getOrDefault(key.b, 0L) + value);
      });

      countMap.forEach((key, value) -> System.out.println("key: " + key + "   value: " + value));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  record Pair(char a, char b) {

    static Pair of(char a, char b) {
      return new Pair(a, b);
    }
  }

  record Insertion(Pair p, char c) {

    Pair pa() {
      return Pair.of(p.a, c);
    }

    Pair pb() {
      return Pair.of(c, p.b);
    }
  }

}