import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day9 extends AbstractDay {

  class Sequence {
    List<Integer> initialSequence;

    public Sequence(String input) {
      initialSequence = new ArrayList<>(stream(input.split(" ")).map(Integer::parseInt).toList());
    }

    int nextValue(boolean reverse) {
      List<List<Integer>> sequences = new ArrayList<>();
      sequences.add(initialSequence);
      while (!sequences.getLast().stream().allMatch(i -> i == 0)) {
        var lastSeq = sequences.getLast();
        var newSeq = new ArrayList<>(
          range(0, lastSeq.size() - 1).map(i -> lastSeq.get(i + 1) - lastSeq.get(i)).boxed().toList());
        sequences.add(newSeq);
      }
      if (reverse) {
        sequences.forEach(Collections::reverse);
      }
      while (sequences.size() > 1) {
        var last = sequences.getLast();
        sequences.remove(last);
        var newLast = sequences.getLast();
        var toAdd = reverse ? (-last.getLast()) : last.getLast();
        newLast.add(newLast.getLast() + toAdd);
      }
      return sequences.getFirst().getLast();
    }

  }

  public Day9() {
    super("day9");
  }

  public static void main(String[] args) throws Exception {
    new Day9().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      List<Sequence> sequences = lines.map(Sequence::new).toList();
      System.out.println(sequences.stream().mapToInt(s -> s.nextValue(false)).sum());
      System.out.println(sequences.stream().mapToInt(s -> s.nextValue(true)).sum());
    }
  }

}
