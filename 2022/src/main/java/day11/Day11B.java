package day11;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day11B extends AbstractDay {


    public Day11B() {
        super("day11");
    }

    public static void main(String[] args) throws Exception {
        new Day11B().solution();
    }

    @Override
    public void solution() throws Exception {
        int nmonkey = 8;
        var lines = Files.readAllLines(getPath());
        var monkeys = range(0, nmonkey).mapToObj(k -> {
            var monkey = new Monkey();
            monkey.items = new Stack<>();
            Arrays.stream(lines.get(1 + k * 7).replace("  Starting items: ", "")
                    .split(", ")).map(Integer::parseInt).map(Item::new).forEach(monkey.items::add);
            var l3 = lines.get(k * 7 + 2).split(" ");
            if ("*".equals(l3[l3.length - 2])) {
                if ("old".equals(l3[l3.length - 1])) {
                    monkey.operator = new Operation('*', true, 0);
                } else {
                    monkey.operator = new Operation('*', false, Integer.parseInt(l3[l3.length - 1]));
                }
            } else {
                if ("old".equals(l3[l3.length - 1])) {
                    monkey.operator = new Operation('+', true, 0);
                } else {
                    monkey.operator = new Operation('+', false, Integer.parseInt(l3[l3.length - 1]));
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
        monkeys.stream().map(m -> m.items).flatMap(Collection::stream)
                .forEach(item ->
                        monkeys.forEach(m -> item.addDivisor(m.divideTest)));

        range(0, 10000).forEach(k -> monkeys.forEach(m -> m.process(monkeys)));
        monkeys.sort(Comparator.comparingLong(m -> m.count));
        System.out.println(monkeys.get(nmonkey - 1).count * monkeys.get(nmonkey - 2).count);
    }

    class Monkey {

        Operation operator;
        private int divideTest;
        Stack<Item> items = new Stack<>();
        int trueTestMonkeyId;
        int falseTestMonkeyId;
        long count = 0;

        public void process(List<Monkey> monkeys) {
            while (!items.isEmpty()) {
                count++;
                Item i = items.pop();
                i.apply(operator);
                if (i.getRest(divideTest) == 0) {
                    monkeys.get(trueTestMonkeyId).items.add(i);
                } else {
                    monkeys.get(falseTestMonkeyId).items.add(i);
                }
            }
        }
    }

    class Item {

        Map<Integer, DivisorRest> divisorRests;
        int initial;

        public Item(int initial) {
            this.initial = initial;
            divisorRests = new HashMap<>();
        }

        void addDivisor(int divisor) {
            divisorRests.put(divisor, new DivisorRest(divisor, initial));
        }

        void apply(Operation o) {
            divisorRests.values().forEach(d -> {
                if (o.type == '*') {
                    if (o.itSelf) {
                        d.multiply(d.rest);
                    } else {
                        d.multiply(o.value);
                    }
                } else {
                    if (o.itSelf) {
                        d.add(d.rest);
                    } else {
                        d.add(o.value);
                    }
                }
            });
        }

        int getRest(int divisor) {
            return divisorRests.get(divisor).rest;
        }
    }

    class DivisorRest {

        int divisor;
        int rest;

        public DivisorRest(int divisor, int initial) {
            this.divisor = divisor;
            this.rest = initial % divisor;
        }

        void multiply(int m) {
            rest = (rest * m) % divisor;
        }

        void add(int a) {
            rest = (rest + a) % divisor;
        }
    }

    class Operation {

        char type;
        boolean itSelf;
        int value;

        public Operation(char type, boolean itSelf, int value) {
            this.type = type;
            this.itSelf = itSelf;
            this.value = value;
        }
    }
}

