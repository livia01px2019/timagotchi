package edu.brown.cs.final_project.timagochi.users;

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
    // TODO Auto-generated method stub
    return studentName;
  }

  @Override
  public void setUsername(String name) {
    // TODO Auto-generated method stub
    studentName = name;
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return studentPassword;
  }

  @Override
  public void setPassword(String password) {
    // TODO Auto-generated method stub
    studentPassword = password;
  }

  @Override
  public void updateSQL(List<String> parameters) {
    // TODO Auto-generated method stub

  }

}
