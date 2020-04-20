package edu.brown.cs.final_project.timagotchi.assignments;

import edu.brown.cs.final_project.timagotchi.users.Class;

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

  public static class CompareByScore implements Comparator<Question> {
    public CompareByScore() {
    }

    @Override
    public int compare(Question q1, Question q2) {
      return Double.compare(q1.getScore(), q2.getScore());
    }
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public List<String> getChoices() {
    return choices;
  }

  public void setChoices(List<String> choices) {
    this.choices = choices;
  }

  public List<Integer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Integer> answers) {
    this.answers = answers;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String[] getPromptSplit() {
    return promptSplit;
  }

  public void setPromptSplit(String[] promptSplit) {
    this.promptSplit = promptSplit;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }
}
