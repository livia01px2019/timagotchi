package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.Comparator;
import java.util.List;

/**
 * Question to appear on a Assignment/Quiz.
 */
public class Question {
  private String id;
  private String prompt;
  private String[] promptSplit;
  private List<String> choices;
  private List<Integer> answers;
  private Double score;

  /**
   * Initializes the Question.
   *
   * @param i The id of the question.
   * @param p The prompt of the question.
   * @param c The possible answer choices.
   * @param a The index of the correct answer(s) in the answer choices.
   */
  public Question(String i, String p, List<String> c, List<Integer> a) {
    id = i;
    prompt = p;
    choices = c;
    answers = a;
    score = 0.0;
    // Convert prompt to only lowercase alphanumeric
    String promptLower = p.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
    // Split prompt by each word
    promptSplit = promptLower.split("\\s+");
  }

  /**
   * Comparator for comparing questions by their similarity score.
   */
  public static class CompareByScore implements Comparator<Question> {
    /**
     * Empty constructor.
     */
    public CompareByScore() {
    }

    @Override
    public int compare(Question q1, Question q2) {
      return Double.compare(q2.getScore(), q1.getScore());
    }
  }

  /**
   * Getter for the question prompt.
   *
   * @return The question prompt.
   */
  public String getPrompt() {
    return prompt;
  }

  /**
   * Setter for the question prompt.
   *
   * @param prompt The question prompt.
   */
  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  /**
   * Getter for the question answer choices.
   *
   * @return The question answer choices.
   */
  public List<String> getChoices() {
    return choices;
  }

  /**
   * Setter for the question answer choices.
   *
   * @param choices A list of question choices.
   */
  public void setChoices(List<String> choices) {
    this.choices = choices;
  }

  /**
   * Getter for the correct answers to the question.
   *
   * @return A list of the correct answers.
   */
  public List<Integer> getAnswers() {
    return answers;
  }

  /**
   * Setter for the correct answers to the question.
   *
   * @param answers A list of the correct answers.
   */
  public void setAnswers(List<Integer> answers) {
    this.answers = answers;
  }

  /**
   * Getter for the question id.
   *
   * @return The question id.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the question id.
   *
   * @param id The question id.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Getter for the prompt split into undercase words.
   *
   * @return An array of each undercase word.
   */
  public String[] getPromptSplit() {
    return promptSplit;
  }

  /**
   * Getter for the similarity score of the question.
   *
   * @return The similarity score.
   */
  public Double getScore() {
    return score;
  }

  /**
   * Setter for the similarity score of the question.
   *
   * @param score The similarity score.
   */
  public void setScore(Double score) {
    this.score = score;
  }
}
