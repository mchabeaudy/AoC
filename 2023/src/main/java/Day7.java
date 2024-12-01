import static java.lang.Long.parseLong;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day7 extends AbstractDay {

  static List<Character> CARDS_ORDER_1 = new ArrayList<>();
  static List<Character> CARDS_ORDER_2 = new ArrayList<>();

  static {
    CARDS_ORDER_1.add('2');
    CARDS_ORDER_1.add('3');
    CARDS_ORDER_1.add('4');
    CARDS_ORDER_1.add('5');
    CARDS_ORDER_1.add('6');
    CARDS_ORDER_1.add('7');
    CARDS_ORDER_1.add('8');
    CARDS_ORDER_1.add('9');
    CARDS_ORDER_1.add('T');
    CARDS_ORDER_1.add('J');
    CARDS_ORDER_1.add('Q');
    CARDS_ORDER_1.add('K');
    CARDS_ORDER_1.add('A');
    CARDS_ORDER_2.add('J');
    CARDS_ORDER_2.add('2');
    CARDS_ORDER_2.add('3');
    CARDS_ORDER_2.add('4');
    CARDS_ORDER_2.add('5');
    CARDS_ORDER_2.add('6');
    CARDS_ORDER_2.add('7');
    CARDS_ORDER_2.add('8');
    CARDS_ORDER_2.add('9');
    CARDS_ORDER_2.add('T');
    CARDS_ORDER_2.add('Q');
    CARDS_ORDER_2.add('K');
    CARDS_ORDER_2.add('A');
  }

  enum HandType implements Comparable<HandType> {
    CARD,
    PAIR,
    TWO_PAIR,
    THREE,
    FULL,
    FOUR,
    FIVE;

    int diff(HandType handType) {
      return ordinal() - handType.ordinal();
    }
  }

  record Hand(String handCards, HandType type, long bid, boolean first) implements Comparable<Hand> {
    @Override
    public int compareTo(Hand hand) {
      int typeDiff = type.diff(hand.type);
      if (typeDiff != 0){
        return typeDiff;
      }
      int k = 0;
      while (handCards.charAt(k) == hand.handCards.charAt(k)){
        k++;
      }
      var list = first ? CARDS_ORDER_1 : CARDS_ORDER_2;
      return list.indexOf(handCards.charAt(k)) - list.indexOf(hand.handCards.charAt(k));
    }
  }

  public Day7() {
    super("day7");
  }

  public static void main(String[] args) throws Exception {
    new Day7().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      var hands1 = input.stream().map(line -> line.split(" "))
        .map(inputs -> new Hand(inputs[0], getHandType1(inputs[0]), parseLong(inputs[1]), true)).sorted().toList();
      long total = range(0, hands1.size()).mapToLong(i -> (i + 1) * hands1.get(i).bid).sum();
      System.out.println(total);

      var hands2 = input.stream().map(line -> line.split(" "))
        .map(inputs -> new Hand(inputs[0], getHandType2(inputs[0]), parseLong(inputs[1]), false)).sorted().toList();
      long total2 = range(0, hands2.size()).mapToLong(i -> (i + 1) * hands2.get(i).bid).sum();
      System.out.println(total2);
    }
  }

  HandType getHandType1(String hand) {
    Map<Character, Integer> occurrences = new HashMap<>();
    range(0, hand.length()).mapToObj(hand::charAt).forEach(c -> occurrences.put(c, occurrences.getOrDefault(c, 0) + 1));
    var values = occurrences.values();
    var keySet = occurrences.keySet();
    if (keySet.size() == 1)
      return HandType.FIVE;
    if (values.stream().anyMatch(v -> v == 4))
      return HandType.FOUR;
    if (keySet.size() == 2)
      return HandType.FULL;
    if (values.stream().anyMatch(v -> v == 3))
      return HandType.THREE;
    if (values.stream().filter(v -> v == 2).count() == 2)
      return HandType.TWO_PAIR;
    if (values.stream().anyMatch(v -> v == 2))
      return HandType.PAIR;
    return HandType.CARD;
  }

  HandType getHandType2(String hand) {
    Map<Character, Integer> occurrences = new HashMap<>();
    range(0, hand.length()).mapToObj(hand::charAt).forEach(c -> occurrences.put(c, occurrences.getOrDefault(c, 0) + 1));
    int jokers = occurrences.getOrDefault('J', 0);
    occurrences.remove('J');
    var values = occurrences.values();
    var keySet = occurrences.keySet();
    return switch (jokers) {
      case 4, 5 -> HandType.FIVE;
      case 3 -> 1 == keySet.size() ? HandType.FIVE : HandType.FOUR;
      case 2 -> getHandTypeWith2Jokers(keySet);
      case 1 -> getHandTypeWith1Jokers(keySet, values);
      default -> getHandType1(hand);
    };
  }

  HandType getHandTypeWith2Jokers(Set<Character> keyset) {
    return switch (keyset.size()) {
      case 1 -> HandType.FIVE;
      case 2 -> HandType.FOUR;
      case 3 -> HandType.THREE;
      default -> throw new IllegalArgumentException();
    };
  }

  HandType getHandTypeWith1Jokers(Set<Character> keyset, Collection<Integer> values) {
    return switch (keyset.size()) {
      case 1 -> HandType.FIVE;
      case 2 -> values.stream().anyMatch(v -> v == 3) ? HandType.FOUR : HandType.FULL;
      case 3 -> HandType.THREE;
      case 4 -> HandType.PAIR;
      default -> throw new IllegalArgumentException();
    };
  }

}
