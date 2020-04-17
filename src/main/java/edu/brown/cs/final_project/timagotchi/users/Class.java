package edu.brown.cs.final_project.timagotchi.users;

import edu.brown.cs.final_project.timagotchi.assignments.Assignment;

import java.util.ArrayList;
import java.util.List;

public class Class {
  private String id;
  private String name;
  private List<String> teacherIds;
  private List<String> studentIds;
  private List<Assignment> assignments;

  /**
   * Initializes a Class.
   *
   * @param i The id of the class.
   * @param n The name of the class.
   * @param ts The list containing just the id of the teacher who created the class.
   */
  public Class(String i, String n, List<String> ts) {
    id = i;
    name = n;
    teacherIds = ts;
    assignments = new ArrayList<Assignment>();
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
   * Adds a student to the class by adding their student id to the list of student ids.
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
   * Adds a teacher to the class by adding their teacher id to the list of teacher ids.
   *
   * @param teacherId The id of the teacher to be added to the class.
   */
  public void addTeacherId(String teacherId) {
    this.teacherIds.add(teacherId);
  }

  /**
   * Getter for the list of assigments for the class.
   *
   * @return The list of assignment for the class.
   */
  public List<Assignment> getAssignments() {
    return assignments;
  }

  /**
   * Adds an assignment to the class.
   *
   * @param assignment The assignment to be added to the class.
   */
  public void addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
