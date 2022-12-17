package day13;

import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import utils.AbstractDay;

public class Day13B extends AbstractDay {


    public Day13B() {
        super("day13");
    }

    public static void main(String[] args) throws Exception {
        new Day13B().solution();
    }

    int k = 1;

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        var allLines = new ArrayList<String>();
        allLines.add("[[2]]");
        allLines.add("[[6]]");
        range(0, lines.size() / 3 + 1)
                .forEach(i -> {
                    allLines.add(lines.get(i * 3));
                    allLines.add(lines.get(i * 3 + 1));
                });
        allLines.sort((s1, s2) -> isInRightOrder(s1, "", s2, "", s1, s2) ? -1 : 1);
        range(0, allLines.size()).forEach(i -> {
            if (allLines.get(i).equals("[[2]]") || allLines.get(i).equals("[[6]]")) {
                k *= (i + 1);
            }
        });
        System.out.println(k);
    }


    boolean isInRightOrder(String line1, String rest1, String line2, String rest2, String initial1, String initial2) {
        if (line1.length() == 0 && line2.length() == 0) {
            return isInRightOrder(rest1, "", rest2, "", initial1, initial2);
        }
        if (line1.length() == 0) {
            return true;
        }
        if (line2.length() == 0) {
            return false;
        }
        if (line1.startsWith(",")) {
            line1 = line1.substring(1);
        }
        if (line2.startsWith(",")) {
            line2 = line2.substring(1);
        }
        if (line1.startsWith("[") && line2.startsWith("[")) {
            if (size(line1) == size(line2)) {
                return isInRightOrder(subList(line1), rest(line1) + rest1, subList(line2), rest(line2) + rest2,
                        initial1, initial2);
            } else {
                if (subList(line1).length() == 0) {
                    return true;
                }
                if (subList(line2).length() == 0) {
                    return false;
                }
                return isInRightOrder(subList(line1), "", subList(line2), "", initial1, initial2);
            }
        } else if (line1.startsWith("[")) {
            if (line1.length() == 2) {
                return true;
            }
            line2 = convertFirstElementToList(line2);
            return isInRightOrder(line1, rest1, line2, rest2, initial1, initial2);
        } else if (line2.startsWith("[")) {
            if (line2.length() == 2) {
                return false;
            }
            line1 = convertFirstElementToList(line1);
            return isInRightOrder(line1, rest1, line2, rest2, initial1, initial2);
        } else {
            int i1 = getFirstInt(line1);
            int i2 = getFirstInt(line2);
            if (i1 < i2) {
                return true;
            } else if (i1 > i2) {
                return false;
            } else {
                return isInRightOrder(cutFirstElement(line1), rest1, cutFirstElement(line2), rest2, initial1, initial2);
            }
        }
    }

    String subList(String l) {
        return l.substring(1, getClosingIndex(l));
    }

    String rest(String l) {
        String s = subList(l);
        if (s.length() + 2 == l.length()) {
            return "";
        }
        return l.substring(getClosingIndex(l) + 1);
    }

    int size(String line) {
        if (line.startsWith("[]")) {
            return 0;
        }
        int size = 1;
        int n = 0;
        for (int i = 0; i < line.length() && i < getClosingIndex(line); i++) {
            char c = line.charAt(i);
            if (c == '[') {
                n++;
            } else if (c == ']') {
                n--;
            }
            if (c == ',' && n == 1) {
                size++;
            }
        }
        return size;
    }

    String cutFirstElement(String line) {
        if (line.length() == 1) {
            return "";
        } else if ((line.substring(0, 2).matches("\\d*$"))) {
            return line.substring(2);
        } else {
            return line.substring(1);
        }
    }

    int getFirstInt(String line) {
        if (line.length() == 1) {
            return Integer.parseInt(line);
        } else if ((line.substring(0, 2).matches("\\d*$"))) {
            return Integer.parseInt(line.substring(0, 2));
        }
        return Integer.parseInt(line.substring(0, 1));
    }

    String convertFirstElementToList(String line) {
        if (line.length() == 1) {
            return "[" + line + "]";
        } else if (line.substring(0, 2).matches("\\d*$")) {
            return "[" + line.substring(0, 2) + "]" + line.substring(2);
        }
        return "[" + line.charAt(0) + "]" + line.substring(1);
    }


    int getClosingIndex(String line) {
        int k = 0;
        int n = 0;
        while (true) {
            if (line.charAt(k) == ']') {
                n--;
            } else if (line.charAt(k) == '[') {
                n++;
            }
            if (n == 0) {
                break;
            }
            k++;
        }
        return k;
    }

}

