package edu.brown.cs.final_project.timagochi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.brown.cs.final_project.timagochi.utils.DBProxy;

public class Controller {

  /**
   * Startup Command
   */
  public Void startUpCommand(String input) {
    try {
      DBProxy.connect(input);
      System.out.println("Connected to: " + input);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Class Command
   */
  public Void addClassCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "INSERT INTO classes VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(inputList[0], inputList[1], inputList[2])));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

}
