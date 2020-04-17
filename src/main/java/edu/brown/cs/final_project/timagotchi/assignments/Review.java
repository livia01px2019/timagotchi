package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Review implements Assignment {
  private String id;
  private String name;
  private Map<String, Boolean> complete;
  private int reward;
  private List<Question> questions;
  private Map<String, List<Boolean>> record;

  /**
   * Initializes the Quiz assignment.
   *
   * @param i The id of the assignment.
   * @param n The name of the assignment
   * @param f Whether this assignment has been finished.
   * @param r The xp reward received from completing this assignment.
   */
  public Review(String i, String n, Boolean f, int r) {
    id = i;
    name = n;
    complete = new HashMap<String, Boolean>();
    reward = r;
    record = new HashMap<String, List<Boolean>>();
  }

  /**
   * Function that uses the "DocDiff" algorithm to generate review questions based on the
   * questions the user with id userId has gotten wrong.
   * @param userId
   */
  public void generateQuestions(String userId) {
    questions = new ArrayList<Question>();
    // TODO: Implement algorithm!
  }

  /**
   * Adds a question to the list of questions associated with the assignment.
   * @param q The question to be added.
   */
  public void addQuestion(Question q) {
    questions.add(q);
  }

  /**
   * Getter for the answer record for this assignment by the user with id userId.
   *
   * @param userId The User ID for the user.
   * @return The list of whether the user got the answers right or wrong for each question.
   */
  public List<Boolean> getRecord(String userId) {
    return record.get(userId);
  }

  /**
   * Setter for whether or not the user with id userId got aquestion with index qIdx
   * right or wrong.
   *
   * @param userId The User ID for the user.
   * @param qIdx The index of the question.
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
   * Getter for whether this assignment has been completed by the user with id userId.
   *
   * @param userId The User ID for the user.
   * @return Whether the assignment has been completed or not by the user.
   */
  @Override
  public Boolean getComplete(String userId) {
    return complete.get(userId);
  }

  /**
   * Setter for whether this assignment has been completed by the user with id userId.
   *
   * @param userId The User ID for the user.
   * @param c Whether this assignment has been completed.
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
}
