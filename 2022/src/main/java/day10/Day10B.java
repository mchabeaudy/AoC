package day10;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day10B extends AbstractDay {

    int clock = 0;
    int register = 1;
    String line;
    List<String> lines = new ArrayList<>();

    public Day10B() {
        super("day10");
    }

    public static void main(String[] args) throws Exception {
        new Day10B().solution();
    }

    @Override
    public void solution() throws Exception {
        var fileLines = Files.readAllLines(getPath());
        for (String l : fileLines) {
            if (l.equals("noop")) {
                clock();
            } else {
                int toAdd = Integer.parseInt(l.substring(5));
                clock();
                clock();
                register += toAdd;
            }
        }
        System.out.println(lines.stream().collect(Collectors.joining(System.lineSeparator())));
    }

    private void clock() {
        if (clock % 40 == 0) {
            line = "";
        }
        clock++;
        int k = clock % 40;
        if (k >= register && k <= register + 2) {
            line += "#";
        } else {
            line += ".";
        }
        if (clock % 40 == 39) {
            lines.add(line);
        }
    }


}

