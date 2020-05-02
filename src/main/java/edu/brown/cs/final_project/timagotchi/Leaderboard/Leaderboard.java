package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.List;

/**
 * A interface for Leaderboards that rank things.
 *
 * @param <T> The type of the object the leaderboard will be ranking.
 */
public interface Leaderboard<T> {
  List<T> getRanking();
}
