package edu.brown.cs.final_project.timagotchi.assignments;

import java.util.List;

/**
 * Question to appear on a Assignment/Quiz.
 */
public class Question {
  private String id;
  private String prompt;
  private List<String> choices;
  private List<Integer> answers;

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
}
