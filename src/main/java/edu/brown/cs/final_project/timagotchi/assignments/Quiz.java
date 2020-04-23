package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assignment that has a set of questions.
 */
public class Quiz implements Assignment {
  private String id;
  private String name;
  private Map<String, Boolean> complete;
  private int reward;
  private List<Question> questions;
  private Boolean competitive;
  private Map<String, List<Boolean>> record;

  /**
   * Initializes the Quiz assignment.
   *
   * @param i  The id of the assignment.
   * @param n  The name of the assignment
   * @param r  The xp reward received from completing this assignment.
   * @param qs The list of questions associated with the assignment.
   * @param c  Whether this assignment is competitive.
   */
  public Quiz(String i, String n, int r, List<Question> qs, Boolean c) {
    id = i;
    name = n;
    complete = new HashMap<String, Boolean>();
    reward = r;
    questions = qs;
    competitive = c;
    record = new HashMap<String, List<Boolean>>();
  }

  /**
   * Adds a question to the list of questions associated with the assignment.
   *
   * @param q The question to be added.
   */
  public void addQuestion(Question q) {
    questions.add(q);
  }

  /**
   * Getter for the answer record for this assignment by the user with id userId.
   *
   * @param userId The User ID for the user.
   * @return The list of whether the user got the answers right or wrong for each
   *         question.
   */
  public List<Boolean> getRecord(String userId) {
    return record.get(userId);
  }

  /**
   * Setter for whether or not the user with id userId got aquestion with index
   * qIdx right or wrong.
   *
   * @param userId  The User ID for the user.
   * @param qIdx    The index of the question.
   * @param correct Whether or not the user got the answer correct.
   */
  public void setRecord(String userId, int qIdx, Boolean correct) {
    List<Boolean> curRecord = record.get(userId);
    // Question being answered is the next question in the list of questions.
    if (qIdx == curRecord.size()) {
      curRecord.add(correct);
    }
    record.replace(userId, curRecord);
  }

  /**
   * Getter for whether this assignment has been completed by the user with id
   * userId.
   *
   * @param userId The User ID for the user.
   * @return Whether the assignment has been completed or not by the user.
   */
  @Override
  public Boolean getComplete(String userId) {
    return complete.get(userId);
  }

  /**
   * Setter for whether this assignment has been completed by the user with id
   * userId.
   *
   * @param userId The User ID for the user.
   * @param c      Whether this assignment has been completed.
   */
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

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public Boolean getCompetitive() {
    return competitive;
  }

  public void setCompetitive(Boolean competitive) {
    this.competitive = competitive;
  }
}
