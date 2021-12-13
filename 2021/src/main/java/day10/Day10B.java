package day10;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Day10B {

    private static final String FILE = "day10.txt";

    private static final List<String> OPENING = List.of("<", "[", "(", "{");
    private static final Map<String, Integer> POINTS = Map.of("(", 1, "[", 2, "{", 3, "<", 4);


    public static void main(String[] args) {
        try (var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)))) {
            var out = lines.map(Day10B::getIncompleteLine)
                    .filter(Iterator::hasNext)
                    .map(Day10B::getPoints)
                    .sorted()
                    .toList();
            System.out.println(out.get((out.size() - 1) / 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Iterator<String> getIncompleteLine(String line) {
        var deque = new ArrayDeque<String>();
        for (String s : line.split("")) {
            if (isOpeningChar(s)) {
                deque.push(s);
            } else {
                var o = deque.pop();
                if (!isMatchingChar(o, s)) {
                    deque.clear();
                    break;
                }
            }
        }
        return deque.iterator();
    }

    static boolean isOpeningChar(String s) {
        return OPENING.contains(s);
    }

    static boolean isMatchingChar(String o, String c) {
        return switch (o) {
            case "<" -> ">".equals(c);
            case "{" -> "}".equals(c);
            case "(" -> ")".equals(c);
            case "[" -> "]".equals(c);
            default -> false;
        };
    }


    static long getPoints(Iterator<String> s) {
        long out = 0;
        while (s.hasNext()) {
            out = out * 5 + POINTS.get(s.next());
        }
        return out;
    }
}
