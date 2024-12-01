package day2;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day2B extends AbstractDay {

    public Day2B() {
        super("day2");
    }

    public static void main(String[] args) throws Exception {
        new Day2B().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.lines(getPath()).toList();

        int s = 0;
        for (String line : lines) {
            char c1 = line.charAt(0);
            char c3 = line.charAt(2);
            char c2 = ' ';
            if (c3 == 'X') {
                if (c1 == 'A') {
                    c2 = 'Z';
                } else if (c1 == 'B') {
                    c2 = 'X';
                } else {
                    c2 = 'Y';
                }
            } else if (c3 == 'Y') {
                if (c1 == 'A') {
                    c2 = 'X';
                } else if (c1 == 'B') {
                    c2 = 'Y';
                } else {
                    c2 = 'Z';
                }
            } else if (c3 == 'Z') {
                if (c1 == 'A') {
                    c2 = 'Y';
                } else if (c1 == 'B') {
                    c2 = 'Z';
                } else {
                    c2 = 'X';
                }
            }
            if (c2 == 'X') {
                s += 1;
            } else if (c2 == 'Y') {
                s += 2;
            } else if (c2 == 'Z') {
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
