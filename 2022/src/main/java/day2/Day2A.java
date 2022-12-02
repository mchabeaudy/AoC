package day2;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day2A {

    private static final Path PATH = Paths.get(getSystemResource("day2.txt").getFile()).toAbsolutePath();

    public static void main(String[] args) throws Exception {
        var lines = Files.lines(PATH).toList();

        int s = 0;
        for (String line : lines) {
            char c1 = line.charAt(0);
            char c2 = line.charAt(2);
            if (c2 == 'X') {
                s += 1;
            } else if (c2 == 'Y') {
                s += 2;
            } else {
                s += 3;
            }
            if (c1 == 'A') {
                if (c2 == 'X') {
                    s += 3;
                } else if (c2 == 'Y') {
                    s += 6;
                } else {
                    s += 0;
                }
            } else if (c1 == 'B') {
                if (c2 == 'X') {
                    s += 0;
                } else if (c2 == 'Y') {
                    s += 3;
                } else {
                    s += 6;
                }
            } else if (c1 == 'C') {
                if (c2 == 'X') {
                    s += 6;
                } else if (c2 == 'Y') {
                    s += 0;
                } else {
                    s += 3;
                }
            }
        }
        System.out.println(s);
    }

}
