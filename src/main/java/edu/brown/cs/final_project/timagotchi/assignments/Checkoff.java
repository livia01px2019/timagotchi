package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.HashMap;
import java.util.Map;

/**
 * Assignment that can just be checked off as complete (classwork,
 * participation, etc.).
 */
public class Checkoff implements Assignment {
  private String id;
  private String name;
  private Map<String, Boolean> complete;
  private int reward;

  /**
   * Initializes the Checkoff assignment.
   *
   * @param i The id of the assignment.
   * @param n The name of the assignment
   * @param r The xp reward received from completing this assignment.
   */
  public Checkoff(String i, String n, int r) {
    id = i;
    name = n;
    complete = new HashMap<String, Boolean>();
    reward = r;
  }

  @Override
  public Boolean getComplete(String userId) {
    return complete.get(userId);
  }

  @Override
  public void setComplete(String userId, Boolean c) {
    complete.put(userId, c);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int getReward() {
    return reward;
  }

  @Override
  public void setReward(int reward) {
    this.reward = reward;
  }

  @Override
  public Boolean isCompetitive() {
    return false;
  }

  @Override
  public Integer getScore(String userID) {
    if (complete.get(userID)) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public Integer getTotalScore() {
    return 1;
  }
}
