package edu.brown.cs.final_project.timagotchi.users;

import java.util.List;

public class Student implements People {

  private String studentName;
  private String studentPassword;
  private String Class;
  private String Pet;
  // to be fixed upon implementation
  private List<Object> RightWrong;
  private List<Object> Assignments;

  public Student(String name, String pet) {
    studentName = name;
    Pet = pet;
  }

  @Override
  public String getUsername() {
    return studentName;
  }

  @Override
  public void setUsername(String name) {
    studentName = name;
  }

  @Override
  public String getPassword() {
    return studentPassword;
  }

  @Override
  public void setPassword(String password) {
    studentPassword = password;
  }

  @Override
  public void updateSQL(List<String> parameters) {
    // TODO Auto-generated method stub

  }

}
