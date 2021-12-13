package day10;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Day10A {

  private static final String FILE = "day10.txt";

  private static final List<String> OPENING = List.of("<", "[", "(", "{");

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));

    var out = lines.map(line -> {
          String corrupted = null;
          var stack = new Stack<String>();
          for (String s : line.split("")) {
            if (isOpeningChar(s)) {
              stack.push(s);
            } else {
              var o = stack.pop();
              if (!isMatchingChar(o, s)) {
                corrupted = s;
                break;
              }
            }
          }
          return corrupted;
        })
        .filter(Objects::nonNull)
        .mapToInt(Day10A::getStringPoints)
        .sum();

    System.out.println(out);
  }

  static boolean isOpeningChar(String s) {
    return OPENING.contains(s);
  }

  static boolean isMatchingChar(String o, String c) {
    boolean out = false;
    switch (o) {
      case "<" -> out = ">".equals(c);
      case "{" -> out = "}".equals(c);
      case "(" -> out = ")".equals(c);
      case "[" -> out = "]".equals(c);
    }
    return out;
  }

  static int getStringPoints(String s) {
    int out = 0;
    switch (s) {
      case ">" -> out = 25137;
      case "}" -> out = 1197;
      case ")" -> out = 3;
      case "]" -> out = 57;
    }
    return out;
  }
}
