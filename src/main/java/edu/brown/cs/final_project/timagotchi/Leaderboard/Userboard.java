package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.List;

/**
 * A Leaderboard to rank students, both overall in a class and after a quiz
 * assignment.
 */
public class Userboard implements Leaderboard<String> {
  public Userboard(String classId) {

  }

  @Override
  public List<String> getRanking() {
    return null;
  }
}
