import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Day2 extends AbstractDay {
    enum Color {
        RED, GREEN, BLUE;

        static Color getColor(String color) {
            return switch (color) {
                case "red" -> RED;
                case "blue" -> BLUE;
                case "green" -> GREEN;
                default -> throw new IllegalArgumentException("unknown color: " + color);
            };
        }
    }

    record CubeDraw(int number, Color color) {
    }

    record Draw(CubeDraw redDraw, CubeDraw greenDraw, CubeDraw blueDraw) {
    }

    record Game(int id, List<Draw> draws) {
        public Game(String line) {
            this(getId(line), getDraws(line));
        }

        private static List<Draw> getDraws(String line) {
            return Arrays.stream(line.substring(line.indexOf(':') + 1).split(";")).map(draw ->
            {
                draw = draw.replace(",", "").substring(1);
                var redDraw = new CubeDraw(0, Color.RED);
                var greenDraw = new CubeDraw(0, Color.GREEN);
                var blueDraw = new CubeDraw(0, Color.BLUE);
                var cubeDraws = draw.split(" ");
                for (int i = 0; i < cubeDraws.length / 2; i++) {
                    Color c = Color.getColor(cubeDraws[2 * i + 1]);
                    var cubeDraw = new CubeDraw(parseInt(cubeDraws[2 * i]), c);
                    if (c == Color.RED) redDraw = cubeDraw;
                    if (c == Color.GREEN) greenDraw = cubeDraw;
                    if (c == Color.BLUE) blueDraw = cubeDraw;
                }
                return new Draw(redDraw, greenDraw, blueDraw);
            }).toList();
        }

        public int getPower(){
            return draws.stream().map(Draw::redDraw).mapToInt(CubeDraw::number).max().orElseThrow()*
                    draws.stream().map(Draw::greenDraw).mapToInt(CubeDraw::number).max().orElseThrow()*
                    draws.stream().map(Draw::blueDraw).mapToInt(CubeDraw::number).max().orElseThrow();
        }

        private static int getId(String line) {
            return parseInt(line.substring(5, line.indexOf(':')));
        }


        public boolean isValid() {
            return draws.stream().noneMatch(draw -> draw.redDraw.number > 12
                    || draw.greenDraw.number > 13 || draw.blueDraw.number > 14);
        }
    }

    public Day2() {
        super("day2");
    }

    public static void main(String[] args) throws Exception {
        new Day2().solution();
    }

    @Override
    public void solution() throws Exception {
        try (var lines = Files.lines(getPath())) {
            var list = lines.toList();
            System.out.println(list.stream().map(Game::new).filter(Game::isValid).mapToInt(Game::id).sum());
            System.out.println(list.stream().map(Game::new).mapToInt(Game::getPower).sum());
        }
    }

}
