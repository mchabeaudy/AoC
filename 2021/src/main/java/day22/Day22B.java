package day22;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Long.parseLong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day22B {

  public static void main(String[] args) throws IOException {
    var lines = Files.lines(Paths.get(getSystemResource("day22.txt").getPath()));
    var instructions = lines.map(input -> {
      var split0 = input.split(" ");
      var split1 = split0[1].split(",");
      var xRange = split1[0].substring(split1[0].indexOf('=') + 1);
      var xMin = parseLong(xRange.substring(0, xRange.indexOf('.')));
      var xMax = parseLong(xRange.substring(xRange.lastIndexOf('.') + 1));
      var yRange = split1[1].substring(split1[1].indexOf('=') + 1);
      var yMin = parseLong(yRange.substring(0, yRange.indexOf('.')));
      var yMax = parseLong(yRange.substring(yRange.lastIndexOf('.') + 1));
      var zRange = split1[2].substring(split1[2].indexOf('=') + 1);
      var zMin = parseLong(zRange.substring(0, zRange.indexOf('.')));
      var zMax = parseLong(zRange.substring(zRange.lastIndexOf('.') + 1));
      return new Instruction(split0[0].equals("on"), xMin, xMax + 1, yMin, yMax + 1, zMin,
          zMax + 1);
    }).toList();

    var day22 = new Day22B();
    var cubes = day22.buildCubes(instructions);

    var result = cubes.stream().mapToLong(Cube::getVolume).sum();
    System.out.println("result: " + result);
  }

  public List<Cube> buildCubes(List<Instruction> instructions) {
    List<Cube> cubes = new ArrayList<>();
    instructions.forEach(i -> processInstruction(cubes, i));
    return cubes;
  }

  private void processInstruction(List<Cube> cubes, Instruction instruction) {
    var cube = instruction.getCube();
    if (instruction.on()) {
      if (cubes.stream().anyMatch(c -> c.contains(cube))) {
        return;
      }
      if (cubes.stream().noneMatch(cube::intersect)) {
        cubes.add(cube);
        return;
      }
      var intersections = cubes.stream().filter(cube::intersect).toList();
      cubes.removeAll(intersections);
      intersections.forEach(i -> cubes.addAll(i.remove(cube)));
      cubes.add(cube);
    } else {
      cubes.removeIf(cube::contains);
      var intersections = cubes.stream().filter(cube::intersect).toList();
      cubes.removeAll(intersections);
      intersections.forEach(c -> cubes.addAll(c.remove(cube)));
    }
  }

  record Instruction(boolean on, long xMin, long xMax, long yMin, long yMax, long zMin, long zMax) {

    Cube getCube() {
      return new Cube(xMin, xMax, yMin, yMax, zMin, zMax);
    }
  }

  record Cube(long xMin, long xMax, long yMin, long yMax, long zMin, long zMax) {

    boolean intersect(Cube cube) {
      return ((xMin < cube.xMax && xMin >= cube.xMin) || (xMax <= cube.xMax && xMax > cube.xMin)
          || (xMin < cube.xMin && xMax > cube.xMax)) &&
          ((yMin < cube.yMax && yMin >= cube.yMin) || (yMax <= cube.yMax && yMax > cube.yMin)
              || (yMin < cube.yMin && yMax > cube.yMax)) &&
          ((zMin < cube.zMax && zMin >= cube.zMin) || (zMax <= cube.zMax && zMax > cube.zMin)
              || (zMin < cube.zMin && zMax > cube.zMax));

    }

    boolean contains(Cube cube) {
      return xMin <= cube.xMin && xMax >= cube.xMax && yMin <= cube.yMin && yMax >= cube.yMax
          && zMin <= cube.zMin && zMax >= cube.zMax;
    }

    long getVolume() {
      return (xMax - xMin) * (yMax - yMin) * (zMax - zMin);
    }

    Set<Cube> remove(Cube cube) {
      Set<Cube> cubes = new HashSet<>();
      if (cube.xMin > xMin) {
        cubes.add(new Cube(xMin, cube.xMin, yMin, yMax, zMin, zMax));
      }
      if (cube.xMax < xMax) {
        cubes.add(new Cube(cube.xMax, xMax, yMin, yMax, zMin, zMax));
      }
      if (cube.yMin > yMin) {
        cubes.add(new Cube(
            Math.max(xMin, cube.xMin), Math.min(cube.xMax, xMax), yMin, cube.yMin, zMin, zMax));
      }
      if (cube.yMax < yMax) {
        cubes.add(new Cube(
            Math.max(xMin, cube.xMin), Math.min(cube.xMax, xMax), cube.yMax, yMax, zMin, zMax));
      }
      if (cube.zMin > zMin) {
        cubes.add(new Cube(
            Math.max(xMin, cube.xMin), Math.min(cube.xMax, xMax),
            Math.max(yMin, cube.yMin), Math.min(cube.yMax, yMax),
            zMin, cube.zMin
        ));
      }
      if (cube.zMax < zMax) {
        cubes.add(new Cube(
            Math.max(xMin, cube.xMin), Math.min(cube.xMax, xMax),
            Math.max(yMin, cube.yMin), Math.min(cube.yMax, yMax),
            cube.zMax, zMax
        ));
      }
      return cubes;
    }
  }

}
