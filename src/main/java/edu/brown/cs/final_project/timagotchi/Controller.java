package edu.brown.cs.final_project.timagotchi;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Leaderboard;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.pets.Pet;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import edu.brown.cs.final_project.timagotchi.utils.PasswordHashing;

public class Controller {

  public static Assignment getAssignment(String assignmentId) {
    // TODO: getter for assignment, but I'm not sure if we need one for each different type? (checkoff, question, etc.)
    // Actually I think we do because each one has different variables that we need to query for
  }

  public static Leaderboard getLeaderboard(String boardId) {
    // TODO: getter for Leaderboard, same thing, I think we need one for each class and user b/c they contain different info??
  }

  /**
   * Get Pet from Database given its id.
   *
   * @param petId The id of the Pet.
   * @return The Pet stored in the database.
   */
  public static Pet getPet(String petId) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters("SELECT * FROM pets WHERE id=?;",
          new ArrayList<>(Arrays.asList(petId)));
      // TODO: Investigate if this will be a problem
      Pet p = new Pet(results.get(0).get(0), results.get(0).get(1), results.get(0).get(4));
      p.addXp(Double.parseDouble(results.get(0).get(2)));
      p.setLevel(Integer.parseInt(results.get(0).get(3)));
      return p;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Student from Database given its id.
   *
   * @param id The id of the Student.
   * @return The Student stored in the database.
   */
  public static Student getStudent(String id) {
    // TODO: add class ids, pet id, and wrong question ids
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM students WHERE id=?;", new ArrayList<>(Arrays.asList(id)));
      // TODO: Investigate if this will be a problem
      return new Student(results.get(0).get(0), results.get(0).get(1), results.get(0).get(2),
          results.get(0).get(3));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Teacher from Database given its id.
   *
   * @param id The id of the Teacher.
   * @return The Teacher stored in the database.
   */
  public static Teacher getTeacher(String id) {
    // TODO: Add class ids
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
              "SELECT * FROM teachers WHERE id=?;", new ArrayList<>(Arrays.asList(id)));
      // TODO: Investigate if this will be a problem
      return new Teacher(results.get(0).get(0), results.get(0).get(1), results.get(0).get(2),
              results.get(0).get(3));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Question from Database given its id.
   *
   * @param qid The id of the Question.
   * @return The Question stored in the database.
   */
  public static Question getQuestion(String qid) {
    try {
      List<List<String>> questions = DBProxy.executeQueryParameters(
          "SELECT * FROM questions WHERE id=?;", new ArrayList<>(Arrays.asList(qid)));
      // done in the following manner as there can be more than one answer
      List<String> answersID = new ArrayList<>();
      for (List<String> q : questions) {
        answersID.add(q.get(2));
      }
      // get all the multiple choice options to the answers
      List<List<String>> options = DBProxy.executeQueryParameters(
          "SELECT * FROM options WHERE questionID=?;", new ArrayList<>(Arrays.asList(qid)));
      List<String> choices = new ArrayList<>();
      // get the indexes of the options that are answers
      List<Integer> answerIndex = new ArrayList<>();
      for (int i = 0; i < options.size(); i++) {
        choices.add(options.get(i).get(2));
        for (String s : answersID) {
          // compare optionID with id of answers
          if (s.equals(options.get(i).get(0))) {
            answerIndex.add(i);
          }
        }
      }
      // TODO: Investigate if this will be a problem
      return new Question(questions.get(0).get(0), questions.get(0).get(1), choices, answerIndex);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get all the questions pertaining to a class with id classId.
   *
   * @param classId The id of the class to grab the questions from.
   * @return The list of all questions belonging to that class.
   */
  public static List<Question> getAllQuestions(String classId) {
    try {
      List<Question> allQuestions = new ArrayList<Question>();
      List<List<String>> assignments = DBProxy.executeQueryParameters(
          "SELECT assignmentID FROM class_assignment WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classId)));
      for (List<String> a : assignments) {
        String assignmentID = a.get(0);
        List<List<String>> questionsID = DBProxy.executeQueryParameters(
            "SELECT questionID FROM assignment_question WHERE assignmentID=?;",
            new ArrayList<>(Arrays.asList(assignmentID)));
        for (List<String> qid : questionsID) {
          allQuestions.add(getQuestion(qid.get(0)));
        }
      }
      // TODO: Investigate if this will be a problem
      return allQuestions;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Startup Command TODO: Run command to maintain foreign key.
   *
   * @param input The filename of the database to connect to.
   * @return Whether the database was successfully connected to.
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
   * Create Teacher Command
   *
   * @param input List of parameters separated by whitespace (username, password, name)
   * @return The teacher that was added
   */
  public Teacher createTeacherCommand(String input) {
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
   * Create Student Command
   *
   * @param input List of parameters separated by whitespace (username, password name)
   * @return The teacher that was added
   */
  public Student createStudentCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID studentID = UUID.randomUUID();
      String hashedPassword = PasswordHashing.hashSHA256(inputList[1]);
      DBProxy.updateQueryParameters("INSERT INTO students VALUES (?,?,?,?);", new ArrayList<>(
              Arrays.asList(studentID.toString(), inputList[0], hashedPassword, inputList[2])));
      return new Student(studentID.toString(), inputList[0], hashedPassword, inputList[2]);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create Class Command
   *
   * @param input List of parameters separated by whitespace (name, teacherId)
   * @return Class The class that was added
   */
  public Class createClassCommand(String input) {
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
   * Add Student To Class Command
   *
   * @param input List of parameters separated by whitespace (username, password, name, classIds)
   * @return The class that was just updated
   */
  public Class addStudentToClassCommand(String input) {
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
   * Get Class from Database given its id
   *
   * @param classID The id of the class to get
   * @return The class that was wanted
   */
  public static Class getClass(String classID) {
    // TODO: Add assignment ids and student ids
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
   *
   * @param input The id of the assignment
   * @return Whether the assignment addition went through successfully.
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
