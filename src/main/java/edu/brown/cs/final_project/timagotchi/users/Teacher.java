package edu.brown.cs.final_project.timagotchi.users;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the Teacher user.
 */
public class Teacher implements People {
  private String id;
  private String username;
  private String password;
  private String name;
  private List<String> classIds;

  /**
   * Initializes a Teacher.
   *
   * @param i The id of the teacher.
   * @param user The username of the teacher.
   * @param pass The password of the teacher.
   * @param n The name of the teacher.
   */
  public Teacher(String i, String user, String pass, String n) {
    id = i;
    username = user;
    password = pass;
    name = n;
    classIds = new ArrayList<String>();
  }

  /**
   * Getter for the ids of all the classes the teacher teaches.
   *
   * @return The list of class ids of the classes the teacher is in.
   */
  @Override
  public List<String> getClassIds() {
    return classIds;
  }

  /**
   * Adds the teacher to a class by adding the class id to the list of class ids.
   *
   * @param classId The id of the class that the teacher is creating.
   */
  @Override
  public void addClassId(String classId) {
    this.classIds.add(classId);
  }

  @Override
  public void updateSQL(List<String> parameters) { }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
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
}
