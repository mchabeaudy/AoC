package day7;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import utils.AbstractDay;

public class Day7B extends AbstractDay {

    List<Dir> dirs = new ArrayList<>();

    public Day7B() {
        super("day7");
    }

    public static void main(String[] args) throws Exception {
        new Day7B().solution();
    }

    @Override
    public void solution() throws Exception {
        var lines = Files.readAllLines(getPath());
        var mainDir = new Dir("/");
        dirs.add(mainDir);
        Dir currentDir = mainDir;
        int i = 1;
        while (i < lines.size()) {
            var args = lines.get(i).split(" ");
            if (args[0].matches("\\d*$")) {
                currentDir.files.add(new File(Integer.parseInt(args[0]), args[1]));
            } else if (args[0].equals("$")) {
                if (args[1].equals("cd")) {
                    if (args[2].equals("..")) {
                        currentDir = currentDir.parent;
                    } else {
                        currentDir = currentDir.subDirs.stream()
                                .filter(d -> d.name.equals(args[2]))
                                .findAny()
                                .orElseThrow();
                    }
                }
            } else if ("dir".equals(args[0])) {
                var dir = new Dir(args[1]);
                dir.parent = currentDir;
                dirs.add(dir);
                currentDir.subDirs.add(dir);
            }
            i++;
        }
        System.out.println("total size: " + dirs.stream()
                .filter(d -> 30000000 <= 70000000 - mainDir.size() + d.size())
                .mapToInt(Dir::size)
                .min()
                .orElseThrow());
    }

    class Dir {

        String name;
        Set<Dir> subDirs = new HashSet<>();
        Set<File> files = new HashSet<>();
        Dir parent;

        public Dir(String name) {
            this.name = name;
        }

        int size() {
            return files.stream().mapToInt(f -> f.size).sum() + subDirs.stream().mapToInt(Dir::size).sum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Dir dir = (Dir) o;
            return Objects.equals(name, dir.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

    }

    class File {

        int size;
        String name;

        public File(int size, String name) {
            this.size = size;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            File file = (File) o;
            return Objects.equals(name, file.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

    }

}

