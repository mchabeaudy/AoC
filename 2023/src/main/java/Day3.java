import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public class Day3 extends AbstractDay {

    record Number(int num, int x, int y, int width) {
        boolean isPart() {
            var surround = getSurround(this);
            var isPart = surround.stream().anyMatch(c -> !isDigit(c) && c != '.');
            return isPart;
        }

        public boolean dontInclude(int x, int y) {
            return !(this.y == y && x >= this.x && x < this.x + width);
        }

        @Override
        public String toString() {
            return ""+num+" x:"+x+", y:"+y+", part:"+isPart();
        }

        public boolean closeTo(int starX, int starY) {
            return starY>=y-1 && starY<= y+1 && starX>=x-1 && starX<=x+width;
        }
    }

    static char[][] grid;
    static int width;
    static int height;

    static List<Character> getSurround(Number n) {
        List<Character> chars = new ArrayList<>();
        var xMin = Math.max(0, n.x - 1);
        var yMin = Math.max(0, n.y - 1);
        var xMax = Math.min(width - 1, n.x + 1 + n.width);
        var yMax = Math.min(height - 1, n.y + 1);
        range(xMin, xMax)
                .forEach(x -> range(yMin, yMax + 1)
                        .filter(y -> n.dontInclude(x, y))
                        .forEach(y -> chars.add(grid[y][x])));
        return chars;
    }

    List<Number> numbers = new ArrayList<>();

    public Day3() {
        super("day3");
    }

    public static void main(String[] args) throws Exception {
        new Day3().solution();
    }

    @Override
    public void solution() throws Exception {
        try (var lines = Files.lines(getPath())) {
            var list = lines.toList();
            height = list.size();
            width = list.get(0).length();
            grid = new char[height][width];
            range(0, height).forEach(y -> range(0, width).forEach(x -> grid[y][x] = list.get(y).charAt(x)));
            for (int y = 0; y < height; y++) {
                String number = "";
                for (int x = 0; x < width; x++) {
                    char c = grid[y][x];
                    boolean isDigit = isDigit(c);
                    if (isDigit) {
                        number += c;
                        if (x == width - 1 || (x < width - 1 && !isDigit(grid[y][x + 1]))) {
                            int l = number.length();
                            numbers.add(new Number(parseInt(number), x - l + 1, y, l));
                            number = "";
                        }
                    }
                }
            }
            System.out.println(numbers.stream().filter(Number::isPart).mapToInt(Number::num).sum());
            int gearSum = 0;
            for(int y=0; y<height;y++){
                for(int x=0;x<width;x++){
                    if(grid[y][x]=='*'){
                        int starX = x;
                        int starY = y;
                        var closeNumbers = numbers.stream().filter(n->n.closeTo(starX, starY)).toList();
                        if(closeNumbers.size()==2){
                            gearSum+=closeNumbers.get(0).num*closeNumbers.get(1).num;
                        }
                    }
                }
            }
            System.out.println(gearSum);
        }
    }

}
