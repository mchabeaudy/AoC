import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day1 extends AbstractDay {

    public Day1() {
        super("day1");
    }

    public static void main(String[] args) throws Exception {
        new Day1().solution();
    }

    void solution() throws Exception {
        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        try (var lines = Files.lines(getPath())) {
            lines.forEach(line -> {
                var s = line.split(" {3}");
                left.add(Integer.parseInt(s[0]));
                right.add(Integer.parseInt(s[1]));
            });
        }
        Collections.sort(left);
        Collections.sort(right);

        // part 1
        System.out.println(
            IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum());

        // part 2
        System.out.println(
            IntStream.range(0, left.size()).map(i -> left.get(i) * count(left.get(i), right))
                .sum());
    }

    private int count(int integer, List<Integer> right) {
        return (int) right.stream().filter(i -> i == integer).count();
    }
}
