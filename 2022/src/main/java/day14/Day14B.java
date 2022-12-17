package day14;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.AbstractDay;

public class Day14B extends AbstractDay {


    public Day14B() {
        super("day14");
    }

    public static void main(String[] args) throws Exception {
        new Day14B().solution();
    }

    List<Point> points = new ArrayList<>();
    int nbSand = 0;

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());

        lines.forEach(this::map);
        int ymax = points.stream().mapToInt(p -> p.y).max().orElseThrow();
        range(500 - 3 - ymax, 500 + 3 + ymax).forEach(x -> points.add(new Point(x, ymax + 2)));
        Point p = new Point(500, 0);
        while (true) {
            if (!points.contains(new Point(p.x, p.y + 1))) {
                p = new Point(p.x, p.y + 1);
            } else if (!points.contains(new Point(p.x - 1, p.y + 1))) {
                p = new Point(p.x - 1, p.y + 1);
            } else if (!points.contains(new Point(p.x + 1, p.y + 1))) {
                p = new Point(p.x + 1, p.y + 1);
            } else {
                points.add(p);
                nbSand++;
                if(p.equals(new Point(500,0))){
                    break;
                }
                p = new Point(500, 0);
            }
        }
        System.out.println(nbSand);
    }

    void map(String line) {
        var linesPoints = new ArrayList<Point>();
        Arrays.stream(line.split(" -> "))
                .map(p -> p.split(","))
                .forEach(p -> linesPoints.add(new Point(parseInt(p[0]), parseInt(p[1]))));
        range(0, linesPoints.size() - 1)
                .forEach(i -> {
                    var p0 = linesPoints.get(i);
                    var p1 = linesPoints.get(i + 1);
                    var xmin = min(p0.x, p1.x);
                    var xmax = max(p0.x, p1.x);
                    var ymin = min(p0.y, p1.y);
                    var ymax = max(p0.y, p1.y);
                    range(xmin, xmax + 1).forEach(x -> range(ymin, ymax + 1).forEach(y -> points.add(new Point(x, y))));
                });
    }

    record Point(int x, int y) {

    }

}

