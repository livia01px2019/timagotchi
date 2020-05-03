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

  public static class CompareByAvgXP implements Comparator<Class> {

    public int getClassXP(Class c1) {
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
      if (c1.getStudentIds().size() == 0) {
        return 0;
      }
      return c1TotalXP / c1.getStudentIds().size();
    }

    @Override
    public int compare(Class c1, Class c2) {
      int c1TotalXP = getClassXP(c1);
      int c2TotalXP = getClassXP(c2);
      return Double.compare(c1TotalXP, c2TotalXP);
    }
  }

  public List<List<String>> rankClassByTotalXP() {
    // Get the list of classes.
    List<Class> classes = new ArrayList<Class>();
    for (String cid : classIds) {
      classes.add(Controller.getClass(cid));
    }
    CompareByAvgXP compare = new CompareByAvgXP();
    // Sort classes by average xp per student.
    Collections.sort(classes, compare.reversed());
    List<List<String>> sorted = new ArrayList<>();
    for (Class c : classes) {
      List<String> entry = new ArrayList<>();
      entry.add(c.getName());
      entry.add("" + compare.getClassXP(c));
      sorted.add(entry);
    }
    return sorted;
  }

}
