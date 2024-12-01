package day21;

import java.util.Map;

public class Day21B {

  static long winNbA = 0;
  static long winNbB = 0;

  public static void main(String[] args) {
    // count occurrences
    Map<Integer, Integer> diceRolls = Map.of(
        3, 1,
        4, 3,
        5, 6,
        6, 7,
        7, 6,
        8, 3,
        9, 1
    );

    calculateWins(diceRolls, 8, 7, 0, 0, true, 1);
    System.out.println("win nb: " + Math.max(winNbA, winNbB));
  }

  private static void calculateWins(Map<Integer, Integer> rolls, int aPos, int bPos, int aScore,
      int bScore, boolean playerATurn, long ocNum) {
    if (aScore >= 21) {
      winNbA += ocNum;
    } else if (bScore >= 21) {
      winNbB += ocNum;
    } else {
      rolls.forEach((die, num) -> {
        int newAScore = aScore;
        int newBScore = bScore;
        int newAPos = aPos;
        int newBPos = bPos;
        if (playerATurn) {
          newAPos += die;
          if (newAPos > 10) {
            newAPos = newAPos - 10;
          }
          newAScore += newAPos;
        } else {
          newBPos += die;
          if (newBPos > 10) {
            newBPos = newBPos - 10;
          }
          newBScore += newBPos;
        }
        calculateWins(rolls, newAPos, newBPos, newAScore, newBScore, !playerATurn, ocNum * num);
      });
    }
  }


}
