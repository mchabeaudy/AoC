package day13;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Day13A {

  public static void main(String[] args) {
    try (var lines = Files.lines(
        Paths.get(getSystemResource("day13.txt").getPath().substring(1)))) {

      var in = lines.toList();
      var points = in.stream().filter(l -> !(l.length() == 0) && !l.startsWith("fold"))
          .map(l -> l.split(","))
          .map(l -> new Point(parseInt(l[0]), parseInt(l[1])))
          .toList();

      var folds = in.stream().filter(l -> l.startsWith("f"))
          .map(l -> l.charAt(11) == 'x' ? new Point(parseInt(l.substring(13)), 0)
              : new Point(0, parseInt(l.substring(13))))
          .peek(System.out::println)
          .toList();

      var fold0 = folds.get(0);
      var set = points.stream()
          .map(p -> fold0.x == 0 ? p.foldY(fold0.y) : p.foldX(fold0.x))
          .collect(Collectors.toSet());

      System.out.println(set.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  record Point(int x, int y) {

    Point foldX(int foldX) {
      if (foldX >= x) {
        return this;
      }
      return new Point(2 * foldX - x, y);
    }

    Point foldY(int foldY) {
      if (foldY >= y) {
        return this;
      }
      return new Point(x, 2 * foldY - y);
    }

    @Override
    public String toString() {
      return "Point{" +
          "x=" + x +
          ", y=" + y +
          '}';
    }
  }
}