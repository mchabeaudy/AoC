package day1;

import java.nio.file.Files;
import java.util.Collections;
import java.util.Stack;
import utils.AbstractDay;

public class Day1 extends AbstractDay {


    public Day1() {
        super("day1");
    }

    public static void main(String[] args) throws Exception {
        new Day1().solution();
    }

    public void solution() throws Exception {
        var lines = Files.lines(getPath()).toList();
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
