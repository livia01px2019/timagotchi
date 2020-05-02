package edu.brown.cs.final_project.timagotchi.Leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
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

  /*
   * public List<List<String>> allAssignmentsXP(String classID) { Class c =
   * Controller.getClass(classID); List<String> assignmentIDs =
   * c.getAssignmentIds(); for (String aid : assignmentIDs) { Assignment a =
   * Controller.getAssignment(aid); List<String> studentIDs =
   * Controller.getStudentsFromAssignment(aid); int reward = 0; for (String sid :
   * studentIDs) { if (a.getComplete(sid)) { reward = reward + a.getReward(); } }
   * } }
   */
}
