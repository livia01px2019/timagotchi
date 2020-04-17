package edu.brown.cs.final_project.timagochi.users;

import java.util.List;

public interface People {

  String getUsername();

  void setUsername(String name);

  String getPassword();

  void setPassword(String password);

  void updateSQL(List<String> parameters);
}
