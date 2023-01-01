package day15;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day15B extends AbstractDay {

    public Day15B() {
        super("day15");
    }

    public static void main(String[] args) throws Exception {
        new Day15B().solution();
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
        int bx = 0;
        int by = 0;
        for (int y = 0; y <= 4000000; y++) {
            List<Bounds> boundsList = new ArrayList<>();
            boundsList.add(new Bounds(0, 4000000));
            for (Bounds bounds : getCircleBounds(y)) {
                boundsList = boundsList.stream()
                        .map(b -> b.remove(bounds))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            }
            if (!boundsList.isEmpty()) {
                by = y;
                bx = boundsList.get(0).x0;
                break;
            }
        }
        System.out.println(4000000L * bx + by);
    }

    List<Bounds> getCircleBounds(int y) {
        return circles.stream()
                .map(c -> c.getBounds(y))
                .filter(Objects::nonNull)
                .toList();
    }

    record Point(int x, int y) {

        int dist(Point p) {
            return abs(x - p.x) + abs(y - p.y);
        }
    }

    record Circle(Point c, int r) {

        Bounds getBounds(int y) {
            int dy = abs(c.y - y);
            if (dy <= r) {
                return new Bounds(c.x - r + dy, c.x + r - dy);
            }
            return null;
        }
    }

    record Bounds(int x0, int x1) {

        List<Bounds> remove(Bounds b) {
            List<Bounds> boundsList = new ArrayList<>();
            if (b.x1 < x0) {
                return List.of(new Bounds(x0, x1));
            }
            if (b.x0 <= x0) {
                if (b.x1 >= x1) {
                    return Collections.emptyList();
                }
                boundsList.add(new Bounds(b.x1 + 1, x1));
            } else if (b.x0 <= x1) {
                boundsList.add(new Bounds(x0, b.x0 - 1));
                if (b.x1 < x1) {
                    boundsList.add(new Bounds(b.x1 + 1, x1));
                }
            } else {
                boundsList.add(new Bounds(x0, x1));
            }
            return boundsList;
        }
    }

}

