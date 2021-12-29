package day22;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Day22A {

  public static void main(String[] args) throws IOException {
    try {

      var lines = Files.lines(Paths.get(getSystemResource("day22Test.txt").getPath()));
      var instructions = lines.map(input -> {
        var split0 = input.split(" ");
        var split1 = split0[1].split(",");
        var xRange = split1[0].substring(split1[0].indexOf('=') + 1);
        var xMin = parseInt(xRange.substring(0, xRange.indexOf('.')));
        var xMax = parseInt(xRange.substring(xRange.lastIndexOf('.') + 1));
        var yRange = split1[1].substring(split1[1].indexOf('=') + 1);
        var yMin = parseInt(yRange.substring(0, yRange.indexOf('.')));
        var yMax = parseInt(yRange.substring(yRange.lastIndexOf('.') + 1));
        var zRange = split1[2].substring(split1[2].indexOf('=') + 1);
        var zMin = parseInt(zRange.substring(0, zRange.indexOf('.')));
        var zMax = parseInt(zRange.substring(zRange.lastIndexOf('.') + 1));
        return new Instruction(split0[0].equals("on"), xMin, xMax, yMin, yMax, zMin, zMax);
      }).toList();


      Map<Integer, Map<Integer, Set<Integer>>> points = new HashMap<>();
      instructions.forEach(i -> {
        var l = new AtomicLong();
        points.forEach((k, v) -> v.forEach((kk, vv) -> l.addAndGet(vv.size())));
        System.out.println("size: " + l.get());
        if (i.on) {
          range(Math.max(-50, i.xMin), Math.min(i.xMax + 1, 51)).forEach(x -> {
            if (!points.containsKey(x)) {
              points.put(x, new HashMap<>());
            }
            var xMap = points.get(x);
            range(Math.max(-50, i.yMin), Math.min(51, i.yMax + 1)).forEach(y -> {
              if (!xMap.containsKey(y)) {
                xMap.put(y, new HashSet<>());
              }
              var ySet = xMap.get(y);
              range(Math.max(-50, i.zMin), Math.min(51, i.zMax + 1)).forEach(ySet::add);
            });
          });
        } else {
          range(Math.max(-50, i.xMin), Math.min(i.xMax + 1, 51)).forEach(x -> {
            if (points.containsKey(x)) {
              var xMap = points.get(x);
              range(Math.max(-50, i.yMin), Math.min(51, i.yMax + 1)).forEach(y -> {
                if (xMap.containsKey(y)) {
                  var ySet = xMap.get(y);
                  range(Math.max(-50, i.zMin), Math.min(51, i.zMax + 1))
                      .forEach(z -> ySet.removeIf(n -> n == z));
                }
              });
            }
          });
        }
      });
      AtomicLong l = new AtomicLong();
      points.forEach((k, v) -> v.forEach((kk, vv) -> l.addAndGet(vv.size())));
      System.out.println("size: " + l.get());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  record Instruction(boolean on, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {

  }

}
