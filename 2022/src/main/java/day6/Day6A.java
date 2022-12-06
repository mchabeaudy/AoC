package day6;

import java.nio.file.Files;
import utils.AbstractDay;

public class Day6A extends AbstractDay {

    public Day6A() {
        super("day6");
    }

    public static void main(String[] args) throws Exception {
        new Day6A().solution();
    }

    @Override
    public void solution() throws Exception {
        var line = Files.readString(getPath());
        int k = 3;
        for (int i = 0; i < line.length() - 4; i++) {
            var c0 = line.charAt(i);
            var c1 = line.charAt(i+1);
            var c2 = line.charAt(i+2);
            var c3 = line.charAt(i+3);
            k++;
            if(c0!=c1 && c0!=c2 && c0!=c3 && c1!=c2 && c1!=c3 && c2!=c3){
                break;
            }
        }
        System.out.println(k);
    }

}

