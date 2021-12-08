package dec03;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AoC_03_B {

  private static final String FILE = "dec03.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1))).toList();
    int index = 0;
    List<String> list = new ArrayList<>(lines);
    while (list.size() > 1) {
      int i = index;
      long ones = list.stream().filter(n -> n.charAt(i) == '1').count();
      if (ones >= list.size() / 2) {
        list = list.stream().filter(n -> n.charAt(i) == '1').toList();
      } else {
        list = list.stream().filter(n -> n.charAt(i) == '0').toList();
      }
      index++;
    }
    var a = list.get(0);
    // least
    index = 0;
    list = new ArrayList<>(lines);
    while (list.size() > 1) {
      int i = index;
      long zeros = list.stream().filter(n -> n.charAt(i) == '0').count();
      if (zeros <= list.size() / 2) {
        list = list.stream().filter(n -> n.charAt(i) == '0').toList();
      } else {
        list = list.stream().filter(n -> n.charAt(i) == '1').toList();
      }
      index++;
    }
    var b = list.get(0);
    int out = parseInt(a, 2) * parseInt(b, 2);
    System.out.println("out: " + out);
  }

}
