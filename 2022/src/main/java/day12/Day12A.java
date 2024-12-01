package day12;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import utils.AbstractDay;

public class Day12A extends AbstractDay {


    Point start;
    Point end;
    char[][] map;
    List<Point> explored = new ArrayList<>();
    int height;
    int length;

    public Day12A() {
        super("day12");
    }

    public static void main(String[] args) throws Exception {
        new Day12A().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        height = lines.size();
        length = lines.get(0).length();
        map = new char[height][length];
        range(0, height).forEach(y ->
                range(0, length).forEach(x -> {
                    map[y][x] = lines.get(y).charAt(x);
                    if (map[y][x] == 'S') {
                        start = new Point(x, y);
                        map[y][x] = 'a';
                    } else if (map[y][x] == 'E') {
                        end = new Point(x, y);
                    }
                }));
        System.out.println(getPathSize(start));
    }

    private int getPathSize(Point p0) {
        int size = 0;
        explored.clear();
        explored.add(p0);
        List<List<Point>> paths = new ArrayList<>();
        var initialPath = new ArrayList<Point>();
        initialPath.add(p0);
        paths.add(initialPath);
        while (true) {
            size++;
            List<List<Point>> newPaths = new ArrayList<>();
            boolean stop = false;
            for (List<Point> path : paths) {
                Point last = path.get(path.size() - 1);
                for (Point p : getAround(last)) {
                    if (map[p.y][p.x] == 'E') {
                        stop = true;
                    }
                    List<Point> newPath = new ArrayList<>(path);
                    newPath.add(p);
                    newPaths.add(newPath);
                }
            }
            paths = newPaths;
            if (stop) {
                break;
            }
        }
        return size;
    }

    List<Point> getAround(Point p) {
        char pc = map[p.y][p.x];
        List<Point> around = new ArrayList<>();
        if (p.dist2(end) <= 1 && (pc == 'z' || pc == 'y')) {
            around.add(end);
        }
        if (p.x > 0 && (!explored.contains(new Point(p.x - 1, p.y))) && map[p.y][p.x - 1] - pc <= 1
                && map[p.y][p.x - 1] != 'E') {
            around.add(new Point(p.x - 1, p.y));
        }
        if (p.y > 0 && (!explored.contains(new Point(p.x, p.y - 1))) && map[p.y - 1][p.x] - pc <= 1
                && map[p.y - 1][p.x] != 'E') {
            around.add(new Point(p.x, p.y - 1));
        }
        if (p.x < length - 1 && (!explored.contains(new Point(p.x + 1, p.y))) && map[p.y][p.x + 1] - pc <= 1
                && map[p.y][p.x + 1] != 'E') {
            around.add(new Point(p.x + 1, p.y));
        }
        if (p.y < height - 1 && (!explored.contains(new Point(p.x, p.y + 1))) && map[p.y + 1][p.x] - pc <= 1
                && map[p.y + 1][p.x] != 'E') {
            around.add(new Point(p.x, p.y + 1));
        }
        explored.addAll(around);
        return around;
    }

    class Point {

        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int dist2(Point p) {
            int dx = p.x - x;
            int dy = p.y - y;
            return dx * dx + dy * dy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }


}

