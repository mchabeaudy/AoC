package dec02;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class AoC_02_A {

  private static final String FILE = "dec02.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));
    var h = new AtomicInteger(0);
    var v = new AtomicInteger(0);
    lines.forEach(l -> {
      var in = l.split(" ");
      switch (in[0]) {
        case "forward" -> h.getAndAdd(parseInt(in[1]));
        case "down" -> v.getAndAdd(parseInt(in[1]));
        case "up" -> v.addAndGet(-parseInt(in[1]));
      }
    });
    System.out.println(h.get() * v.get());
  }

}
