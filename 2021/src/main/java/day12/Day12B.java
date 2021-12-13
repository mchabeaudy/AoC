package day12;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12B {

  public static void main(String[] args) {
    try (var lines = Files.lines(
        Paths.get(getSystemResource("day12.txt").getPath().substring(1)))) {
      var links = lines.map(line -> line.split("-"))
          .map(line -> new CaveLink(line[0], line[1]))
          .toList();

      var paths = links.stream()
          .filter(CaveLink::isStart)
          .map(caveLink -> {
            var deque = new ArrayDeque<String>();
            deque.add("start");
            deque.add("start".equals(caveLink.cave1) ? caveLink.cave2 : caveLink.cave1);
            return new Path(deque);
          })
          .toList();

      boolean process = true;
      while (process) {
        var newPaths = new ArrayList<Path>();
        paths.forEach(path -> {
          if (path.isClosed()) {
            newPaths.add(path);
          } else {
            var lastCave = path.caves.getLast();
            links.stream()
                .filter(link -> link.contains(lastCave))
                .forEach(link -> {
                  var otherCave = link.getOther(lastCave);
                  if (Character.isUpperCase(otherCave.charAt(0)) ||
                      (Character.isLowerCase(otherCave.charAt(0)) &&
                          path.containsLessThan2TimeCave(otherCave))) {
                    var deque = new ArrayDeque<>(path.caves);
                    deque.add(otherCave);
                    newPaths.add(new Path(deque));
                  }
                });
          }
        });

        if (newPaths.stream().mapToInt(p -> p.caves.size()).sum() ==
            paths.stream().mapToInt(p -> p.caves.size()).sum()) {
          process = false;
        }
        paths = newPaths;
      }

      var result = paths.stream().filter(Path::isClosed)
          .count();


      System.out.println("result: " + result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  record CaveLink(String cave1, String cave2) {

    boolean isStart() {
      return "start".equals(cave1) || "start".equals(cave2);
    }

    boolean contains(String cave) {
      return cave.equals(cave1) || cave.contains(cave2);
    }

    String getOther(String cave) {
      return cave1.equals(cave) ? cave2 : cave1;
    }
  }

  record Path(Deque<String> caves) {

    boolean isClosed() {
      return "end".equals(caves.getLast());
    }

    boolean containsLessThan2TimeCave(String cave) {
      if ("start".equals(cave)) {
        return false;
      }
      if (caves.stream()
          .filter(c -> Character.isLowerCase(c.charAt(0)))
          .anyMatch(c -> Collections.frequency(caves, c) == 2 && caves.contains(cave))) {
        return false;
      }
      return caves.stream().filter(cave::equals).count() < 2;
    }

    public String toString() {
      return String.join("-", caves);
    }

  }
}
