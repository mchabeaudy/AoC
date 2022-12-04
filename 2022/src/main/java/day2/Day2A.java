package day2;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day2A extends AbstractDay {

    public Day2A() {
        super("day2");
    }

    public static void main(String[] args) throws Exception {
        new Day2A().solution();
    }

    public void solution() throws Exception {
        var lines = Files.lines(getPath()).toList();

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
