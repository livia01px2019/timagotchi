package edu.brown.cs.final_project.timagotchi.assignments;

/**
 * A generic Assignment interface.
 */
public interface Assignment {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  Boolean getComplete(String userId);

  void setComplete(String userId, Boolean c);

  int getReward();

  void setReward(int reward);

  // TODO: please implement a get score for each student type function :)
}
