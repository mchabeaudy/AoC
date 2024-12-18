import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 extends AbstractDay {

  long registerA;
  long registerB;
  long registerC;
  List<Integer> program;
  int instructionPointer;
  List<Long> output;
  static final int ADV = 0;
  static final int BXL = 1;
  static final int BST = 2;
  static final int JNZ = 3;
  static final int BXC = 4;
  static final int OUT = 5;
  static final int BDV = 6;
  static final int CDV = 7;

  public Day17() {
    super("day17");
  }

  @Override
  void solution() throws Exception {
    try (var lines = Files.lines(getPath())) {
      var input = lines.toList();
      registerA = parseLong(input.getFirst().replace("Register A: ", ""));
      registerB = parseLong(input.get(1).replace("Register B: ", ""));
      registerC = parseLong(input.get(2).replace("Register C: ", ""));
      output = new ArrayList<>();
      program = stream(input.get(4).replace("Program: ", "").split(",")).map(Integer::parseInt).toList();

      // part 1
      while (instructionPointer < program.size() - 1) {
        execute(new Operation(program.get(instructionPointer), program.get(instructionPointer + 1)));
      }
      System.out.println(result());

      // part 2
//      long kinit = 210892_507_286_223L;
//      long kmax = 247460932890320L;

      //      long kinit = 236548290631303L;
//      long kmax = 236548294394453L;

      /// //////////////
//      long kinit = 216592050786542L;
//      long kmax = 216592052948062L;
//      long kinit = 216592050808157L;
//      long kmax = 216592050829772L;

      // 216592045774019
      // 216593003671995

      // 234184231954731
      // 234185189852707
      long kinit = 236548275891843L;
      long kmax = 236548294347623L;

      // 236548275891843
      // 236548294347623

      long registerAInitial = kinit;
      int k = 0;
      while (registerAInitial <= kmax) {
//        registerA = registerAInitial;
//        registerB = 0;
//        registerC = 0;
//        instructionPointer = 0;
//        output = new ArrayList<>();
//        while (instructionPointer < program.size() - 1) {
          execute(registerAInitial);
//        }
//        System.out.println("registerAInitial:" + registerAInitial + " program.diff=" + diff() + " score :"+score());
        if (output.size() == program.size()) {
          System.out.println("registerAInitial:" + registerAInitial + " program.diff=" + diff() + " score :" + score());
        }
        k++;
//        registerAInitial = kinit + (k * (kmax - kinit)) / n;
        if (k % 1000000 == 0) {
          System.out.println("k:" + k + " progression = " + ((100.0 * k) / (kmax - kinit)) + "%");
        }
        registerAInitial++;
      }
    }
  }

  public Result execute(long initialA) {
    registerA = initialA;
    registerB = 0;
    registerC = 0;
    instructionPointer = 0;
    output = new ArrayList<>();
    while (instructionPointer < program.size() - 1) {
      execute(new Operation(program.get(instructionPointer), program.get(instructionPointer + 1)));
      if (range(0, output.size()).anyMatch(i -> output.get(i).intValue() != program.get(i))) {
        return Result.STOP;
      }
    }
    if (range(0, output.size()).anyMatch(i -> output.get(i).intValue() != program.get(i))) {
      return Result.STOP;
    }
    return Result.FOUND;
  }

  private String result() {
    return output.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  long score() {
    long k = 0;
    for (int i = program.size() - 1; i >= 0; i--) {
      if (output.get(i).intValue() == program.get(i)) {
        k++;
      } else {
        break;
      }
    }
    return k;
  }

  private String diff() {
    return range(0, program.size()).mapToObj(i -> String.valueOf(program.get(i) - output.get(i)))
        .collect(Collectors.joining(","));
  }

  public void execute(Operation operation) {
    switch (operation.opcode) {
      case ADV -> adv(operation.operand());
      case BXL -> bxl(operation.operand());
      case BST -> bst(operation.operand());
      case JNZ -> jnz(operation.operand());
      case BXC -> bxc(operation.operand());
      case OUT -> out(operation.operand());
      case BDV -> bvd(operation.operand());
      case CDV -> cdv(operation.operand());
      default -> throw new IllegalStateException("Unexpected value: " + operation.opcode);
    }
  }

  private void bvd(int operand) {
    registerB = (long) (registerA / Math.pow(2, comboOperand(operand)));
    incrementInstruction();
  }

  private void cdv(int operand) {
    registerC = (long) (registerA / Math.pow(2, comboOperand(operand)));
    incrementInstruction();
  }

  private void out(int operand) {
    output.add(comboOperand(operand) % 8);
    incrementInstruction();
  }

  private void bxc(int operand) {
    registerB = registerB ^ registerC;
    incrementInstruction();
  }

  private void jnz(int operand) {
    if (registerA != 0) {
      instructionPointer = operand;
    } else {
      incrementInstruction();
    }
  }

  void incrementInstruction() {
    instructionPointer += 2;
  }

  private void bst(int operand) {
    registerB = comboOperand(operand) % 8;
    incrementInstruction();
  }

  private void bxl(int operand) {
    registerB = registerB ^ operand;
    incrementInstruction();
  }

  private void adv(int operand) {
    registerA = (long) (registerA / Math.pow(2, comboOperand(operand)));
    incrementInstruction();
  }

  public static void main(String[] args) throws Exception {
    new Day17().solution();
  }

  public enum Result {
    CONTINUE,
    STOP,
    FOUND
  }

  record Operation(int opcode, int operand) {

  }

  long comboOperand(int operand) {
    return switch (operand) {
      case 0, 1, 2, 3 -> operand;
      case 4 -> registerA;
      case 5 -> registerB;
      case 6 -> registerC;
      case 7 -> throw new IllegalArgumentException("reserved");
      default -> throw new IllegalArgumentException();
    };
  }
}
