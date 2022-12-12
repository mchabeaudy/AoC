package day11;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day11A extends AbstractDay {


    public Day11A() {
        super("day11");
    }

    public static void main(String[] args) throws Exception {
        new Day11A().solution();
    }

    @Override
    public void solution() throws Exception {
            var lines = Files.readAllLines(getPath());
            var monkeys = range(0, 8).mapToObj(k -> {
                var monkey = new Monkey();
                monkey.items = new Stack<>();
                Arrays.stream(lines.get(1 + k * 7).replace("  Starting items: ", "")
                        .split(", ")).map(Integer::parseInt).forEach(monkey.items::add);
                var l3 = lines.get(k * 7 + 2).split(" ");
                if ("*".equals(l3[l3.length - 2])) {
                    if ("old".equals(l3[l3.length - 1])) {
                        monkey.operator = i -> i * i;
                    } else {
                        monkey.operator = i -> i * Integer.parseInt(l3[l3.length - 1]);
                    }
                } else {
                    if ("old".equals(l3[l3.length - 1])) {
                        monkey.operator = i -> i + i;
                    } else {
                        monkey.operator = i -> i + Integer.parseInt(l3[l3.length - 1]);
                    }
                }
                var l4 = lines.get(k * 7 + 3).split(" ");
                var l5 = lines.get(k * 7 + 4).split(" ");
                var l6 = lines.get(k * 7 + 5).split(" ");
                monkey.divideTest = Integer.parseInt(l4[l4.length - 1]);
                monkey.trueTestMonkeyId = Integer.parseInt(l5[l5.length - 1]);
                monkey.falseTestMonkeyId = Integer.parseInt(l6[l6.length - 1]);
                return monkey;
            }).collect(Collectors.toList());
            range(0, 20).forEach(k -> monkeys.forEach(m -> m.process(monkeys)));
            monkeys.sort(Comparator.comparingInt(m -> m.count));
            System.out.println(monkeys.get(7).count * monkeys.get(6).count);
    }

    class Monkey {

        UnaryOperator<Integer> operator;
        private int divideTest;
        Stack<Integer> items = new Stack<>();
        int trueTestMonkeyId;
        int falseTestMonkeyId;
        int count = 0;

        public void process(List<Monkey> monkeys) {
            while (!items.isEmpty()) {
                count++;
                int k = items.pop();
                int newInt = operator.apply(k);
                newInt /= 3;
                if (newInt % divideTest == 0) {
                    monkeys.get(trueTestMonkeyId).items.add(newInt);
                } else {
                    monkeys.get(falseTestMonkeyId).items.add(newInt);
                }
            }
        }
    }


}

