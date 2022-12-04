package day3;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import utils.AbstractDay;

public class Day3A extends AbstractDay {

    private static final Path PATH = Paths.get(getSystemResource("day3.txt").getFile()).toAbsolutePath();

    public Day3A() {
        super("day3");
    }

    public static void main(String[] args) throws Exception {
        new Day3A().solution();
    }

    @Override
    public void solution() throws Exception {
        System.out.println(Files.lines(getPath())
                .mapToInt(line -> {
                    var m = line.length() / 2;
                    var s1 = line.substring(0, m);
                    var s2 = line.substring(m);
                    return Arrays.stream(s1.split(""))
                            .filter(s2::contains)
                            .findAny()
                            .map(s -> convert(s.charAt(0)))
                            .get();
                }).sum());

    }

    private int convert(char c) {
        if (Character.isUpperCase(c)) {
            return c - 38;
        }
        return c - 96;
    }
}
