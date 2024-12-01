import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day8 extends AbstractDay {

  class Node {

    String name;

    Node right;

    Node left;

    boolean endNode;

    public Node(String name) {
      this.name = name;
      endNode = name.endsWith("Z");
    }

  }

  class Position {
    Node node;

    public Position(Node node) {
      this.node = node;
    }

    void move(char c) {
      if (c == 'L') {
        node = node.left;
      } else {
        node = node.right;
      }
    }

    boolean isEndPosition() {
      return node.endNode;
    }

  }

  public Day8() {
    super("day8");
  }

  public static void main(String[] args) throws Exception {
    new Day8().solution();
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
      Node position = nodes.get("AAA");
      long k = 0;
      while (!position.name.equals("ZZZ")) {
        char c = instructions.charAt((int) (k % instructions.length()));
        if (c == 'L') {
          position = position.left;
        } else {
          position = position.right;
        }
        k++;
      }
      System.out.println(k);
    }
  }

}
