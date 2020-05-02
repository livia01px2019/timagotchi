package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
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

  public static class CompareByAllXP implements Comparator<Class> {
    @Override
    public int compare(Class c1, Class c2) {
      List<String> c1Assignments = c1.getAssignmentIds();
      int c1TotalXP = 0;
      for (String aid : c1Assignments) {
        Assignment a = Controller.getAssignment(aid);
        List<String> studentIDs = Controller.getStudentsFromAssignment(aid);
        for (String sid : studentIDs) {
          if (a.getComplete(sid)) {
            c1TotalXP = c1TotalXP + a.getReward();
          }
        }
      }
      List<String> c2Assignments = c2.getAssignmentIds();
      int c2TotalXP = 0;
      for (String aid : c2Assignments) {
        Assignment a = Controller.getAssignment(aid);
        List<String> studentIDs = Controller.getStudentsFromAssignment(aid);
        for (String sid : studentIDs) {
          if (a.getComplete(sid)) {
            c2TotalXP = c2TotalXP + a.getReward();
          }
        }
      }
      return Double.compare(c1TotalXP, c2TotalXP);
    }
  }

  public List<Class> rankClassByTotalXP() {
    // Get the list of classes.
    List<Class> classes = new ArrayList<Class>();
    for (String cid : classIds) {
      classes.add(Controller.getClass(cid));
    }

    // Sort classes by average xp per student.
    Collections.sort(classes, new CompareByAllXP().reversed());
    return classes;
  }

}
