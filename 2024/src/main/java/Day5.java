import static java.util.function.Predicate.not;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day5 extends AbstractDay {

  Map<Integer, Set<Integer>> pages;

  public Day5() {
    super("day5");
  }

  @Override
  void solution() throws Exception {
    pages = new HashMap<>();
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      inputs.stream()
          .filter(line -> line.contains("|"))
          .forEach(line -> addPageOrder(Arrays.stream(line.split("\\|")).map(Integer::parseInt).toList()));

      // part 1
      var sum = inputs.stream()
          .filter(line -> line.contains(","))
          .map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
          .filter(this::isCorrect)
          .mapToInt(list -> list.get(list.size() / 2))
          .sum();
      System.out.println(sum);

      // part 2
      sum = inputs.stream()
          .filter(line -> line.contains(","))
          .map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
          .filter(not(this::isCorrect))
          .map(this::reorder)
          .filter(Objects::nonNull)
          .mapToInt(list -> list.get(list.size() / 2))
          .sum();
      System.out.println(sum);
    }
  }

  private boolean isCorrect(List<Integer> list) {
    return range(0, list.size() - 1).allMatch(
        i -> range(i + 1, list.size()).mapToObj(list::get)
            .anyMatch(pages.getOrDefault(list.get(i), new HashSet<>())::contains) &&
            range(0, i).map(list::get).noneMatch(pages.getOrDefault(list.get(i), new HashSet<>())::contains));
  }

  private List<Integer> reorder(List<Integer> list) {
    var elements = list.stream().filter(i ->
        list.stream()
            .filter(k -> k != i)
            .noneMatch(k -> pages.getOrDefault(k, new HashSet<>()).contains(i))
    ).toList();
    List<List<Integer>> lists = new ArrayList<>(elements.stream()
        .map(i -> {
          var l = new ArrayList<Integer>();
          l.add(i);
          return l;
        }).toList());
    while (lists.stream().noneMatch(l -> l.size() == list.size())) {
      List<List<Integer>> newLists = new ArrayList<>();
      for (var l : lists) {
        var remaining = list.stream().filter(not(l::contains)).toList();
        remaining.stream().filter(i ->
            l.stream()
                .noneMatch(pages.getOrDefault(i, new HashSet<>())::contains)
        ).map(k -> {
          var newList = new ArrayList<>(l);
          newList.add(k);
          return newList;
        }).forEach(newLists::add);
      }
      lists = newLists;
    }
    System.out.println("size : " + lists.size());
    return lists.getFirst();
  }


  private void addPageOrder(List<Integer> list) {
    pages.computeIfAbsent(list.get(0), k -> new HashSet<>()).add(list.get(1));
  }

  public static void main(String[] args) throws Exception {
    new Day5().solution();
  }
}
