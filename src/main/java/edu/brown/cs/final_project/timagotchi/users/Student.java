package edu.brown.cs.final_project.timagotchi.users;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.pets.Pet;
import edu.brown.cs.final_project.timagotchi.utils.PasswordHashing;

public class Student implements People {
  private String id;
  private String username;
  private String passwordHash;
  private String name;
  private List<String> classIds;
  private String petId;
  private List<HashSet<String>> wrongQuestionIds;

  /**
   * Initializes a Student.
   *
   * @param i    The id of the student.
   * @param user The username of the student.
   * @param pass The password of the student.
   * @param n    The name of the student.
   */
  public Student(String i, String user, String pass, String n) throws NoSuchAlgorithmException {
    id = i;
    username = user;
    passwordHash = pass;
    name = n;
    wrongQuestionIds = new ArrayList<HashSet<String>>();
    classIds = new ArrayList<String>();
  }

  /**
   * Getter for the ids of all the questions the student has gotten wrong for the
   * class with id cid.
   *
   * @param cid The id of the class that is wanted.
   * @return The set of question ids of the questions the student got wrong.
   */
  public Set<String> getWrongQuestionIds(String cid) {
    int classInd = classIds.indexOf(cid);
    return wrongQuestionIds.get(classInd);
  }

  /**
   * Adds the id of a question the student has gotten wrong for the class with id
   * cid.
   *
   * @param cid             The id of the class that is wanted.
   * @param wrongQuestionId The id of the question the student got wrong.
   */
  public void addWrongQuestionId(String wrongQuestionId, String cid) {
    int classInd = classIds.indexOf(cid);
    this.wrongQuestionIds.get(classInd).add(wrongQuestionId);
  }

  /**
   * Removes the id of a question the student got wrong at first but now got right
   * for the class with id cid.
   *
   * @param cid             The id of the class that is wanted.
   * @param wrongQuestionId The id of the question the student originally got
   *                        wrong.
   */
  public void removeWrongQuestionId(String wrongQuestionId, String cid) {
    int classInd = classIds.indexOf(cid);
    this.wrongQuestionIds.get(classInd).remove(wrongQuestionId);
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
    this.wrongQuestionIds.add(new HashSet<String>());
  }

  @Override
  public void updateSQL(List<String> parameters) {
    // Check if it exists in DB. If so, just update. Otherwise, add to database.
  }

  public String getPetId() {
    return petId;
  }

  public void setPetId(String petId) {
    this.petId = petId;
  }

  public static class CompareByXp implements Comparator<Student> {
    public CompareByXp() {
    }

    @Override
    public int compare(Student s1, Student s2) {
      Pet p1 = Controller.getPet(s1.getPetId());
      Pet p2 = Controller.getPet(s2.getPetId());
      return Double.compare(p1.getXp(), p2.getXp());
    }
  }

  public static class CompareByScore implements Comparator<Student> {
    private String assignmentID;

    public CompareByScore(String aid) {
      assignmentID = aid;
    }

    @Override
    public int compare(Student s1, Student s2) {
      Quiz a = (Quiz) Controller.getAssignment(assignmentID); // TODO: Fix here
      if (a.getComplete(s1.getId()) && a.getComplete(s2.getId())) {
        return Double.compare(a.getScore(s1.getId()) / a.getTotalScore(),
            a.getScore(s2.getId()) / a.getTotalScore());
      } else if (a.getComplete(s1.getId())) {
        return 1;
      } else if (a.getComplete(s2.getId())) {
        return -1;
      } else {
        return 0;
      }
    }
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
  public String getPassword() throws Exception {
    throw new Exception("Password is write only");
  }

  public Boolean verifyPassword(String pass) throws NoSuchAlgorithmException {
    return PasswordHashing.hashSHA256(pass).equals(this.passwordHash);
  }

  @Override
  public void setPassword(String hashedPassword) {
    passwordHash = hashedPassword;
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
}
