package day3;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day3B {

    private static final Path PATH = Paths.get(getSystemResource("day3.txt").getFile()).toAbsolutePath();

    public static void main(String[] args) throws Exception {
        var lines = Files.lines(PATH).toList();

        System.out.println(range(0, lines.size() / 3)
                .map(i -> {
                    var l1 = lines.get(i * 3);
                    var l2 = lines.get(i * 3 + 1);
                    var l3 = lines.get(i * 3 + 2);
                    return Arrays.stream(l1.split(""))
                            .filter(s -> l2.contains(s) && l3.contains(s))
                            .findAny()
                            .map(s -> convert(s.charAt(0)))
                            .get();
                }).sum());
    }

    private static int convert(char c) {
        if (Character.isUpperCase(c)) {
            return c - 38;
        }
        return c - 96;
    }

}
