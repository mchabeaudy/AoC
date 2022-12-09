package day9;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import utils.AbstractDay;

public class Day9A extends AbstractDay {

    private Set<Point> visited = new HashSet<>();

    public Day9A() {
        super("day9");
    }

    public static void main(String[] args) throws Exception {
        new Day9A().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());

        var head = new Point();
        var tail = new Point();
        visited.add(new Point());
        lines.forEach(line -> {
            var dir = line.charAt(0);
            var moves = Integer.parseInt(line.substring(2));
            range(0, moves).forEach(i -> {
                switch (dir) {
                    case 'U' -> head.x++;
                    case 'D' -> head.x--;
                    case 'L' -> head.y--;
                    case 'R' -> head.y++;
                }
                tail.moveTo(head);
                visited.add(new Point(tail));
            });
        });
        System.out.println(visited.size());
    }


}

