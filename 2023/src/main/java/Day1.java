import java.nio.file.Files;
import java.util.Map;
import java.util.Set;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;

public class Day1 extends AbstractDay {


    private static final Set<String> KEYS = Set.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    private static final Map<String, Character> NUMBERS = Map.of(
            "one", '1',
            "two", '2',
            "three", '3',
            "four", '4',
            "five", '5',
            "six", '6',
            "seven", '7',
            "eight", '8',
            "nine", '9'
    );

    public Day1() {
        super("day1");
    }

    public static void main(String[] args) throws Exception {
        new Day1().solution();
    }

    public void solution() throws Exception {
        try (var lines = Files.lines(getPath())) {
            var list = lines.toList();
            int s1 = list.stream().mapToInt(line -> parseInt("" + find(line) + findReverse(line))).sum();
            int s2 = list.stream().mapToInt(line -> parseInt("" + find2(line) + findReverse2(line))).sum();
            System.out.println(s1);
            System.out.println(s2);
        }
    }

    private char find(String line) {
        return find(line, false);
    }

    private char findReverse(String line) {
        return find(line, true);
    }

    private char find(String line, boolean reverse) {
        int k = 0;
        char c = line.charAt(reverse ? line.length() - k - 1 : k);
        while (!isDigit(c)){
            k++;
            c = line.charAt(reverse ? line.length() - k - 1 : k);
        }
        return c;
    }

    private char find2(String line) {
        return find2(line, false);
    }

    private char findReverse2(String line) {
        return find2(line, true);
    }


    private char find2(String line, boolean reverse) {
        int k = 0;
        while (true) {
            var index = reverse ? line.length() - k - 1 : k;
            char c = line.charAt(index);
            if (isDigit(c)) {
                return c;
            }
            if (reverse) {
                var l = line.substring(0, line.length() - k);
                var key = KEYS.stream().filter(l::endsWith).findAny();
                if (key.isPresent()) {
                    return NUMBERS.get(key.get());
                }
            } else {
                var l = line.substring(k);
                var key = KEYS.stream().filter(l::startsWith).findAny();
                if (key.isPresent()) {
                    return NUMBERS.get(key.get());
                }
            }
            k++;
        }
    }

}
