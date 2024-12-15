import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Day14 extends AbstractDay {

  static final int WIDTH = 101;
  static final int HEIGHT = 103;

  public Day14() {
    super("day14");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();

      var robots = input.stream()
          .map(l -> new Robot(
              Arrays.stream(l.replace("p=", "").replace(" v=", ",").split(",")).map(Integer::parseInt).toList()))
          .toList();

      long count = 0;
      int nMax = (int) (0.45 * robots.size());
      while (true) {
        int k = 0;
        robots = robots.stream().map(Robot::next).toList();
        while (k < robots.size()*0.55) {
          var remaining = new ArrayList<>(robots);
          var r = remaining.get(k);
          remaining.remove(r);
          var tree = new ArrayList<Robot>();
          tree.add(r);
          while (tree.size() < nMax) {
            var op = remaining.stream().filter(rr -> rr.isInTree(tree)).findAny();
            if (op.isPresent()) {
              var toAdd = op.get();
              tree.add(toAdd);
              remaining.remove(toAdd);
            } else {
              break;
            }
          }
          if (tree.size() == nMax) {
            System.out.println("win: " + count);
            String l = "";
            for (int y=0; y<HEIGHT; y++) {
              l="";
              for (int x=0; x<WIDTH; x++) {
                int xf = x;
                int yf = y;
                l += robots.stream().anyMatch(rr->rr.x==xf&&rr.y==yf)?"X":".";
              }
              System.out.println(l);
            }
            break;
          }
          k++;
        }
        count++;
        if (count % 500 == 0) {
          System.out.println(count);
        }
      }
    }

  }

  public static void main(String[] args) throws Exception {
    new Day14().solution();
  }

  record Robot(int x, int y, int vx, int vy) {

    public Robot(List<Integer> list) {
      this(list.get(0), list.get(1), list.get(2), list.get(3));
    }

    Robot next() {
      return new Robot((x + vx + WIDTH) % WIDTH, (y + vy + HEIGHT) % HEIGHT, vx, vy);
    }

    boolean isInTree(Collection<Robot> robots) {
      return robots.stream().anyMatch(r -> r.dist2(this) <= 4 );
    }

    int dist2(Robot robot) {
      int dx = robot.x - x;
      int dy = robot.y - y;
      return dx * dx + dy * dy;
    }

    double dist(Robot robot) {
      return Math.sqrt(dist2(robot));
    }
  }

}
