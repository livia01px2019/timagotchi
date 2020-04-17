package edu.brown.cs.final_project.timagotchi.users;

import java.util.List;

public interface People {
  String getId();

  void setId(String id);

  String getName();

  void setName(String name);

  String getUsername();

  void setUsername(String username);

  String getPassword();

  void setPassword(String password);

  List<String> getClassIds();

  void addClassId(String classId);

  void updateSQL(List<String> parameters);
}
