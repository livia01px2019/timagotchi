package edu.brown.cs.final_project.timagotchi.users;

import java.util.List;

/**
 * A generic People interface.
 */
public interface People {
  /**
   * Getter for the id of the person.
   *
   * @return The person id.
   */
  String getId();

  /**
   * Setter for the id of the person.
   *
   * @param id The person id.
   */
  void setId(String id);

  /**
   * Getter for the name of the person.
   *
   * @return The person's name.
   */
  String getName();

  /**
   * Setter for the name of the person.
   *
   * @param name The person's name.
   */
  void setName(String name);

  /**
   * Getter for the username of the person.
   *
   * @return The person's username.
   */
  String getUsername();

  /**
   * Setter for the username of the person.
   *
   * @param username The person's username.
   */
  void setUsername(String username);

  /**
   * Getter for the password of the person.
   *
   * @return The person's password.
   * @throws Exception The exception thrown.
   */
  String getPassword() throws Exception;

  /**
   * Setter for the password of the person.
   *
   * @param password The person's password.
   */
  void setPassword(String password);

  /**
   * Getter for the ids of the classes that the person belongs to.
   *
   * @return The class ids the person belongs to.
   */
  List<String> getClassIds();

  /**
   * Adds the id of a class the person belongs to.
   *
   * @param classId The id of the class to be added.
   */
  void addClassId(String classId);

}
