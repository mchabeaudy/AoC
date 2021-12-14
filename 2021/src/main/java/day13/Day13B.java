package day13;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13B {

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
          .toList();

      Set<Point> set = new HashSet<>(points);
      for (Point fold : folds) {
        set = set.stream()
            .map(p -> fold.x == 0 ? p.foldY(fold.y) : p.foldX(fold.x))
            .collect(Collectors.toSet());
      }
      for (int y = 0; y < 6; y++) {
        var line = new StringBuilder();
        for (int x = 0; x < 40; x++) {
          if(set.contains(new Point(x,y))){
            line.append('#');
          }else{
            line.append('.');
          }
        }
        System.out.println(line);
      }
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