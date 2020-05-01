package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.List;

/**
 * A generic Assignment interface.
 */
public interface Assignment {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  Boolean isCompetitive();

  Boolean getComplete(String userId);

  void setComplete(String userId, Boolean c);

  int getReward();

  void setReward(int reward);

  Integer getScore(String userID);

  Integer getTotalScore();
}
