package day20;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day20ABis {

  static final Map<Integer, Integer> algo = new HashMap<>();

  public static void main(String[] args) {
    try (var linesStream = Files.lines(
        Paths.get(getSystemResource("day20.txt").getPath().substring(1)))) {
      var lines = linesStream.toList();
      var l = lines.get(0);
      range(0, l.length()).forEach(i -> algo.put(i, pixelValue(l.charAt(i))));

      var size = lines.size() - 2;

      int[][] pixels = new int[size + 1000][size + 1000];

      range(0, size + 1000).forEach(y -> {
        int[] line = new int[size + 1000];
        Arrays.fill(line, 0);
        pixels[y] = line;
      });

      range(0, size).forEach(y -> {
        var line = lines.get(y + 2);
        range(0, size).forEach(x -> pixels[y + 450][x + 450] = pixelValue(line.charAt(x)));
      });


      int[][] expand = pixels;
      for(int k = 0;k<50;k++){
        expand = expand(expand);
      }

      long count = 0;
      for (int x = 100; x < 900; x++) {
        for (int y = 100; y < size + 900; y++) {
          count += expand[y][x];
        }
      }

      System.out.println("count: " + count);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int[][] expand(int[][] pixels) {
    int size = pixels.length;
    int[][] newPixels = new int[size][size];
    range(1, size - 1).forEach(y -> {
      range(1, size - 1).forEach(x -> newPixels[y][x] = newValue(x, y, pixels));
    });
    return newPixels;
  }

  private static int newValue(int x, int y, int[][] table) {
    String s = "" +
        table[y - 1][x - 1] +
        table[y - 1][x] +
        table[y - 1][x + 1] +
        table[y][x - 1] +
        table[y][x] +
        table[y][x + 1] +
        table[y + 1][x - 1] +
        table[y + 1][x] +
        table[y + 1][x + 1];
    return algo.get(Integer.parseInt(s, 2));
  }

  private static int pixelValue(char c) {
    return c == '.' ? 0 : 1;
  }
}
