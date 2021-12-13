package day02;

import static java.lang.ClassLoader.getSystemResource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class Day02B {

  private static final String FILE = "day02.txt";

  public static void main(String[] args) throws Exception {
    var lines = Files.lines(Paths.get(getSystemResource(FILE).getPath().substring(1)));
    var h = new AtomicLong(0);
    var v = new AtomicLong(0);
    var aim = new AtomicLong(0);
    lines.forEach(l -> {
      var in = l.split(" ");
      long d = Long.parseLong(in[1]);
      switch (in[0]) {
        case "forward" -> {
          h.addAndGet(d);
          v.addAndGet(aim.get() * d);
        }
        case "down" -> aim.addAndGet(d);
        case "up" -> aim.addAndGet(-d);
      }
    });
    System.out.println(v.get() * h.get());
  }

}
