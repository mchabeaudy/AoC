package day21;

public class Day21A {

  public static void main(String[] args) {
    long aScore = 0;
    long bScore = 0;
    int aPos = 7;
    int bPos = 6;
    int die = 1;
    long rollNb = 0;
    boolean aTurn = true;
    while (aScore < 1000 && bScore < 1000) {
      int toAdd = 0;
      rollNb += 3;
      for (int k = 0; k < 3; k++) {
        toAdd += die;
        die = next(die);
      }
      if (aTurn) {
        aPos = (aPos + toAdd) % 10 ;
        aScore += aPos+1;
      } else {
        bPos = (bPos + toAdd) % 10;
        bScore += bPos+1;
      }
      aTurn = !aTurn;
    }
    System.out.println("nb = " + rollNb * (aTurn ? aScore : bScore));
  }

  static int next(int i) {
    if (i == 100) {
      return 1;
    }
    return i + 1;
  }

}
