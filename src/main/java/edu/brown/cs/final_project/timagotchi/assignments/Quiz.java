package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.ArrayList;
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
    if (curRecord == null) {
      curRecord = new ArrayList<>();
    }
    if (qIdx == curRecord.size()) {
      curRecord.add(correct);
    }
    record.put(userId, curRecord);
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

  /**
   * Setter for whether this quiz is competitive.
   *
   * @param competitive Whether this assignment is competitive.
   */
  public void setCompetitive(Boolean competitive) {
    this.competitive = competitive;
  }

  /**
   * Getter for the questions of this Quiz.
   *
   * @return A list of the quiz questions.
   */
  public List<Question> getQuestions() {
    return questions;
  }

  /**
   * Setter for the questions of this Quiz.
   *
   * @param questions A list of the questions for this quiz.
   */
  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  @Override
  public Integer getScore(String userID) {
    List<Boolean> l = getRecord(userID);
    if (l != null) {
      int score = 0;
      for (Boolean b : l) {
        if (b) {
          score += 1;
        }
      }
      return score;
    }
    return null;
  }

  @Override
  public Integer getTotalScore() {
    return questions.size();
  }
}
