package edu.brown.cs.final_project.timagotchi;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import edu.brown.cs.final_project.timagotchi.utils.PasswordHashing;

public class Controller {

  /**
   * Startup Command TODO: Run command to maintain foreign key.
   *
   * @throws NoSuchAlgorithmException
   */
  public Boolean startUpCommand(String input) {
    try {
      DBProxy.connect(input);
      if (DBProxy.isConnected()) {
        System.out.println("Connected to: " + input);
        return true;
      } else {
        System.out.println("ERROR: Not connected to: " + input);
        return false;
      }
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Add Teacher Command
   */
  public Teacher addTeacherCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID teacherID = UUID.randomUUID();
      String hashedPassword = PasswordHashing.hashSHA256(inputList[1]);
      DBProxy.updateQueryParameters("INSERT INTO teachers VALUES (?,?,?,?);", new ArrayList<>(
          Arrays.asList(teacherID.toString(), inputList[0], hashedPassword, inputList[2])));
      return new Teacher(teacherID.toString(), inputList[0], hashedPassword, inputList[2]);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Class Command
   */
  public Class addClassCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID classID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID.toString(), inputList[0])));
      DBProxy.updateQueryParameters("INSERT INTO teacher_classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList[1], classID.toString())));
      return new Class(classID.toString(), inputList[0],
          new ArrayList<>(Arrays.asList(inputList[1])));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Student Command
   */
  public Class addStudentCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID studentID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO students VALUES (?,?,?,?);",
          new ArrayList<>(Arrays.asList(studentID.toString(), inputList[0],
              PasswordHashing.hashSHA256(inputList[1]), inputList[2])));
      Class c = getClass(inputList[3]);
      c.addStudentId(studentID.toString());
      return c;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Class from Database
   */
  public Class getClass(String classID) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT p1.id,p1.name,p2.teacherID FROM classes AS p1, teacher_classes AS p2 WHERE id=? AND p1.id = p2.classID;",
          new ArrayList<>(Arrays.asList(classID)));
      List<String> teacherIDs = new ArrayList<>();
      for (List<String> s : results) {
        teacherIDs.add(s.get(2));
      }
      // TODO: Investigate if this will be a problem
      return new Class(results.get(0).get(0), results.get(0).get(1), teacherIDs);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Assignment Command
   */
  public Boolean addAssignmentCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      return DBProxy.updateQueryParameters("INSERT INTO assignments (type) VALUES (?);",
          new ArrayList<>(Arrays.asList(inputList[0])));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

}
