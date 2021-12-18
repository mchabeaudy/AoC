package day18;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public class Day18A {

  public static void main(String[] args) {
    try {

      try (var linesStream = Files.lines(
          Paths.get(getSystemResource("day18.txt").getPath().substring(1)))) {

        var lines = linesStream.toList();

        var number = lines.get(0);

        for (int i = 1; i < lines.size(); i++) {
          var line = lines.get(i);
          number = "[" + number + "," + line + "]";
          number = reduceNumber(number);
          System.out.println(number);
        }

        int index = 0;
        while (number.contains(",")) {
          index = 0;

          while (index < number.length() - 3) {
            if (Character.isDigit(number.charAt(index))) {
              int lastNumberIndex = index;
              String n0 = "";
              while (Character.isDigit(number.charAt(lastNumberIndex))) {
                n0 += number.charAt(lastNumberIndex);
                lastNumberIndex++;
              }
              if (',' == number.charAt(lastNumberIndex) && Character.isDigit(
                  number.charAt(lastNumberIndex + 1))) {
                String n1 = "";
                int lastNumberIndex2 = lastNumberIndex + 1;
                while (Character.isDigit(number.charAt(lastNumberIndex2))) {
                  n1 += number.charAt(lastNumberIndex2);
                  lastNumberIndex2++;
                }
                long mag = parseLong(n0) * 3 + parseLong(n1) * 2;
                number =
                    number.substring(0, index - 1) + mag + number.substring(lastNumberIndex2 + 1);
                break;
              }

            }

            index++;
          }
        }

        System.out.println(number);

      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String reduceNumber(String number) {
    int nbOpenBracket = 0;
    int index = 0;
    boolean process = true;
    while (process) {
      //System.out.println(number);
      process = false;
      nbOpenBracket = 0;
      index = 0;

      while (index < number.length()) {
        char c = number.charAt(index);
        if (c == '[') {
          nbOpenBracket++;
        } else if (c == ']') {
          nbOpenBracket--;
        }
        if (nbOpenBracket > 4 && Character.isDigit(number.charAt(index + 1)) &&
            (Character.isDigit(number.charAt(index + 3)) || ((number.charAt(index + 3) == ',')
                && (Character.isDigit(number.charAt(index + 4)))))) {
          String out = "";

          // explode

          int k = index + 1;
          String n0 = "";
          while (Character.isDigit(number.charAt(k))) {
            n0 += number.charAt(k);
            k++;
          }
          k++;
          String n1 = "";
          while (Character.isDigit(number.charAt(k))) {
            n1 += number.charAt(k);
            k++;
          }
          int backwardIndex = index - 1;
          String begin = "";
          while (!Character.isDigit(number.charAt(backwardIndex)) && backwardIndex > 0) {
            backwardIndex--;
          }
          if (Character.isDigit(number.charAt(backwardIndex))) {
            int indexBackwardBegin = backwardIndex;
            while (Character.isDigit(number.charAt(indexBackwardBegin))) {
              indexBackwardBegin--;
            }
            int n = parseInt(range(indexBackwardBegin + 1, backwardIndex + 1)
                .mapToObj(number::charAt)
                .map(Objects::toString)
                .collect(Collectors.joining()));
            n += parseInt(n0);
            begin = number.substring(0, indexBackwardBegin + 1) + n;
            process = true;
          } else {
            begin = number.substring(0, backwardIndex + 1);
          }

          int forwardIndex = k;
          while (!Character.isDigit(number.charAt(forwardIndex))
              && forwardIndex < number.length() - 1) {
            forwardIndex++;
          }
          if (Character.isDigit(number.charAt(forwardIndex))) {
            int forwardIndexEnd = forwardIndex;
            process = true;
            while (Character.isDigit(number.charAt(forwardIndexEnd))) {
              forwardIndexEnd++;
            }
            int n = parseInt(range(forwardIndex, forwardIndexEnd)
                .mapToObj(number::charAt)
                .map(Objects::toString)
                .collect(Collectors.joining()));
            n += parseInt(n1);

            out = begin + number.substring(backwardIndex + 1, index) + "0" +
                number.substring(k + 1, forwardIndex) + n + number.substring(forwardIndexEnd);
          } else {
            out = begin + number.substring(backwardIndex + 1, index) + "0" + number.substring(
                k + 1);
          }
          number = out;
          break;
        }
        index++;
      }

      // split
      index = 1;
      while (index < number.length() && !process) {
        var c0 = number.charAt(index - 1);
        var c1 = number.charAt(index);
        if (Character.isDigit(c0) && Character.isDigit(c1)) {
          int k = parseInt("" + c0 + c1);
          int left = k % 2 == 1 ? (k - 1) / 2 : k / 2;
          int right = k % 2 == 1 ? (k + 1) / 2 : k / 2;
          String leftPart = number.substring(0, index - 1);
          String rightPart = number.substring(index + 1);
          number = leftPart + "[" + left + "," + right + "]" + rightPart;
          process = true;
          break;
        }
        index++;
      }
    }
    return number;
  }

}
