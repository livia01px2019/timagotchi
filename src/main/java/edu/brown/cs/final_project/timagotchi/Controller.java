package edu.brown.cs.final_project.timagotchi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Classboard;
import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Checkoff;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.assignments.Review;
import edu.brown.cs.final_project.timagotchi.pets.Pet;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import edu.brown.cs.final_project.timagotchi.utils.PasswordHashing;

public class Controller {

  public static List<String> getWhatever(String studentID, String classID) {
    // TODO:
    return null;
  }

  // TODO: IDs not existing.

  public static Assignment getAssignment(String assignmentId) {
    // TODO: getter for assignment, but I'm not sure if we need one for each
    // different type? (checkoff, question, etc.)
    // Actually I think we do because each one has different variables that we need
    // to query for
    try {
      List<List<String>> assignments = DBProxy.executeQueryParameters(
          "SELECT * FROM assignments WHERE id=?;", new ArrayList<>(Arrays.asList(assignmentId)));
      // TODO: Investigate if this will be a problem
      if (assignments.get(0).get(2).equals("checkoff")) {
        Assignment a = new Checkoff(assignments.get(0).get(0), assignments.get(0).get(1),
            (int) Double.parseDouble(assignments.get(0).get(3)));
        List<List<String>> results = DBProxy.executeQueryParameters(
            "SELECT studentID,complete FROM student_assignment WHERE assignmentID=?",
            new ArrayList<>(Arrays.asList(assignmentId)));
        for (List<String> student : results) {
          a.setComplete(student.get(0), Boolean.parseBoolean(student.get(1)));
        }
        return a;
      } else if (assignments.get(0).get(2).equals("competitive")
          || assignments.get(0).get(2).equals("regular")) {
        // creates the list of questions, cacheable!
        List<List<String>> questionIDs = DBProxy.executeQueryParameters(
            "SELECT questionID FROM assignment_question WHERE assignmentID=?;",
            new ArrayList<>(Arrays.asList(assignmentId)));
        List<Question> convertedQ = new ArrayList<>();
        for (List<String> qid : questionIDs) {
          List<List<String>> questionAttributes = DBProxy.executeQueryParameters(
              "SELECT prompt,answerID FROM questions WHERE id=?;",
              new ArrayList<>(Arrays.asList(qid.get(0))));
          List<List<String>> options = DBProxy.executeQueryParameters(
              "SELECT option,id FROM options WHERE questionID=?;",
              new ArrayList<>(Arrays.asList(qid.get(0))));
          List<String> questionOptions = new ArrayList<>();
          List<Integer> answerIndex = new ArrayList<>();
          int i = 0;
          for (List<String> o : options) {
            questionOptions.add(o.get(0));
            if (o.get(1).equals(questionAttributes.get(0).get(1))) {
              answerIndex.add(i);
            }
            i += 1;
          }
          Question fullQuestion = new Question(qid.get(0), questionAttributes.get(0).get(0),
              questionOptions, answerIndex);
          convertedQ.add(fullQuestion);
        }
        Quiz convertedQuiz = null;
        // create quiz object
        if (assignments.get(0).get(2).equals("competitive")) {
          convertedQuiz = new Quiz(assignments.get(0).get(0), assignments.get(0).get(1),
              (int) Double.parseDouble(assignments.get(0).get(3)), convertedQ, true);
        } else {
          convertedQuiz = new Quiz(assignments.get(0).get(0), assignments.get(0).get(1),
              (int) Double.parseDouble(assignments.get(0).get(3)), convertedQ, false);
        }
        // for record
        for (int i = 0; i < convertedQ.size(); i++) {
          List<List<String>> record = DBProxy.executeQueryParameters(
              "SELECT studentID,record FROM student_record WHERE questionID=?;",
              new ArrayList<>(Arrays.asList(convertedQ.get(i).getId())));
          for (List<String> r : record) {
            convertedQuiz.setRecord(r.get(0), i, Boolean.parseBoolean(r.get(1)));
          }
        }
        // for complete
        List<List<String>> complete = DBProxy.executeQueryParameters(
            "SELECT studentID,complete FROM student_assignment WHERE assignmentID=?;",
            new ArrayList<>(Arrays.asList(assignmentId)));
        for (List<String> comp : complete) {
          convertedQuiz.setComplete(comp.get(0), Boolean.parseBoolean(comp.get(1)));
        }
        return convertedQuiz;
      } else if (assignments.get(0).get(2).equals("review")) {
        Review r = new Review(assignments.get(0).get(0), assignments.get(0).get(1),
            (int) Double.parseDouble(assignments.get(0).get(3)));
        return r;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Classboard getLeaderboard(List<String> classIDs) {
    return new Classboard(classIDs);
  }

  public static Userboard getLeaderboard(String classID) {
    return new Userboard(classID);
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
      Pet p = new Pet(results.get(0).get(0), results.get(0).get(1));
      p.updateSprite(results.get(0).get(4));
      p.addXp(Double.parseDouble(results.get(0).get(2)));
      p.setLevel(Integer.parseInt(results.get(0).get(3)));
      return p;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add pet to student.
   *
   * @param input studentID, pet name
   * @return
   */
  public static Pet addPet(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID petID = UUID.randomUUID();
      Pet p = new Pet(petID.toString(), inputList[1]);
      DBProxy.updateQueryParameters("INSERT INTO pets VALUES (?,?,?,?,?);",
          new ArrayList<>(Arrays.asList(petID.toString(), inputList[1], "0", "1", p.getImage())));
      DBProxy.updateQueryParameters("INSERT INTO student_pet VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList[0], petID.toString())));
      return p;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Complete Assignment for Student.
   *
   * @param inputList [studentID, assignmentID]
   * @return
   */
  public static Assignment completeAssignment(String[] inputList) {
    try {
      Assignment a = getAssignment(inputList[1]);
      a.setComplete(inputList[0], true);
      DBProxy.updateQueryParameters(
          "UPDATE student_assignment SET complete=? WHERE studentID=? AND assignmentID=?;",
          new ArrayList<>(Arrays.asList("true", inputList[0], inputList[1])));
      return a;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Uncomplete Assignment for Student.
   *
   * @param inputList [studentID, assignmentID]
   * @return
   */
  public static Assignment uncompleteAssignment(String[] inputList) {
    // TODO: implement- remember to update DB
    return null;
  }

  /**
   * Add Student Record (Quiz) to Student.
   *
   * @param studentID
   * @param assignmentID
   * @param inputList    List of True/Falses
   * @return
   */
  public static Assignment addStudentRecord(String studentID, String assignmentID, String classID,
      List<String> inputList) {
    try {
      Quiz a = (Quiz) getAssignment(assignmentID); // TODO: to be fixed later
      List<List<String>> complete = DBProxy.executeQueryParameters(
          "SELECT complete FROM student_assignment WHERE studentID=? AND assignmentID=?;",
          new ArrayList<>(Arrays.asList(studentID, assignmentID)));
      if (!Boolean.parseBoolean(complete.get(0).get(0))) {
        for (int i = 0; i < inputList.size(); i++) {
          a.setRecord(studentID, i, Boolean.parseBoolean(inputList.get(i)));
          List<List<String>> questions = DBProxy.executeQueryParameters(
              "SELECT questionID FROM assignment_question WHERE assignmentID=?;",
              new ArrayList<>(Arrays.asList(assignmentID)));
          for (List<String> q : questions) {
            DBProxy.updateQueryParameters("INSERT INTO student_record VALUES (?,?,?,?);",
                new ArrayList<>(Arrays.asList(studentID, q.get(0), inputList.get(i), classID)));
          }
        }
        // update student status as complete
        completeAssignment(new String[] {
            studentID, assignmentID
        });
        // update pet xp
        Student s = getStudent(studentID);
        String p = s.getPetId();
        double xp = a.getReward(); // * (a.getScore(studentID) / a.getTotalScore());
        DBProxy.updateQueryParameters("UPDATE pets SET xp=? WHERE id=?;",
            new ArrayList<>(Arrays.asList("" + xp, p)));
      }
      return a;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Student password from database given a username.
   *
   * @param username The username of the Student
   * @return The password corresponding to that Username
   */
  public static String getStudentPassword(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT passwordHash FROM students WHERE username=?;",
          new ArrayList<>(Arrays.asList(username)));
      if (results == null || results.isEmpty()) {
        return "";
      } else {
        return results.get(0).get(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Teacher password from database given a username.
   *
   * @param username The username of the Teacher
   * @return The password corresponding to that Teacher
   */
  public static String getTeacherPassword(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT passwordHash FROM teachers WHERE username=?;",
          new ArrayList<>(Arrays.asList(username)));
      if (results == null || results.isEmpty()) {
        return "";
      } else {
        return results.get(0).get(0);
      }
    } catch (Exception e) {
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
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM students WHERE id=?;", new ArrayList<>(Arrays.asList(id)));
      // TODO: Investigate if this will be a problem
      Student s = new Student(results.get(0).get(0), results.get(0).get(1), results.get(0).get(2),
          results.get(0).get(3));
      // add class IDs
      List<List<String>> classes = DBProxy.executeQueryParameters(
          "SELECT classID FROM class_student WHERE studentID=?;",
          new ArrayList<>(Arrays.asList(id)));
      for (List<String> c : classes) {
        s.addClassId(c.get(0));
      }
      // add wrong question IDs
      List<List<String>> wrongs = DBProxy.executeQueryParameters(
          "SELECT questionID,classID FROM student_record WHERE studentID=? AND record=\"wrong\";",
          new ArrayList<>(Arrays.asList(id)));
      for (List<String> w : wrongs) {
        s.addWrongQuestionId(w.get(0), w.get(1));
      }
      // add pet ID
      List<List<String>> pets = DBProxy.executeQueryParameters(
          "SELECT petID FROM student_pet WHERE studentID=?;", new ArrayList<>(Arrays.asList(id)));
      s.setPetId(pets.get(0).get(0)); // TODO: Pets for different class?
      return s;
    } catch (Exception e) {
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
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM teachers WHERE id=?;", new ArrayList<>(Arrays.asList(id)));
      // TODO: Investigate if this will be a problem
      Teacher t = new Teacher(results.get(0).get(0), results.get(0).get(1), results.get(0).get(2),
          results.get(0).get(3));
      // add class IDs
      List<List<String>> classes = DBProxy.executeQueryParameters(
          "SELECT classID FROM teacher_classes WHERE teacherID=?;",
          new ArrayList<>(Arrays.asList(id)));
      for (List<String> c : classes) {
        t.addClassId(c.get(0));
      }
      return t;
    } catch (Exception e) {
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
   * @param input List of parameters separated by whitespace (username, password,
   *              name)
   * @return The teacher that was added
   */
  public static Teacher createTeacherCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      // TODO return null if username is already taken
      UUID teacherID = UUID.randomUUID();
      String hashedPassword = PasswordHashing.hashSHA256(inputList[1]);
      DBProxy.updateQueryParameters("INSERT INTO teachers VALUES (?,?,?,?);", new ArrayList<>(
          Arrays.asList(teacherID.toString(), inputList[0], hashedPassword, inputList[2])));
      return new Teacher(teacherID.toString(), inputList[0], hashedPassword, inputList[2]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create Class Command
   *
   * @param inputList List of parameters separated by whitespace (name, teacherId)
   * @return Class The class that was added
   */
  public static Class createClassCommand(String[] inputList) {
    try {
      UUID classID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID.toString(), inputList[0])));
      DBProxy.updateQueryParameters("INSERT INTO teacher_classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList[1], classID.toString())));
      return new Class(classID.toString(), inputList[0],
          new ArrayList<>(Arrays.asList(inputList[1])));
    } catch (Exception e) {
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
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT p1.id,p1.name,p2.teacherID FROM classes AS p1, teacher_classes AS p2 WHERE id=? AND p1.id = p2.classID;",
          new ArrayList<>(Arrays.asList(classID)));
      List<String> teacherIDs = new ArrayList<>();
      for (List<String> s : results) {
        teacherIDs.add(s.get(2));
      }
      // TODO: Investigate if this will be a problem
      System.out.println(results);
      Class c = new Class(results.get(0).get(0), results.get(0).get(1), teacherIDs);
      List<List<String>> assignments = DBProxy.executeQueryParameters(
          "SELECT assignmentID FROM class_assignment WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classID)));
      for (List<String> a : assignments) {
        c.addAssignmentId(a.get(0));
      }
      List<List<String>> studentIDs = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classID)));
      for (List<String> s : studentIDs) {
        c.addStudentId(s.get(0));
      }
      return c;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Check whether a classID is valid.
   *
   * @param input
   * @return
   */
  public static Boolean checkValidClassID(String input) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM classes WHERE id=?;", new ArrayList<>(Arrays.asList(input)));
      if (results.size() != 0) {
        return true;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Student ID To Class Command
   *
   * @param inputList List of parameters (classID studentID)
   * @return The class that was just updated
   */
  public static Class addStudentIDToClassCommand(String[] inputList) {
    try {
      DBProxy.updateQueryParameters("INSERT INTO class_student VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList[0], inputList[1])));
      Class c = getClass(inputList[0]);
      c.addStudentId(inputList[1]);
      return c;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create Student Command
   *
   * @param input List of parameters separated by whitespace (username, password,
   *              name)
   * @return The student that was added
   */
  public static Student createStudentCommand(String input) {
    String[] inputList = input.split(" ");
    try {
      // TODO return null if username is already taken
      UUID studentID = UUID.randomUUID();
      String hashedPassword = PasswordHashing.hashSHA256(inputList[1]);
      DBProxy.updateQueryParameters("INSERT INTO students VALUES (?,?,?,?);", new ArrayList<>(
          Arrays.asList(studentID.toString(), inputList[0], hashedPassword, inputList[2])));
      Pet p = addPet(studentID.toString() + " aaa"); // TODO: Change for pet name
      Student s = new Student(studentID.toString(), inputList[0], hashedPassword, inputList[2]);
      s.setPetId(p.getId());
      return s;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add assignment to student
   *
   * @param input StudentID, AssignmentID
   */
  public static void addAssignmentToStudent(String input) {
    String[] inputList = input.split(" ");
    try {
      DBProxy.updateQueryParameters("INSERT INTO student_assignment VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(inputList[0], inputList[1], "false")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes an assignment.
   *
   * @param assignmentID
   */
  public static void deleteAssignment(String assignmentID) {
    try {
      DBProxy.updateQueryParameters("DELETE FROM assignments WHERE id=?",
          new ArrayList<>(Arrays.asList(assignmentID)));
      // TODO delete all questions from questions table
      DBProxy.updateQueryParameters("DELETE FROM assignment_question WHERE assignmentID=?",
          new ArrayList<>(Arrays.asList(assignmentID)));
      DBProxy.updateQueryParameters("DELETE FROM class_assignment WHERE assignmentID=?",
          new ArrayList<>(Arrays.asList(assignmentID)));
      DBProxy.updateQueryParameters("DELETE FROM student_assignment WHERE assignmentID=?",
          new ArrayList<>(Arrays.asList(assignmentID)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Add Checkoff Assignment Command
   *
   * @param inputList The classID, name and xp reward
   * @return The Checkoff that was added
   */
  public static Checkoff addCheckoffAssignment(List<String> inputList) {
    try {
      UUID assignmentID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);", new ArrayList<>(
          Arrays.asList(assignmentID.toString(), inputList.get(1), "checkoff", inputList.get(2))));
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList.get(0), assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(inputList.get(0))));
      Checkoff c = new Checkoff(assignmentID.toString(), inputList.get(1),
          Integer.parseInt(inputList.get(2)));
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0) + " " + assignmentID.toString());
        c.setComplete(student.get(0), false);
      }
      return c;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Question.
   *
   * @param inputList (Prompt, Option1, Option2, Option3, Option4, AnswerIndex)
   * @return
   */
  public static Question addQuestion(List<String> inputList) {
//    String[] inputList = input.split(" ");
//    System.out.println(inputList.length);
    try {
      UUID questionID = UUID.randomUUID();
      UUID answer1ID = UUID.randomUUID();
      UUID answer2ID = UUID.randomUUID();
      UUID answer3ID = UUID.randomUUID();
      UUID answer4ID = UUID.randomUUID();
      List<String> uuids = new ArrayList<>(Arrays.asList(answer1ID.toString(), answer2ID.toString(),
          answer3ID.toString(), answer4ID.toString()));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);", new ArrayList<>(
          Arrays.asList(answer1ID.toString(), questionID.toString(), inputList.get(1))));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);", new ArrayList<>(
          Arrays.asList(answer2ID.toString(), questionID.toString(), inputList.get(2))));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);", new ArrayList<>(
          Arrays.asList(answer3ID.toString(), questionID.toString(), inputList.get(3))));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);", new ArrayList<>(
          Arrays.asList(answer4ID.toString(), questionID.toString(), inputList.get(4))));
      DBProxy.updateQueryParameters("INSERT INTO questions VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(questionID.toString(), inputList.get(0),
              uuids.get(Integer.parseInt(inputList.get(5))))));
      Question q = new Question(questionID.toString(), inputList.get(0), uuids,
          new ArrayList<>(Arrays.asList(Integer.parseInt(inputList.get(5)))));
      return q;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Review Assignment Command
   *
   * @param input The classID, name and xp reward
   * @return The Review that was added
   */
  public static Review addReviewAssignment(String input) {
    String[] inputList = input.split(" ");
    try {
      UUID assignmentID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);", new ArrayList<>(
          Arrays.asList(assignmentID.toString(), inputList[1], "review", inputList[2])));
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList[0], assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(inputList[0])));
      Review r = new Review(assignmentID.toString(), inputList[1], Integer.parseInt(inputList[2]));

      // add assignment to each student in the class, and set the complete to false
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0) + " " + assignmentID.toString());
        r.setComplete(student.get(0), false);
      }
      return r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Not Competitive Quiz Assignment Command, without allocating students.
   *
   * @param inputList ClassID, name, xp, String for competitive / false for
   *                  regular, [questionIDs]
   * @return
   */
  public static Quiz addQuizAssignment(List<String> inputList) {
    try {
      UUID assignmentID = UUID.randomUUID();
      if (inputList.get(3).equals("competitive")) {
        DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);",
            new ArrayList<>(Arrays.asList(assignmentID.toString(), inputList.get(1), "competitive",
                inputList.get(2))));
      } else {
        DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);", new ArrayList<>(
            Arrays.asList(assignmentID.toString(), inputList.get(1), "regular", inputList.get(2))));
      }
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(inputList.get(0), assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(inputList.get(0))));

      // input question ID and add to the map
      List<String> questionIDs = new ArrayList<>();
      for (int i = 4; i < inputList.size(); i++) {
        questionIDs.add(inputList.get(i));
        DBProxy.updateQueryParameters("INSERT INTO assignment_question VALUES (?,?);",
            new ArrayList<>(Arrays.asList(assignmentID.toString(), inputList.get(i))));
      }
      // create list of questions
      List<Question> convertedQ = new ArrayList<>();
      for (String qid : questionIDs) {
        List<List<String>> questionAttributes = DBProxy.executeQueryParameters(
            "SELECT prompt,answerID FROM questions WHERE id=?;",
            new ArrayList<>(Arrays.asList(qid)));
        List<List<String>> options = DBProxy.executeQueryParameters(
            "SELECT option,id FROM options WHERE questionID=?;",
            new ArrayList<>(Arrays.asList(qid)));
        List<String> questionOptions = new ArrayList<>();
        int index = 0;
        for (int o = 0; o < options.size(); o++) {
          questionOptions.add(options.get(o).get(0));
          if (options.get(o).get(1).equals(questionAttributes.get(0).get(1))) {
            index = o;
          }
        }
        List<Integer> answerIndex = new ArrayList<>(Arrays.asList(index));
        Question fullQuestion = new Question(qid, questionAttributes.get(0).get(0), questionOptions,
            answerIndex);
        convertedQ.add(fullQuestion);
      }
      Quiz q = null;
      // create complete assignment object
      if (inputList.get(3).equals("competitive")) {
        q = new Quiz(assignmentID.toString(), inputList.get(1), Integer.parseInt(inputList.get(2)),
            convertedQ, true);
      } else {
        q = new Quiz(assignmentID.toString(), inputList.get(1), Integer.parseInt(inputList.get(2)),
            convertedQ, false);
      }
      // add assignment to each student in the class, and set the complete to false
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0) + " " + assignmentID.toString());
        q.setComplete(student.get(0), false);
      }
      return q;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getStudentIDFromUsername(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT id FROM students WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      return results.get(0).get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getTeacherIDFromUsername(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT id FROM teachers WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      return results.get(0).get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Accessor method for all class IDs
   *
   * @return
   */
  public static List<String> getAllClassIds() {
    try {
      List<List<String>> results = DBProxy.executeQuery("SELECT id FROM classes;");
      List<String> ids = new ArrayList<String>();
      for (List<String> r : results) {
        ids.add(r.get(0));
      }
      return ids;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
