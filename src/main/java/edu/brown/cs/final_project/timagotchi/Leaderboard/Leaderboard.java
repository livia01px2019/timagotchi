package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.List;

/**
 * A interface for Leaderboards that rank things.
 *
 * @param <T> The type of the object the leaderboard will be ranking.
 */
public interface Leaderboard<T> {
  /**
   * Getter for the ranking of Ts to be put into a leaderboard.
   *
   * @return The list of class ids.
   */
  List<T> getRanking();
}
