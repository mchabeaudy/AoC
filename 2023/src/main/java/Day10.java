import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day10 extends AbstractDay {

  class Tile {
    int x;

    int y;

    char c;

    List<Tile> neighbors = new ArrayList<>();

    public Tile(int x, int y, char c) {
      this.x = x;
      this.y = y;
      this.c = c;
    }

    boolean north() {
      return switch (c) {
        case '|', 'J', 'L' -> true;
        default -> false;
      };
    }

    boolean south() {
      return switch (c) {
        case '|', '7', 'F' -> true;
        default -> false;
      };
    }

    boolean west() {
      return switch (c) {
        case '-', 'J', '7' -> true;
        default -> false;
      };
    }

    boolean east() {
      return switch (c) {
        case '-', 'L', 'F' -> true;
        default -> false;
      };
    }

    int dist(Tile tile) {
      return Math.abs(x - tile.x) + Math.abs(y - tile.y);
    }

    boolean isConnected(Tile tile) {
      boolean southConnected = isSouthTo(tile) && (isS() || north()) && tile.south();
      boolean northConnected = isNorthTo(tile) && (isS() || south()) && tile.north();
      boolean westConnected = isWestTo(tile) && (isS() || east()) && tile.west();
      boolean eastConnected = isEastTo(tile) && (isS() || west()) && tile.east();
      return southConnected || northConnected || westConnected || eastConnected;
    }

    boolean isS() {
      return 'S' == c;
    }

    boolean isNorthTo(Tile tile) {
      return tile.y - y == 1;
    }

    boolean isSouthTo(Tile tile) {
      return y - tile.y == 1;
    }

    boolean isWestTo(Tile tile) {
      return tile.x - x == 1;
    }

    boolean isEastTo(Tile tile) {
      return x - tile.x == 1;
    }

    public boolean isInside(Set<Tile> loop) {
      var walls = loop.stream().filter(t -> t.x < x && t.y == y).sorted(Comparator.comparingInt(t -> t.x)).toList();
      List<List<Tile>> list = new ArrayList<>();
      walls.forEach(w -> {
        if (list.isEmpty()) {
          List<Tile> initial = new ArrayList<>();
          initial.add(w);
          list.add(initial);
        } else {
          var last = list.getLast();
          if (belongToSameGroup(w, last)) {
            last.add(w);
          } else {
            List<Tile> newList = new ArrayList<>();
            newList.add(w);
            list.add(newList);
          }
        }
      });
      boolean b = list.stream().mapToInt(this::evalList).sum() % 2 == 1;
      if (b) {
        System.out.println("x:" + x + ",y:" + y);
      }
      return list.stream().mapToInt(this::evalList).sum() % 2 == 1;
    }

    private boolean belongToSameGroup(Tile w, List<Tile> last) {
      var lastTile = last.getLast();
      if (last.size() > 1 && lastTile.isEndTile()) {
        return false;
      }
      return lastTile.dist(w) == 1 && (w.isEndTile()||w.c=='-');
    }

    boolean isEndTile() {
      return c == '7' || c == '|' || c == 'J';
    }

    int evalList(List<Tile> tiles) {
      if(tiles.size()==1){
        return 1;
      }
      if ((tiles.getFirst().goesUp() && tiles.getLast().goesUp()) || (tiles.getFirst().goesDown() && tiles.getLast()
        .goesDown())) {
        return 2;
      }
      return 1;
    }

    private boolean goesUp() {
      return c == 'L' || c == 'J' || c == '|';
    }

    private boolean goesDown() {
      return c == '7' || c == 'F' || c == '|';
    }

    @Override
    public String toString() {
      return "Tile{" + "x=" + x + ", y=" + y + ", c=" + c + '}';
    }
  }

  public Day10() {
    super("day10");
  }

  public static void main(String[] args) throws Exception {
    new Day10().solution();
  }

  @Override
  public void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var inputs = lines.toList();
      int width = inputs.getFirst().length();
      int height = inputs.size();
      var tiles = range(0, height).mapToObj(
          y -> range(0, width).mapToObj(x -> new Tile(x, y, inputs.get(y).charAt(x))).toList())
        .flatMap(Collection::stream).toList();
      tiles.forEach(tile -> tile.neighbors = tiles.stream().filter(t -> t.dist(tile) == 1).toList());

      var sTile = tiles.stream().filter(Tile::isS).findAny().orElseThrow();
      int k = 0;
      Set<Tile> loop = new HashSet<>();
      loop.add(sTile);
      Tile tile = sTile;
      while (Objects.nonNull(tile)) {
        Tile nextTile = null;
        for (var neighbor : tile.neighbors) {
          if (!loop.contains(neighbor) && tile.isConnected(neighbor)) {
            nextTile = neighbor;
            loop.add(nextTile);
            break;
          }
        }
        tile = nextTile;
        k++;
      }
      System.out.println("part 1: " + (k / 2 + k % 2));
      sTile.c = '7';
      System.out.println(tiles.stream().filter(t -> !loop.contains(t) && t.isInside(loop)).count());
    }
  }

}
