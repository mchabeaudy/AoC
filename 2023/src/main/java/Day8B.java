import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day8B extends AbstractDay {

  class Node {

    String name;

    Node right;

    Node left;

    public Node(String name) {
      this.name = name;
    }

  }

  class State {
    int position;

    List<Node> visited = new ArrayList<>();

    public State(int position) {
      this.position = position;
    }
  }

  class Path {
    Node node;

    Map<Integer, State> states;

    boolean looped = false;

    int statesSize;

    List<Integer> zIndexes = new ArrayList<>();

    int loopSize;

    public Path(Node node, int statesSize) {
      this.node = node;
      this.statesSize = statesSize;
      states = range(0, statesSize).boxed().collect(Collectors.toMap(Function.identity(), State::new));
      states.get(0).visited.add(node);
    }

    void move(char c, int index, int k) {
      if (!looped) {
        var state = states.get((index + 1) % statesSize);
        Node next;
        if (c == 'R') {
          next = node.right;
        } else {
          next = node.left;
        }
        if (next.name.endsWith("Z")) {
          zIndexes.add(k + 1);
        }
        if (state.visited.contains(next)) {
          looped = true;
          loopSize = statesSize * state.visited.size();
          System.out.println("pathSize : " + statesSize * state.visited.size());
          System.out.println("zIndexes size : " + zIndexes.size());
          System.out.println(zIndexes.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        }
        state.visited.add(next);
        node = next;
      }
    }

    boolean looped() {
      return looped;
    }
  }

  public Day8B() {
    super("day8");
  }

  public static void main(String[] args) throws Exception {
    new Day8B().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      String instructions = inputs.getFirst();

      Map<String, Node> nodes = range(2, inputs.size()).mapToObj(i -> inputs.get(i).substring(0, 3))
        .collect(Collectors.toMap(Function.identity(), Node::new));
      range(2, inputs.size()).mapToObj(inputs::get).forEach(line -> {
        var name = line.substring(0, 3);
        var left = line.substring(7, 10);
        var right = line.substring(12, 15);
        nodes.get(name).left = nodes.get(left);
        nodes.get(name).right = nodes.get(right);
      });
      int k = 0;
      List<Path> paths = nodes.keySet().stream().filter(key -> key.endsWith("A")).map(nodes::get)
        .map(node -> new Path(node, instructions.length())).toList();
      while (true) {
        int index = k % instructions.length();
        int l = k;
        char c = instructions.charAt(index);
        paths.forEach(p -> p.move(c, index, l));
        if (paths.stream().allMatch(Path::looped)) {
          break;
        }
        k++;
      }
      System.out.println(ppcmMultiple(paths.stream().map(p -> (long) p.zIndexes.get(0)).toList()));
    }
  }

  long pgcd(long a, long b) {
    while (b != 0) {
      long temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  long ppcm(long a, long b) {
    return Math.abs(a * b) / pgcd(a, b);
  }

  long ppcmMultiple(List<Long> numbers) {
    long result = numbers.get(0);
    for (int i = 1; i < numbers.size(); i++) {
      result = ppcm(result, numbers.get(i));
    }
    return result;
  }
}
