package edu.brown.cs.final_project.timagotchi.users;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.pets.Pet;

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

  public static class CompareByAverageXp implements Comparator<Class> {
    public CompareByAverageXp() {
    }

    @Override
    public int compare(Class c1, Class c2) {
      List<String> sIds1 = c1.getStudentIds();
      List<String> sIds2 = c2.getStudentIds();
      double totXp1 = 0;
      double totXp2 = 0;

      // Get each student in each class, find their pet, and add its xp to the total for that class.
      for (String tempId : sIds1) {
        Student s = Controller.getStudent(tempId);
        Pet p = Controller.getPet(s.getPetId());
        totXp1 = totXp1 + p.getXp();
      }
      for (String tempId : sIds2) {
        Student s = Controller.getStudent(tempId);
        Pet p = Controller.getPet(s.getPetId());
        totXp2 = totXp2 + p.getXp();
      }

      // Find average xp for each class.
      double avgXp1 = totXp1 / sIds1.size();
      double avgXp2 = totXp2 / sIds2.size();

      return Double.compare(avgXp1, avgXp2);
    }
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
