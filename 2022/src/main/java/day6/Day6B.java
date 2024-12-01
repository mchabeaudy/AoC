package day6;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Collectors;
import utils.AbstractDay;

public class Day6B extends AbstractDay {

    public Day6B() {
        super("day6");
    }

    public static void main(String[] args) throws Exception {
        new Day6B().solution();
    }

    @Override
    public void solution() throws Exception {
        var line = Files.readString(getPath());
        int k = 13;
        var characters = new Stack<>();
        range(0, 13).mapToObj(line::charAt).forEach(characters::add);
        for (int i = 13; i < line.length(); i++) {
            characters.add(line.charAt(i));
            k++;
            if (new HashSet<>(characters).size()==characters.size()) {
                break;
            } else {
                characters.remove(characters.firstElement());
            }
        }
        System.out.println(k);
    }

}

