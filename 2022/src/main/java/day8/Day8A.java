package day8;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day8A extends AbstractDay {


    public Day8A() {
        super("day8");
    }

    public static void main(String[] args) throws Exception {
        new Day8A().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        int height = lines.size();
        int width = lines.get(0).length();
        int[][] values = new int[height][width];
        range(0, height).forEach(y -> range(0, width).forEach(
                x -> values[y][x] = Integer.parseInt(String.valueOf(lines.get(y).charAt(x)))));
        int sum = 2 * width + 2 * height - 4;
        for (int y0 = 1; y0 < height - 1; y0++) {
            for (int x0 = 1; x0 < width - 1; x0++) {
                int value = values[y0][x0];
                int yval = y0;
                int xval = x0;
                var b0 = range(0, x0).allMatch(x -> values[yval][x] < value);
                var b1 = range(x0 + 1, width).allMatch(x -> values[yval][x] < value);
                var b2 = range(0, y0).allMatch(y -> values[y][xval] < value);
                var b3 = range(y0 + 1, height).allMatch(y -> values[y][xval] < value);
                if (b0 || b1 || b2 || b3) {
                    sum++;
                }
            }
        }
        System.out.println(sum);
    }


}

