package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.users.Class;

/**
 * Leaderboard for ranking class based on the average xp of a student in the class.
 */
public class Classboard implements Leaderboard<Class> {
  private List<String> classIds;

  /**
   * Initialize the Classboard.
   *
   * @param cids The list of class ids to be included in the leaderboard.
   */
  public Classboard(List<String> cids) {
    classIds = cids;
  }

  @Override
  public List<Class> getRanking() {
    // Get the list of classes.
    List<Class> classes = new ArrayList<Class>();
    for (String cid : classIds) {
      classes.add(Accessors.getClass(cid));
    }

    // Sort classes by average xp per student.
    Collections.sort(classes, new Class.CompareByAverageXp().reversed());
    return classes;
  }

  /**
   * Getter for the class ids being used for the Classboard.
   *
   * @return The list of class ids.
   */
  public List<String> getClassIds() {
    return classIds;
  }

  /**
   * Setter for the class ids being used for the Classboard.
   *
   * @param classIds The list of class ids.
   */
  public void setClassIds(List<String> classIds) {
    this.classIds = classIds;
  }

  /**
   * Comparator for comparing classes by the average xp of a student in the class.
   */
  public static class CompareByAvgXP implements Comparator<Class> {
    /**
     * Getter the average xp of a student in a class.
     *
     * @param c1 The class to find the average xp of.
     * @return The average xp of a student in the class.
     */
    public int getClassXP(Class c1) {
      List<String> c1Assignments = c1.getAssignmentIds();
      int c1TotalXP = 0;
      for (String aid : c1Assignments) {
        Assignment a = Accessors.getAssignment(aid);
        List<String> studentIDs = Accessors.getStudentsFromAssignment(aid);
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

  /**
   * Sorts classes by the average xp of a student in the class to be presented on a Leaderboard.
   *
   * @return A list of classes represented as lists of the class name and average xp.
   */
  public List<List<String>> rankClassByTotalXP() {
    // Get the list of classes.
    List<Class> classes = new ArrayList<Class>();
    for (String cid : classIds) {
      classes.add(Accessors.getClass(cid));
    }
    CompareByAvgXP compare = new CompareByAvgXP();
    // Sort classes by average xp per student.
    Collections.sort(classes, compare.reversed());
    List<List<String>> sorted = new ArrayList<>();
    for (Class c : classes) {
      List<String> entry = new ArrayList<>();
      entry.add(c.getName());
      entry.add("" + ((double) compare.getClassXP(c)) / c.getStudentIds().size());
      sorted.add(entry);
    }
    return sorted;
  }
}
