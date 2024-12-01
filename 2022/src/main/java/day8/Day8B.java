package day8;

import static java.lang.Math.max;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day8B extends AbstractDay {


    public Day8B() {
        super("day8");
    }

    public static void main(String[] args) throws Exception {
        new Day8B().solution();
    }

    @Override
    public void solution() throws Exception {
        try {
            var lines = Files.readAllLines(getPath());
            int height = lines.size();
            int width = lines.get(0).length();
            int[][] values = new int[height][width];
            range(0, height).forEach(y -> range(0, width).forEach(
                    x -> values[y][x] = Integer.parseInt(String.valueOf(lines.get(y).charAt(x)))));
            int score = 0;
            for (int y0 = 1; y0 < height - 1; y0++) {
                for (int x0 = 1; x0 < width - 1; x0++) {
                    int value = values[y0][x0];
                    int k0 = 1;
                    int x = x0 - 1;
                    int y = y0;
                    while (values[y][x] < value && x > 0) {
                        x--;
                        k0++;
                    }

                    int k1 = 1;
                    x = x0 + 1;
                    y = y0;
                    while (values[y][x] < value && x < width - 1) {
                        x++;
                        k1++;
                    }

                    int k2 = 1;
                    x = x0;
                    y = y0 - 1;
                    while (values[y][x] < value && y > 0) {
                        y--;
                        k2++;
                    }

                    int k3 = 1;
                    x = x0;
                    y = y0 + 1;
                    while (values[y][x] < value && y < height - 1) {
                        y++;
                        k3++;
                    }
                    score = max(score, k0 * k1 * k2 * k3);
                }
            }
            System.out.println(score);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

