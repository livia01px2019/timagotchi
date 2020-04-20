package edu.brown.cs.final_project.timagotchi.users;

import java.util.List;

/**
 * A generic People interface.
 */
public interface People {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  String getUsername();

  void setUsername(String username);

  String getPassword() throws Exception;

  void setPassword(String password);

  List<String> getClassIds();

  void addClassId(String classId);

  void updateSQL(List<String> parameters);
}
