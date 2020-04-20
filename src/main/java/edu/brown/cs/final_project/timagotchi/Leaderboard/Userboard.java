package edu.brown.cs.final_project.timagotchi.Leaderboard;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Leaderboard to rank students, both overall in a class and after a quiz
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
    Collections.sort(students, new Student.CompareByXp());
    return students;
  }
}
