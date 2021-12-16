package day16;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Long.parseLong;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day16B {


  public static void main(String[] args) {
    try (var linesStream = Files.lines(
        Paths.get(getSystemResource("day16.txt").getPath().substring(1)))) {
      var line = linesStream.toList().get(0);
      var input = range(0, line.length()).mapToObj(line::charAt).map(Day16B::hexToBin)
          .collect(Collectors.joining());

      var index = new AtomicInteger();
      var packet = new Packet();
      packet.parseSubPackets(index, input);

      System.out.println("sum: " + packet.value());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static class Packet {

    long version;
    long typeId;
    long lengthTypeId;
    List<String> bits;

    List<Packet> subPackets;

    public Packet() {
    }


    public void parseSubPackets(AtomicInteger index, String input) {
      version = parseLong(input.substring(index.get(), index.addAndGet(3)), 2);
      typeId = parseLong(input.substring(index.get(), index.addAndGet(3)), 2);
      if (typeId == 4) {
        bits = new ArrayList<>();
        boolean process = true;
        while (process) {
          var seq = input.substring(index.get(), index.addAndGet(5));
          bits.add(seq.substring(1));
          if (seq.charAt(0) == '0') {
            process = false;
          }
        }
      } else {
        subPackets = new ArrayList<>();
        lengthTypeId = parseLong(input.substring(index.get(), index.addAndGet(1)));
        if (lengthTypeId == 0) {
          long totalSubPacketsLength = parseLong(input.substring(index.get(), index.addAndGet(15)),
              2);
          int indexValue = index.get();
          while (index.get() - indexValue != totalSubPacketsLength) {
            var packet = new Packet();
            subPackets.add(packet);
            packet.parseSubPackets(index, input);
          }
        } else {
          long subPacketNumber = parseLong(input.substring(index.get(), index.addAndGet(11)), 2);
          while (subPackets.size() < subPacketNumber) {
            var packet = new Packet();
            packet.parseSubPackets(index, input);
            subPackets.add(packet);
          }
        }
      }
    }

    long value() {
      return switch ((int)typeId) {
        case 0 -> subPackets.stream().mapToLong(Packet::value).sum();
        case 1 -> product();
        case 2 -> subPackets.stream().mapToLong(Packet::value).min().orElse(0);
        case 3 -> subPackets.stream().mapToLong(Packet::value).max().orElse(0);
        case 4 -> parseLong(String.join("", bits), 2);
        case 5 -> greaterThan();
        case 6 -> lessThan();
        case 7 -> equal();
        default -> 0;
      };
    }

    long product() {
      long p = 1;
      for (Packet packet : subPackets) {
        p *= packet.value();
      }
      return p;
    }

    long greaterThan() {
      return subPackets.get(0).value() > subPackets.get(1).value() ? 1 : 0;
    }

    long lessThan() {
      return subPackets.get(0).value() < subPackets.get(1).value() ? 1 : 0;
    }

    long equal() {
      return subPackets.get(0).value() == subPackets.get(1).value() ? 1 : 0;
    }
  }

  private static String hexToBin(char hex) {
    return switch (hex) {
      case '0' -> "0000";
      case '1' -> "0001";
      case '2' -> "0010";
      case '3' -> "0011";
      case '4' -> "0100";
      case '5' -> "0101";
      case '6' -> "0110";
      case '7' -> "0111";
      case '8' -> "1000";
      case '9' -> "1001";
      case 'A' -> "1010";
      case 'B' -> "1011";
      case 'C' -> "1100";
      case 'D' -> "1101";
      case 'E' -> "1110";
      case 'F' -> "1111";
      default -> throw new IllegalArgumentException();
    };
  }
}
