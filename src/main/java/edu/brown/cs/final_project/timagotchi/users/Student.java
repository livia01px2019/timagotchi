package edu.brown.cs.final_project.timagotchi.users;

import edu.brown.cs.final_project.timagotchi.pets.Pet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Student implements People {
  private String id;
  private String username;
  private String password;
  private String name;
  private List<String> classIds;
  private Pet pet;
  private Set<String> wrongQuestionIds;

  /**
   * Initializes a Student.
   *
   * @param i The id of the student.
   * @param user The username of the student.
   * @param pass The password of the student.
   * @param p The pet belonging the student.
   * @param n The name of the student.
   */
  public Student(String i, String user, String pass, Pet p, String n) {
    id = i;
    username = user;
    password = pass;
    pet = p;
    name = n;
    wrongQuestionIds = new HashSet<String>();
    classIds = new ArrayList<String>();
  }

  /**
   * Getter for the ids of all the questions the student has gotten wrong.
   *
   * @return The set of question ids of the questions the student got wrong.
   */
  public Set<String> getWrongQuestionIds() {
    return wrongQuestionIds;
  }

  /**
   * Adds the id of a question the student has gotten wrong.
   *
   * @param wrongQuestionId The id of the question the student got wrong.
   */
  public void addWrongQuestionId(String wrongQuestionId) {
    this.wrongQuestionIds.add(wrongQuestionId);
  }

  /**
   * Removes the id of a question the student got wrong at first but now got right.
   *
   * @param wrongQuestionId The id of the question the student originally got wrong.
   */
  public void removeWrongQuestionId(String wrongQuestionId) {
    this.wrongQuestionIds.remove(wrongQuestionId);
  }

  /**
   * Getter for the ids of all the classes the student belongs to.
   *
   * @return The list of class ids of the classes the student is in.
   */
  @Override
  public List<String> getClassIds() {
    return classIds;
  }

  /**
   * Adds the student to a class by adding the class id to the list of class ids.
   *
   * @param classId The id of the class that the student is being added to.
   */
  @Override
  public void addClassId(String classId) {
    this.classIds.add(classId);
  }

  @Override
  public void updateSQL(List<String> parameters) {
    // TODO Auto-generated method stub
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }
}
