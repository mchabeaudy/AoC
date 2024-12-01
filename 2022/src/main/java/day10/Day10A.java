package day10;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day10A extends AbstractDay {

    int clock = 0;
    int register = 1;
    int sum = 0;

    public Day10A() {
        super("day10");
    }

    public static void main(String[] args) throws Exception {
        new Day10A().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        for (String line : lines) {
            if (line.equals("noop")) {
                clock();
            } else {
                int toAdd = Integer.parseInt(line.substring(5));
                clock();
                clock();
                register += toAdd;
            }
        }
        System.out.println(sum);
    }

    private void clock() {
        clock++;
        if ((clock+20) % 40 == 0) {
            sum += clock * register;
        }
    }


}

