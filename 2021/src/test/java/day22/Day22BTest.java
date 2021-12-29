package day22;

import static org.junit.jupiter.api.Assertions.assertEquals;

import day22.Day22B.Cube;
import day22.Day22B.Instruction;
import java.util.List;
import org.junit.jupiter.api.Test;

class Day22BTest {

  @Test
  void test1() {
    var i1 = new Instruction(true, 0, 10, 0, 10, 0, 10);
    var i2 = new Instruction(true, 8, 18, 8, 18, 8, 18);
    var day22 = new Day22B();

    var cubes = day22.buildCubes(List.of(i1, i2));
    var vol = cubes.stream().mapToLong(Cube::getVolume).sum();
    assertEquals(1992L, vol);
  }

}
