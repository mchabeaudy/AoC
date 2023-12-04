import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public class Day4 extends AbstractDay {

    static Map<Integer, Long> map = new HashMap<>();
    static List<Card> cards;

    record Card(int id, List<Integer> winning, List<Integer> numbers) {
        int value() {
            int matching = winningNumberCount();
            if (matching == 0) {
                return 0;
            }
            return (int) Math.pow(2, matching - 1);
        }

        private int winningNumberCount() {
            return (int) numbers.stream().filter(winning::contains).count();
        }

        public long count() {
            if (map.containsKey(id)) {
                return map.get(id);
            }
            int n = Math.min(cards.size(), id + winningNumberCount());
            var count = 1 + range(id, n).mapToObj(cards::get).mapToLong(Card::count).sum();
            map.put(id, count);
            return count;
        }
    }

    public Day4() {
        super("day4");
    }

    public static void main(String[] args) throws Exception {
        new Day4().solution();
    }

    @Override
    public void solution() throws Exception {
        try (var lines = Files.lines(getPath())) {
            cards = lines.map(this::fromString).toList();
            System.out.println(cards.stream().mapToInt(Card::value).sum());
            long sum = range(0, cards.size()).mapToObj(k -> cards.get(cards.size() - k - 1))
                    .mapToLong(Card::count).sum();
            System.out.println(sum);
        }
    }

    private Card fromString(String line) {
        int k = line.indexOf(':');
        int id = parseInt(line.substring(4, line.indexOf(":")).trim());
        List<Integer> winning = new ArrayList<>();
        while (!line.substring(k + 1, k + 4).contains("|")) {
            winning.add(parseInt(line.substring(k + 1, k + 4).trim()));
            k += 3;
        }
        k = line.indexOf('|') + 1;
        List<Integer> numbers = new ArrayList<>();
        while (k + 3 <= line.length()) {
            if (k + 4 >= line.length()) {
                numbers.add(parseInt(line.substring(k + 1).trim()));
            } else {
                numbers.add(parseInt(line.substring(k + 1, k + 4).trim()));
            }
            k += 3;
        }
        return new Card(id, winning, numbers);
    }

}
