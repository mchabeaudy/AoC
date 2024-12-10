import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day9 extends AbstractDay {

  List<DiskSpace> diskSpaces = new ArrayList<>();

  List<Memory> memories = new ArrayList<>();
  List<Memory> memories2 = new ArrayList<>();
  List<Integer> processedId = new ArrayList<>();

  protected Day9() {
    super("day9");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var line = Arrays.stream(lines.findAny().orElseThrow().split("")).map(Integer::parseInt).toList();
      int id = 0;
      boolean isFile = true;
      int memoryPosition = 0;
      for (int space : line) {
        DiskSpace diskSpace;
        if (isFile) {
          diskSpace = new DiskSpace(id, true, space);
          id++;
        } else {
          diskSpace = new DiskSpace(0, false, space);
        }
        diskSpaces.add(diskSpace);
        for (int memoryIndex = 0; memoryIndex < space; memoryIndex++) {
          memories.add(new Memory(diskSpace.id, memoryPosition, diskSpace.occupied));
          memories2.add(new Memory(diskSpace.id, memoryPosition, diskSpace.occupied));
          memoryPosition++;
        }
        isFile = !isFile;
      }

      // part 1
      boolean operate = true;
      while (operate) {
        operate = doOperation();
      }
      System.out.println(range(0, memories.size()).mapToLong(k -> ((long) memories.get(k).id) * k).sum());

      // part 2
      for (int i = id - 1; i >= 0; i--) {
        int idToProcess = i;
        var fileMemories = memories2.stream().filter(m -> m.id == idToProcess).toList();
        int pos = fileMemories.stream()
            .min(Comparator.comparingInt(Memory::getMemoryPosition))
            .map(Memory::getMemoryPosition)
            .orElseThrow();

        int s = 0;
        while (s < pos) {
          if (range(s, fileMemories.size() + s).noneMatch(mIndex -> memories2.get(mIndex).occupied)) {
            range(s, fileMemories.size() + s)
                .mapToObj(memories2::get)
                .forEach(memory -> {
                  memory.occupied = true;
                  memory.id = idToProcess;
                });
            fileMemories.forEach(m -> {
              m.occupied = false;
              m.id = 0;
            });
            break;
          }
          s++;
        }
      }
      System.out.println(range(0, memories2.size()).mapToLong(k -> ((long) memories2.get(k).id) * k).sum());
    }
  }

  private boolean doOperation() {
    var lastOccupied = memories.stream().filter(m -> m.occupied)
        .max(Comparator.comparingInt(Memory::getMemoryPosition))
        .orElseThrow();
    var firstFree = memories.stream().filter(m -> !m.occupied)
        .min(Comparator.comparingInt(Memory::getMemoryPosition))
        .orElseThrow();
    if (lastOccupied.memoryPosition > firstFree.memoryPosition) {
      firstFree.setId(lastOccupied.getId());
      firstFree.setOccupied(true);
      lastOccupied.setId(0);
      lastOccupied.setOccupied(false);
      return true;
    }
    return false;
  }

  public static void main(String[] args) throws Exception {
    new Day9().solution();
  }

  class Memory {

    int id;

    final int memoryPosition;

    boolean occupied;

    public Memory(int id, int memoryPosition, boolean occupied) {
      this.id = id;
      this.memoryPosition = memoryPosition;
      this.occupied = occupied;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public boolean isOccupied() {
      return occupied;
    }

    public void setOccupied(boolean occupied) {
      this.occupied = occupied;
    }

    public int getMemoryPosition() {
      return memoryPosition;
    }

    @Override
    public String toString() {
      return occupied ? String.valueOf(id) : ".";
    }
  }

  class DiskSpace {

    int id;

    boolean occupied;

    int space;

    public DiskSpace(int id, boolean occupied, int space) {
      this.id = id;
      this.occupied = occupied;
      this.space = space;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public boolean isOccupied() {
      return occupied;
    }

    public void setOccupied(boolean occupied) {
      this.occupied = occupied;
    }

    public int getSpace() {
      return space;
    }

    public void setSpace(int space) {
      this.space = space;
    }
  }
}
