package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;

/**
 * A leaderboard to rank students, both overall in a class and after a quiz
 * assignment.
 */
public class Userboard implements Leaderboard<Student> {
  private String classId;

  /**
   * Initializes the leaderboard.
   *
   * @param cid The id of the class to be ranked.
   */
  public Userboard(String cid) {
    classId = cid;
  }

  @Override
  public List<Student> getRanking() {
    Class c = Controller.getClass(classId);
    List<String> studentIds = c.getStudentIds();
    // Construct a list of all the students
    List<Student> students = new ArrayList<Student>();
    for (String sid : studentIds) {
      students.add(Controller.getStudent(sid));
    }
    // Return list of students sorted by their pets' xp.
    Collections.sort(students, new Student.CompareByXp().reversed());
    return students;
  }

  /**
   * Sort the students who have completed a assignment by their score.
   *
   * @param assignmentID The specified assignment.
   * @return The List of Tuples (studentName, score), ordered from highest to
   *         lowest score.
   */
  public List<List<String>> getRankingScore(String assignmentID) {
    Quiz a = (Quiz) Controller.getAssignment(assignmentID); // TODO: Fix here
    List<String> studentIDs = Controller.getStudentsFromAssignment(assignmentID);
    System.out.println("bla");
    System.out.println(studentIDs);
    List<Student> students = new ArrayList<Student>();
    for (String sid : studentIDs) {
      students.add(Controller.getStudent(sid));
    }
    System.out.println(students);
    Collections.sort(students, new Student.CompareByScore(assignmentID).reversed());
    System.out.println(students);
    List<List<String>> sortedID = new ArrayList<>();
    for (Student s : students) {
      System.out.println(s.getId());
      if (a.getComplete(s.getId())) {
        List<String> entry = new ArrayList<>();
        entry.add(s.getName());
        entry.add("" + a.getScore(s.getId()) / a.getTotalScore());
        sortedID.add(entry);
      }
    }
    System.out.println("Here");
    System.out.println(sortedID);
    return sortedID;
  }

  public static class CompareByAllXP implements Comparator<Student> {
    private String classID;

    public CompareByAllXP(String cid) {
      classID = cid;
    }

    public int getScore(String classID, String studentID) {
      Class c = Controller.getClass(classID);
      List<String> assignmentIDs = c.getAssignmentIds();
      int s1TotalXP = 0;
      for (String aid : assignmentIDs) {
        Assignment a = Controller.getAssignment(aid);
        if (a.getComplete(studentID)) {
          s1TotalXP = s1TotalXP + a.getReward();
        }
      }
      return s1TotalXP;
    }

    @Override
    public int compare(Student s1, Student s2) {
      int s1TotalXP = getScore(classID, s1.getId());
      int s2TotalXP = getScore(classID, s2.getId());
      return Double.compare(s1TotalXP, s2TotalXP);
    }
  }

  public List<List<String>> allAssignmentsXP() {
    Class c = Controller.getClass(classId);
    List<String> studentIDs = c.getStudentIds();
    List<Student> students = new ArrayList<>();
    for (String sid : studentIDs) {
      students.add(Controller.getStudent(sid));
    }
    CompareByAllXP compare = new CompareByAllXP(classId);
    Collections.sort(students, compare.reversed());
    List<List<String>> sorted = new ArrayList<>();
    for (Student s : students) {
      List<String> entry = new ArrayList<>();
      entry.add(s.getUsername());
      entry.add("" + compare.getScore(classId, s.getId()));
      sorted.add(entry);
    }
    return sorted;
  }

}
