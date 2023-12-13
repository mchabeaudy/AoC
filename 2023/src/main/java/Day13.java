import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 extends AbstractDay {

  record Index(int value, boolean vertical) {
  }

  class Valley {
    List<String> lines;

    Index index;

    public Valley(List<String> lines) {
      this.lines = lines;
    }

    public Valley(List<String> lines, Index index) {
      this.lines = lines;
      this.index = index;
    }

    int val() {
      return vertical() + horizontal();
    }

    int vertical() {
      int width = lines.getFirst().length();
      int reflexionIndex = range(1, width).filter(
        i -> (index == null || !index.equals(new Index(i, true))) && lines.stream()
          .allMatch(line -> hasReflexion(line, i))).findAny().orElse(0);
      if (reflexionIndex == 0) {
        return 0;
      }
      var newIndex = new Index(reflexionIndex, true);
      if (newIndex.equals(index)) {
        return 0;
      }
      if (index == null) {
        index = newIndex;
      }
      return reflexionIndex;
    }

    boolean hasReflexion(String s, int index) {
      int length = Math.min(index, s.length() - index);
      return range(0, length).allMatch(i -> s.charAt(index - i - 1) == s.charAt(index + i));
    }

    int horizontal() {
      var verticalLines = verticalLines();
      int width = verticalLines.getFirst().length();
      int reflexionIndex = range(1, width).filter(
        i -> (index == null || !index.equals(new Index(i, false))) && verticalLines.stream()
          .allMatch(line -> hasReflexion(line, i))).findAny().orElse(0);
      if (reflexionIndex == 0) {
        return 0;
      }
      var newIndex = new Index(reflexionIndex, false);
      if (newIndex.equals(index)) {
        return 0;
      }
      if (index == null) {
        index = newIndex;
      }
      return 100 * reflexionIndex;
    }

    List<String> verticalLines() {
      int width = lines.getFirst().length();
      return range(0, width).mapToObj(
        i -> lines.stream().map(line -> String.valueOf(line.charAt(i))).collect(Collectors.joining())).toList();
    }

    public int val2() {
      int x = 0;
      int y = 0;
      int width = lines.getFirst().length();
      var newValley = new Valley(rebuildLines(0, 0, width), index);
      int val = newValley.val();
      while (val == 0) {
        if (x == width - 1) {
          x = 0;
          y++;
        } else {
          x++;
        }
        newValley = new Valley(rebuildLines(x, y, width), index);
        val = newValley.val();
      }
      return val;
    }


    List<String> rebuildLines(int x, int y, int width) {
      var newList = new ArrayList<String>();
      for (int k = 0; k < lines.size(); k++) {
        var line = lines.get(k);
        String newLine;
        if (k == y) {
          if (x == 0) {
            newLine = "" + opposite(line.charAt(0)) + line.substring(1);
          } else if (x == width - 1) {
            newLine = line.substring(0, width - 1) + opposite(line.charAt(width - 1));
          } else {
            newLine = line.substring(0, x) + opposite(line.charAt(x)) + line.substring(x + 1);
          }
        } else {
          newLine = line;
        }
        newList.add(newLine);
      }
      return newList;
    }

    char opposite(char c) {
      return c == '.' ? '#' : '.';
    }
  }

  public Day13() {
    super("day13");
  }

  public static void main(String[] args) throws Exception {
    new Day13().solution();
  }

  @Override
  public void solution() throws Exception {
    var allLines = Files.readString(getPath());
    var split = allLines.split(lineSeparator() + lineSeparator());
    var valleys = stream(split).map(s -> stream(s.split(lineSeparator())).toList()).map(Valley::new).toList();
    System.out.println(valleys.stream().mapToInt(Valley::val).sum());
    System.out.println(valleys.stream().mapToInt(Valley::val2).sum());
  }

}
