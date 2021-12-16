package day16;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {


  public static void main(String[] args) {
    try (var linesStream = Files.lines(
        Paths.get(getSystemResource("day16.txt").getPath().substring(1)))) {
      var line = linesStream.toList().get(0);
      var input = range(0, line.length()).mapToObj(line::charAt).map(Day16::hexToBin)
          .collect(Collectors.joining());

      int index = 0;
      List<Packet> packets = new ArrayList<>();
      while (index < line.length()) {
        int version = parseInt(input.substring(index, index + 3), 2);
        index += 3;
        int typeId = parseInt(input.substring(index, index + 3), 2);
        index += 3;
        var packet = new Packet(version, typeId);
        packets.add(packet);
        if (typeId == 4) {
          List<String> bits = new ArrayList<>();
          boolean process = true;
          while (process) {
            var seq = input.substring(index, index + 5);
            index += 5;
            bits.add(seq.substring(1));
            if (seq.charAt(0) == '0') {
              process = false;
            }
          }

        } else {
          int lengthTypeId = parseInt(input.substring(index, index + 1));
          index += 1;

          if (lengthTypeId == 0) {
            int totalSubPacketsLength = parseInt(input.substring(index, index + 15));
            index += 15;
          } else {
            int subPacketNumber = parseInt(input.substring(index, index + 11));
            index += 11;
          }
        }
      }

      System.out.println(input);

    } catch (Exception e) {

    }
  }

  static class Packet {

    int version;
    int typeId;
    List<Packet> subPacket;

    public Packet(int version, int typeId) {
      this.version = version;
      this.typeId = typeId;
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
