package edu.brown.cs.final_project.timagochi.utils;

import java.util.function.Function;

/**
 * Command Class - Function wrapper.
 */
public class Command {
  private Function<String, ?> function;

  /**
   * Constructor.
   * @param f Function to wrap.
   */
  public Command(Function<String, ?> f) {
    function = f;
  }

  /**
   * Applies the wrapped function.
   * @param input Input.
   */
  public void apply(String input) {
    function.apply(input);
  }
}
