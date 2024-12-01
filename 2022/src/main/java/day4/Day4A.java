package day4;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day4A extends AbstractDay {

    public Day4A() {
        super("day4");
    }

    public static void main(String[] args) throws Exception {
        new Day4A().solution();
    }

    @Override
    public void solution() throws Exception {
        System.out.println(Files.lines(getPath())
                .mapToInt(line -> {
                    var s = line.split(",");
                    var i = s[0].split("-");
                    var j = s[1].split("-");
                    int i0 = Integer.parseInt(i[0]);
                    int i1 = Integer.parseInt(i[1]);
                    int j0 = Integer.parseInt(j[0]);
                    int j1 = Integer.parseInt(j[1]);
                    return include(i0, i1, j0, j1) ? 1 : 0;
                })
                .sum());
    }

    public boolean include(int i0, int i1, int j0, int j1) {
        return (i0 <= j0 && i1 >= j1) || (i0 >= j0 && i1 <= j1);
    }
}

