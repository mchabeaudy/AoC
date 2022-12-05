package day5;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day5B extends AbstractDay {

    public Day5B() {
        super("day5");
    }

    public static void main(String[] args) throws Exception {
        new Day5B().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.lines(getPath()).toList();
        var stacks = range(0, 9).mapToObj(i -> new Stack<Character>()).toList();
        range(0, 8).forEach(i ->
                range(0, 9).forEach(j -> {
                    var c = lines.get(7 - i).charAt(4 * j + 1);
                    if (' ' != c) {
                        stacks.get(j).add(c);
                    }
                })
        );
        range(10, lines.size())
                .mapToObj(i -> formatLine(lines.get(i)))
                .forEach(ints ->
                {
                    var s = new Stack<Character>();
                    range(0, ints[0]).forEach(k -> s.add(stacks.get(ints[1] - 1).pop()));
                    range(0, ints[0]).forEach(k -> stacks.get(ints[2]-1).add(s.pop()));
                });
        System.out.println(
                stacks.stream().map(s -> String.valueOf(s.pop())).collect(Collectors.joining()));
    }

    private int[] formatLine(String line) {
        return Arrays.stream(line.replace("move ", "")
                        .replace("from ", "")
                        .replace(" to", "")
                        .split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

}

