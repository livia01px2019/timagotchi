package edu.brown.cs.final_project.timagotchi.assignments;

/**
 * A generic Assignment interface.
 */
public interface Assignment {
  /**
   * Getter for assignment id.
   *
   * @return The assignment id.
   */
  String getId();

  /**
   * Setter for assignment id.
   *
   * @param id The assignment id.
   */
  void setId(String id);

  /**
   * Getter for assignment name.
   *
   * @return The assignment name.
   */
  String getName();

  /**
   * Setter for assignment name.
   *
   * @param name The assignment name.
   */
  void setName(String name);

  /**
   * Getter for whether the assignment is competitive.
   *
   * @return Whether the assignment is competitive.
   */
  Boolean isCompetitive();

  /**
   * Getter for whether the assignment is complete for a certain student.
   *
   * @param userId The id of the student to be queried.
   * @return Whether the assignment is complete.
   */
  Boolean getComplete(String userId);

  /**
   * Setter for whether the assignment is complete for a certain student.
   *
   * @param userId The id of the wanted student.
   * @param c Whether the assignment is complete.
   */
  void setComplete(String userId, Boolean c);

  /**
   * Getter for the reward of the assignment.
   *
   * @return The assignment reward.
   */
  int getReward();

  /**
   * Setter for the reward of the assignment.
   *
   * @param reward The assignment reward.
   */
  void setReward(int reward);

  /**
   * Getter for the score of the assignment for a certain student.
   *
   * @param userID The id of the student to be queried.
   * @return The assignment score for the student.
   */
  Integer getScore(String userID);

  /**
   * Getter for the total score of the assignment.
   *
   * @return The total score of the assignment.
   */
  Integer getTotalScore();
}
