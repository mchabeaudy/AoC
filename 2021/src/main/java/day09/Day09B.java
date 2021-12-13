package day09;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day09B {

    private static final String FILE = "day09.txt";

    public static void main(String[] args) throws Exception {
        var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath())).toList();

        var points = range(0, lines.size())
                .mapToObj(y -> range(0, lines.get(0).length())
                        .mapToObj(x -> new Point(x, y, charToInt(lines.get(y).charAt(x))))
                        .toList())
                .flatMap(Collection::stream)
                .toList();

        var lowPoints = points.stream()
                .filter(p -> p.isLowPoint(points))
                .toList();

        List<Set<Point>> basins = new ArrayList<>();
        lowPoints.stream()
                .map(p -> p.basin(points))
                .forEach(basin -> {
                            if (basins.stream()
                                    .noneMatch(
                                            b -> b.stream().anyMatch(bp -> basin.stream().anyMatch(bp::hasSameCoordinate)))) {
                                basins.add(basin);
                            }
                        }
                );

        basins.sort(Comparator.comparingInt(Collection::size));

        var m = basins.get(basins.size() - 1).size()
                * basins.get(basins.size() - 2).size()
                * basins.get(basins.size() - 3).size();
        System.out.println(m);
    }

    record Point(int x, int y, int value) {

        List<Point> closePoints(List<Point> points) {
            return points.stream()
                    .filter(p -> dist(p) == 1)
                    .toList();
        }

        int dist(Point p) {
            return Math.abs(p.x - x) + Math.abs(p.y - y);
        }

        boolean isLowPoint(List<Point> points) {
            return closePoints(points).stream()
                    .allMatch(p -> p.value > value);
        }

        Set<Point> basin(List<Point> allPoints) {
            Set<Point> basin = new HashSet<>();
            basin.add(this);
            boolean process = true;
            List<Point> previouslyAdded = new ArrayList<>(basin);
            while (process) {
                var newToAdd = allPoints.stream()
                        .filter(p -> p.value != 9 &&
                                basin.stream().noneMatch(p::hasSameCoordinate) &&
                                previouslyAdded.stream().anyMatch(pa -> pa.dist(p) == 1))
                        .toList();
                previouslyAdded.clear();
                previouslyAdded.addAll(newToAdd);
                basin.addAll(newToAdd);
                if (newToAdd.isEmpty()) {
                    process = false;
                }
            }
            return basin;
        }

        boolean hasSameCoordinate(Point p) {
            return p.x == x && p.y == y;
        }

    }


    static int charToInt(char c) {
        return Integer.parseInt(String.valueOf(c));
    }
}
