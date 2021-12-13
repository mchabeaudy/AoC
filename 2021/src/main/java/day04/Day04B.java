package day04;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Day04B {

  private static final String FILE = "day04.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1))).toList();

    var ints = Arrays.stream(lines.get(0).split((","))).map(Integer::valueOf).toList();

    var boards = range(0, (lines.size() - 1) / 6)
        .mapToObj(i -> {
          var board = new Board();
          board.addRow(lines.get(i * 6 + 2));
          board.addRow(lines.get(i * 6 + 3));
          board.addRow(lines.get(i * 6 + 4));
          board.addRow(lines.get(i * 6 + 5));
          board.addRow(lines.get(i * 6 + 6));
          board.calculateColumns();
          return board;
        })
        .toList();

    AtomicInteger k = new AtomicInteger(0);
    while (boards.stream().filter(Board::hasWon).count() != boards.size() - 1) {
      int kk = k.getAndAdd(1);
      boards.forEach(b -> b.getNumbers().add(ints.get(kk)));
    }
    Board b = boards.stream().filter(Predicate.not(Board::hasWon)).findAny().orElseThrow();
    while (!b.hasWon()) {
      b.getNumbers().add(ints.get(k.getAndAdd(1)));
    }
    System.out.println(b.result());
  }

  static class Board {

    List<List<Integer>> rows = new ArrayList<>();
    List<List<Integer>> columns = new ArrayList<>();
    List<Integer> numbers = new ArrayList<>();

    void calculateColumns() {
      columns = range(0, rows.get(0).size())
          .mapToObj(i -> rows.stream().map(r -> r.get(i)).toList())
          .toList();
    }

    void addRow(String row) {
      rows.add(Arrays.stream(row.replace("  ", " ").split(" ")).filter(s -> s.length() > 0)
          .map(Integer::valueOf)
          .toList());
    }

    boolean hasWon() {
      return rows.stream().anyMatch(row -> numbers.containsAll(row)) || columns.stream()
          .anyMatch(col -> numbers.containsAll(col));
    }

    int result() {
      return rows.stream().flatMap(Collection::stream).filter(i -> !numbers.contains(i))
          .mapToInt(Integer::intValue)
          .sum() * numbers.get(numbers.size() - 1);
    }

    public List<Integer> getNumbers() {
      return numbers;
    }
  }


}
