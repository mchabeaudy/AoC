package day25;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class Day25A {

  static boolean process = true;

  public static void main(String[] args) throws IOException {
    try {

      var lines = Files.lines(Paths.get(getSystemResource("day25.txt").getPath())).toList();
      int width = lines.get(0).length();
      int height = lines.size();
      String[][] cucumbers = new String[height][width];
      range(0, height).forEach(y -> {
        var line = lines.get(y).split("");
        range(0, width).forEach(x -> cucumbers[y][x] = line[x]);
      });

      int k = 0;
      var input = cucumbers;
      while (process) {
        var jj = input;
        print(width, height, jj);
        k++;
        input = increase(input);
       // System.out.println("k :" + k);
      }

      System.out.println("k :" + k);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void print(int width, int height, String[][] jj) {
    /*System.out.println();
    range(0, height).forEach(y -> {
      range(0, width).forEach(x -> System.out.print(jj[y][x]));
      System.out.println();
    });
    System.out.println();*/
  }

  private static String[][] increase(String[][] input) {
    var stopProcess = new AtomicBoolean(true);
    int width = input[0].length;
    int height = input.length;

    var output = new String[height][width];
    range(0, height).forEach(y -> range(0, width).forEach(x -> output[y][x] = "x"));

    range(0, height).forEach(y -> {
      range(0, width - 1).forEach(x -> {
        if (output[y][x].equals("x")) {
          var value = input[y][x];
          var nextValue = input[y][x + 1];
          if (value.equals(">") && nextValue.equals(".")) {
            output[y][x] = ".";
            output[y][x + 1] = ">";
            stopProcess.set(false);
          } else {
            output[y][x] = input[y][x];
          }
        }
      });
      if (output[y][width - 1].equals("x")) {
        if (input[y][width - 1].equals(">") && input[y][0].equals(".")) {
          output[y][width - 1] = ".";
          output[y][0] = ">";
          stopProcess.set(false);
        } else {
          output[y][width - 1] = input[y][width - 1];
        }
      }
    });

    var output1 = new String[height][width];
    range(0, height).forEach(y -> range(0, width).forEach(x -> output1[y][x] = "x"));

    range(0, width).forEach(x -> {
      range(0, height - 1).forEach(y -> {
        if (output1[y][x].equals("x")) {
          var value = output[y][x];
          var nextValue = output[y + 1][x];
          if (value.equals("v") && nextValue.equals(".")) {
            output1[y][x] = ".";
            output1[y + 1][x] = "v";
            stopProcess.set(false);
          } else {
            output1[y][x] = output[y][x];
          }
        }
      });
      if (output1[height - 1][x].equals("x")) {
        if (output[height - 1][x].equals("v")
            && output[0][x].equals(".")) {
          output1[height - 1][x] = ".";
          output1[0][x] = "v";
          stopProcess.set(false);
        } else {
          output1[height - 1][x] = output[height - 1][x];
        }
      }
    });

    process = !stopProcess.get();
    return output1;
  }


}
