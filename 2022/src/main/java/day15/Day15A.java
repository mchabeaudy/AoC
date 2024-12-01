package day15;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import utils.AbstractDay;

public class Day15A extends AbstractDay {

    public Day15A() {
        super("day15");
    }

    public static void main(String[] args) throws Exception {
        new Day15A().solution();
    }

    List<Point> sensors = new ArrayList<>();

    List<Point> beacons = new ArrayList<>();

    List<Circle> circles = new ArrayList<>();

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        lines.forEach(line -> {

            var split = line
                    .replace(",", "")
                    .replace(":", "")
                    .replace("x=", "")
                    .replace("y=", "")
                    .split(" ");
            var sensor = new Point(parseInt(split[2]), parseInt(split[3]));
            var beacon = new Point(parseInt(split[8]), parseInt(split[9]));
            sensors.add(sensor);
            beacons.add(beacon);
            circles.add(new Circle(sensor, sensor.dist(beacon)));
        });
        int lineY = 2000000;
        int xMin = circles.stream().mapToInt(c -> c.centre.x - c.r).min().getAsInt();
        int xMax = circles.stream().mapToInt(c -> c.centre.x + c.r).max().getAsInt();
        var count = range(xMin, xMax + 1)
                .mapToObj(x -> new Point(x, lineY))
                .filter(p -> !sensors.contains(p) && !beacons.contains(p) && circles.stream()
                        .anyMatch(c -> c.contains(p)))
                .count();
        System.out.println(count);
    }

    record Point(int x, int y) {

        int dist(Point p) {
            return abs(x - p.x) + abs(y - p.y);
        }
    }

    record Circle(Point centre, int r) {

        boolean contains(Point p) {
            return centre.dist(p) <= r;
        }
    }

}

