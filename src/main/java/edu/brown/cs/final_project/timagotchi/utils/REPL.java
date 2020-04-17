package edu.brown.cs.final_project.timagotchi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The REPL. Contains functionality to add and apply commands, and also begin accepting
 * input through the command line.
 */
public class REPL extends BufferedReader {
  private Map<String, Command> commands;

  /**
   * Initializes the REPL. Takes in an InputStreamReader to specify where input should come from,
   * and where output should go.
   *
   * @param r Reader for input/output.
   */
  public REPL(InputStreamReader r) {
    super(r);
    commands = new HashMap<>();
  }

  /**
   * Adds a command to the REPL. The Command is then callable through the REPL by String call.
   *
   * @param call command to enter to call command.
   * @param command function to be bound to call.
   */
  public void addCommand(String call, Command command) {
    commands.put(call, command);
  }

  /**
   * Starts the REPL in an infinite loop. Does this by reading input from the command line,
   * then mapping the first "word" of the input to a Map of Functions, applying the function
   * if it exists, and returning an error if it doesn't. The  function will be passed ONLY
   * the parameters of the repl call, not the call itself.
   */
  public void begin() {
    try {
      String input;
      while ((input = this.readLine()) != null) {
        applyCommand(input);
      }
    } catch (IOException err) {
      System.err.println("ERROR: Invalid input.");
    }
  }

  /**
   * Searches for the command with the given call. If it exists, it is applied.
   * Otherwise, an error is printed to the user.
   *
   * @param input command to enter to call func.
   */
  private void applyCommand(String input) {
    String call = input.split("\\s+")[0];
    Command command = commands.get(call);
    if (command != null) {
      command.apply(input.substring(call.length()).strip());
    } else {
      System.err.println("ERROR: Invalid command");
    }
  }
}
