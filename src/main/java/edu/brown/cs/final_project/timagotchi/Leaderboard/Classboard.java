package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.users.Class;

public class Classboard implements Leaderboard<Class> {
  private List<String> classIds;

  public Classboard(List<String> cids) {
    classIds = cids;
  }

  @Override
  public List<Class> getRanking() {
    // Get the list of classes.
    List<Class> classes = new ArrayList<Class>();
    for (String cid : classIds) {
      classes.add(Controller.getClass(cid));
    }

    // Sort classes by average xp per student.
    Collections.sort(classes, new Class.CompareByAverageXp().reversed());
    return classes;
  }

  public List<String> getClassIds() {
    return classIds;
  }

  public void setClassIds(List<String> classIds) {
    this.classIds = classIds;
  }
}
