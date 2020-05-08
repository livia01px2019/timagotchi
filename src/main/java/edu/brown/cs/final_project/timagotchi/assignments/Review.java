package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.users.Class;

/**
 * Review Assignment that generates questions based off what the student has gotten wrong.
 */
public class Review implements Assignment {
  private final int initNumQ = 5;
  private int numQuestions;
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
   * @param r The xp reward received from completing this assignment.
   */
  public Review(String i, String n, int r) {
    id = i;
    name = n;
    complete = new HashMap<String, Boolean>();
    reward = r;
    record = new HashMap<String, List<Boolean>>();
    numQuestions = initNumQ;
  }

  /**
   * Function that uses the "DocDiff" algorithm to generate review questions based
   * on the questions the user with id userId has gotten wrong.
   *
   * @param studentId The id of the student to generate the review for.
   * @param classId   The id of the class they want to review.
   */
  public void generateQuestions(String studentId, String classId) {
    // DocDiff algorithm that goes through every question in the class and gets a similarity score
    // with the wrong questions, orders by similarity, and returns the top numQs most similar.
    Class c = Accessors.getClass(classId);
    List<String> wrongQuestionIds = Accessors.getWrongQuestionIDs(studentId, c.getId());
    // Get all the questions the student got wrong in this class.
    List<Question> wrongQuestions = new ArrayList<Question>();
    for (String qid : wrongQuestionIds) {
      wrongQuestions.add(Accessors.getQuestion(qid));
    }

    // Get all the questions used for this class.
    List<Question> allQuestions = Accessors.getAllQuestions(classId);
    // Generate a list of all unique words for all questions.
    Set<String> dictionary = new HashSet<String>();
    for (Question q : allQuestions) {
      // Add the unique words to the compiled list of all unique words.
      for (String word : q.getPromptSplit()) {
        dictionary.add(word);
      }
    }

    // Generate the frequency vectors for all the questions and all the wrong
    // questions.
    List<List<Integer>> qFreq = new ArrayList<List<Integer>>();
    List<List<Integer>> wrongQFreq = new ArrayList<List<Integer>>();
    for (Question q : allQuestions) {
      qFreq.add(determineFrequency(q, dictionary));
    }
    for (Question q : wrongQuestions) {
      wrongQFreq.add(determineFrequency(q, dictionary));
    }

    // Calculate the similarity of score for each question
    for (int i = 0; i < allQuestions.size(); i++) {
      Question q = allQuestions.get(i);
      List<Integer> vector = qFreq.get(i);
      Double qScore = 0.0;
      // Final score is average of all individual similarity scores.
      Double vectMag = dotProduct(vector, vector);
      for (List<Integer> wrongVector : wrongQFreq) {
        // Individual similarity score is dot product of two frequency vectors
        // divided by greater magnitude.
        Double overlap = dotProduct(vector, wrongVector);
        Double magnitude = Math.max(vectMag, dotProduct(wrongVector, wrongVector));
        qScore = qScore + (overlap / magnitude);
      }
      q.setScore(qScore / wrongQFreq.size());
    }

    // Sort all questions according to highest score
    Collections.sort(allQuestions, new Question.CompareByScore());

    // Set the questions to top numQuestions most relevant questions.
    if (allQuestions.size() >= numQuestions) {
      questions = allQuestions.subList(0, numQuestions);
    } else {
      questions = allQuestions.subList(0, allQuestions.size());
    }
  }

  /**
   * Determines the dot product of two integer vectors.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @return The dot product represented as a double.
   */
  private Double dotProduct(List<Integer> v1, List<Integer> v2) {
    int sum = 0;
    for (int i = 0; i < v1.size(); i++) {
      int product = v1.get(i) * v2.get(i);
      sum = sum + product;
    }
    return Double.valueOf(sum);
  }

  /**
   * Determines the frequency of each word in a given dictionary in a given
   * Question.
   *
   * @param q    The question to be analyzed.
   * @param dict The dictionary of all the words.
   * @return The list of frequencies of each word in the dictionary.
   */
  private List<Integer> determineFrequency(Question q, Set<String> dict) {
    List<Integer> freq = new ArrayList<Integer>();
    for (String word : dict) {
      int freqTemp = 0;
      // Determine frequency of current word in the Question.
      for (String qWord : q.getPromptSplit()) {
        if (word.equals(qWord)) {
          freqTemp++;
        }
      }
      freq.add(freqTemp);
    }
    return freq;
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

  @Override
  public Boolean getComplete(String userId) {
    return complete.get(userId);
  }

  @Override
  public void setComplete(String userId, Boolean c) {
    complete.put(userId, c);
  }

  /**
   * Getter for the number of questions for the Review.
   *
   * @return The number of questions for the Review.
   */
  public int getNumQuestions() {
    return numQuestions;
  }

  /**
   * Setter for the number of questions for the Review.
   *
   * @param n The number of questions for the Review.
   */
  public void setNumQuestions(int n) {
    this.numQuestions = n;
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
   * Getter for the questions of the Review.
   *
   * @return A list of the questions for the Review.
   */
  public List<Question> getQuestions() {
    return questions;
  }

  /**
   * Setter for the questions of the Review.
   *
   * @param questions A list of questions for the Review.
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
    return numQuestions;
  }
}
