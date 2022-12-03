package day3;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day3A {

    private static final Path PATH = Paths.get(getSystemResource("day3.txt").getFile()).toAbsolutePath();

    public static void main(String[] args) throws Exception {
        System.out.println(Files.lines(PATH)
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

    private static int convert(char c) {
        if (Character.isUpperCase(c)) {
            return c - 38;
        }
        return c - 96;
    }

}
