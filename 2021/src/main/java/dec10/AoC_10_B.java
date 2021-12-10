package dec10;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.function.Predicate.not;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class AoC_10_B {

  private static final String FILE = "dec10_test.txt";

  private static final List<String> OPENING = List.of("<", "[", "(", "{");

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));

    var out = lines.map(line -> {
          var stack = new Stack<String>();
          for (String s : line.split("")) {
            if (isOpeningChar(s)) {
              stack.push(s);
            } else {
              var o = stack.pop();
              if (!isMatchingChar(o, s)) {
                stack.clear();
                break;
              }
            }
          }
          return stack;
        })
        .filter(not(Collection::isEmpty))
        .map(AoC_10_B::getStackPoints)
        .sorted()
        .toList();
    out.forEach(System.out::println);
    var index = (out.size()-1)/2;
    System.out.println("index: "+index);
    System.out.println("size: "+out.size());

    System.out.println(out.get(index));
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
      case "(" -> out = 1;
      case "[" -> out = 2;
      case "{" -> out = 3;
      case "<" -> out = 4;
    }
    return out;
  }

  static long getStackPoints(Stack<String> s) {
    var out = new AtomicLong(0);
    s.forEach(c -> out.set(out.get() * 5 + getStringPoints(c)));
    return out.get();
  }
}
