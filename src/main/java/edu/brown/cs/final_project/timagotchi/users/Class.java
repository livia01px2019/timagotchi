package edu.brown.cs.final_project.timagotchi.users;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.pets.Pet;

/**
 * A class that teachers can create for students to join and complete assignments.
 */
public class Class {
  private String id;
  private String name;
  private List<String> teacherIds;
  private List<String> studentIds;
  private List<String> assignmentIds;

  /**
   * Initializes a Class.
   *
   * @param i  The id of the class.
   * @param n  The name of the class.
   * @param ts The list containing just the id of the teacher who created the
   *           class.
   */
  public Class(String i, String n, List<String> ts) {
    id = i;
    name = n;
    teacherIds = ts;
    assignmentIds = new ArrayList<String>();
    studentIds = new ArrayList<String>();
  }

  /**
   * Getter for the list of student ids of the students in the class.
   *
   * @return The list of student ids of students in the class.
   */
  public List<String> getStudentIds() {
    return studentIds;
  }

  /**
   * Adds a student to the class by adding their student id to the list of student
   * ids.
   *
   * @param studentId The id of the student to be added to the class
   */
  public void addStudentId(String studentId) {
    this.studentIds.add(studentId);
  }

  /**
   * Getter for the list of teacher ids of the teachers who teach this class.
   *
   * @return The list of teacher ids of the teachers of the class.
   */
  public List<String> getTeacherIds() {
    return teacherIds;
  }

  /**
   * Adds a teacher to the class by adding their teacher id to the list of teacher
   * ids.
   *
   * @param teacherId The id of the teacher to be added to the class.
   */
  public void addTeacherId(String teacherId) {
    this.teacherIds.add(teacherId);
  }

  /**
   * Getter for the list of assigment ids for the class.
   *
   * @return The list of assignment ids for the class.
   */
  public List<String> getAssignmentIds() {
    return assignmentIds;
  }

  /**
   * Adds an assignment id to the class.
   *
   * @param assignment The assignment id to be added to the class.
   */
  public void addAssignmentId(String assignment) {
    this.assignmentIds.add(assignment);
  }

  /**
   * Comparator for comparing classes by the average xp of their students.
   */
  public static class CompareByAverageXp implements Comparator<Class> {
    /**
     * Empty constructor.
     */
    public CompareByAverageXp() {
    }

    @Override
    public int compare(Class c1, Class c2) {
      return Double.compare(c1.getAvgXp(), c2.getAvgXp());
    }
  }

  /**
   * Gets the average xp of a student in the class.
   *
   * @return The average xp of a student.
   */
  public double getAvgXp() {
    List<String> sIds = getStudentIds();
    double totXp = 0;

    // Get each student in each class, find their pet, and add its xp to the total
    // for that class.
    for (String tempId : sIds) {
      Student s = Controller.getStudent(tempId);
      Pet p = Controller.getPet(s.getPetId());
      totXp = totXp + p.getXp();
    }
    // Find average xp for each class.
    double avgXp1 = totXp / sIds.size();

    return avgXp1;
  }

  /**
   * Getter for the id of the class.
   *
   * @return The class id.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the id of the class.
   *
   * @param id The class id.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Getter for the name of the class.
   *
   * @return The class name.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for the name of the class.
   *
   * @param name The class name.
   */
  public void setName(String name) {
    this.name = name;
  }
}
