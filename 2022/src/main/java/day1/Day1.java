package day1;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Stack;

public class Day1 {

    private static final Path PATH = Paths.get(getSystemResource("day1.txt").getFile()).toAbsolutePath();

    public static void main(String[] args) throws Exception {
        var lines = Files.lines(PATH).toList();
        var cals = new Stack<Integer>();
        int c = 0;
        for (String line : lines) {
            if ("".equals(line)) {
                cals.add(c);
                c = 0;
            } else {
                c += Integer.parseInt(line);
            }
        }
        cals.add(c);
        Collections.sort(cals);
        // Part 1
        System.out.println(cals.lastElement());
        // Part 2
        System.out.println(cals.pop() + cals.pop() + cals.pop());
    }

}
