package day19;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class Day19A {

  static List<Function<Point, Point>> rotations = new ArrayList<>();
  static List<Scanner> scanners = new ArrayList<>();

  public static void main(String[] args) {
    try (var linesStream = Files.lines(Paths.get(getSystemResource("day19.txt").getPath()))) {
      var lines = linesStream.toList();
      initScanners(lines);
      initRotations();
      mapScanners();

      System.out.println("size: " + scanners.get(0).points.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void mapScanners() {
    Scanner s0 = scanners.get(0);
    s0.setMapped(true);
    while (scanners.stream().anyMatch(s -> !s.isMapped())) {

      Scanner s = null;
      Map<Point, Point> matchingPoints = null;
      for (var scanner : scanners) {
        if (!scanner.isMapped()) {
          var mPoints = s0.overlap(scanner);
          if (!mPoints.isEmpty()) {
            s = scanner;
            matchingPoints = mPoints;
            break;
          }
        }
      }
      Function<Point, Point> rotation = null;
      Point vector = null;
      for (var rot : rotations) {
        boolean goodRot = false;
        Long d = null;
        for (var entry : matchingPoints.entrySet()) {
          var rotated = rot.apply(entry.getValue());
          var dist = entry.getKey().dist2(rotated);
          if (Objects.equals(d, dist)) {
            rotation = rot;
            goodRot = true;
            int dx = entry.getKey().x() - rotated.x();
            int dy = entry.getKey().y() - rotated.y();
            int dz = entry.getKey().z() - rotated.z();
            vector = new Point(dx, dy, dz);
            break;
          }
          d = dist;
        }
        if (goodRot) {
          break;
        }
      }

      var goodVector = vector;
      s.setMapped(true);
      s.points.stream()
          .map(rotation)
          .map(p -> p.translate(goodVector))
          .forEach(s0::add);
    }
  }

  private static void initScanners(List<String> lines) {
    scanners = new ArrayList<>();
    var scanner = new Scanner();
    for (var line : lines) {
      if (line.startsWith("--- scanner")) {
        scanner = new Scanner();
        scanners.add(scanner);
      } else if (line.length() != 0) {
        scanner.add(new Point(line.split(",")));
      }
    }
  }

  static class Scanner {

    boolean mapped = false;
    Set<Point> points = new HashSet<>();

    void add(Point p) {
      points.add(p);
    }

    public boolean isMapped() {
      return mapped;
    }

    public void setMapped(boolean mapped) {
      this.mapped = mapped;
    }

    public Map<Point, Point> overlap(Scanner s) {
      var ownDistMap = points.stream()
          .collect(toMap(identity(), p -> points.stream().map(p::dist2).toList()));
      var sDistMap = s.points.stream()
          .collect(toMap(identity(), p -> s.points.stream().map(p::dist2).toList()));
      Map<Point, Point> matchingPair = new HashMap<>();
      ownDistMap.forEach((ownPoint, ownDist) -> {
        sDistMap.forEach((sPoint, sDist) -> {
          if (ownDist.stream().filter(sDist::contains).count() >= 12) {
            matchingPair.put(ownPoint, sPoint);
          }
        });
      });
      return matchingPair;
    }

  }

  static void initRotations() {
    rotations.add(p -> new Point(p.x(), p.y(), p.z()));
    rotations.add(p -> new Point(p.y(), p.z(), p.x()));
    rotations.add(p -> new Point(p.z(), p.x(), p.y()));

    rotations.add(p -> new Point(p.y(), -p.x(), p.z()));
    rotations.add(p -> new Point(p.z(), p.y(), -p.x()));
    rotations.add(p -> new Point(-p.x(), p.z(), p.y()));

    rotations.add(p -> new Point(-p.x(), -p.y(), p.z()));
    rotations.add(p -> new Point(p.z(), -p.x(), -p.y()));
    rotations.add(p -> new Point(-p.y(), p.z(), -p.x()));

    rotations.add(p -> new Point(-p.y(), p.x(), p.z()));
    rotations.add(p -> new Point(p.z(), -p.y(), p.x()));
    rotations.add(p -> new Point(p.x(), p.z(), -p.y()));

    rotations.add(p -> new Point(p.y(), p.x(), -p.z()));
    rotations.add(p -> new Point(-p.z(), p.y(), p.x()));
    rotations.add(p -> new Point(p.x(), -p.z(), p.y()));

    rotations.add(p -> new Point(-p.x(), p.y(), -p.z()));
    rotations.add(p -> new Point(-p.z(), -p.x(), p.y()));
    rotations.add(p -> new Point(p.y(), -p.z(), -p.x()));

    rotations.add(p -> new Point(-p.y(), -p.x(), -p.z()));
    rotations.add(p -> new Point(-p.z(), -p.y(), -p.x()));
    rotations.add(p -> new Point(-p.x(), -p.z(), -p.y()));

    rotations.add(p -> new Point(p.x(), -p.y(), -p.z()));
    rotations.add(p -> new Point(-p.z(), p.x(), -p.y()));
    rotations.add(p -> new Point(-p.y(), -p.z(), p.x()));
  }
}
