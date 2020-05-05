package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * Class that has all the functions retrieving things from the backend for the
 * frontend.
 */
public final class Accessors {
  private static final int ASCII_OFFSET = 65;

  private Accessors() {
  }

  /**
   * Get the option that the student picked for a specific question.
   *
   * @param studentID  The id of the student.
   * @param questionID The id of the question.
   * @return A string showing the option the student picked (as text)
   */
  public static String getRecord(String studentID, String questionID) {
    try {
      List<List<String>> response = DBProxy.executeQueryParameters(
          "SELECT optionID from student_record WHERE studentID=? AND questionID=?;",
          new ArrayList<>(Arrays.asList(studentID, questionID)));
      if (response.size() != 0) {
        int optionIndex = response.get(0).get(0).charAt(0) - ASCII_OFFSET;
        List<List<String>> options = DBProxy.executeQueryParameters(
            "SELECT option FROM options WHERE questionID=?;",
            new ArrayList<>(Arrays.asList(questionID)));
        if (options.size() != 0) {
          return options.get(optionIndex).get(0);
        }
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns all the ids of questions a student got wrong given student ID and
   * class ID.
   *
   * @param studentID The id of the student.
   * @param classID   The id of the class.
   * @return List of string of question ids
   */
  public static List<String> getWrongQuestionIDs(String studentID, String classID) {
    List<String> allWrongs = new ArrayList<>();
    try {
      List<List<String>> assignments = DBProxy.executeQueryParameters(
          "SELECT questionID from student_record WHERE "
              + "studentID=? AND classID=? AND record=\"false\";",
          new ArrayList<>(Arrays.asList(studentID, classID)));
      for (List<String> a : assignments) {
        allWrongs.add(a.get(0));
      }
      return allWrongs;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns Assignment of correct type given assignmentID.
   *
   * @param assignmentId The id of the assignment.
   * @return Assignment of correct type and info from DB
   */
  public static Assignment getAssignment(String assignmentId) {
    try {
      List<List<String>> assignments = DBProxy.executeQueryParameters(
          "SELECT * FROM assignments WHERE id=?;", new ArrayList<>(Arrays.asList(assignmentId)));
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
        // for complete
        List<List<String>> complete = DBProxy.executeQueryParameters(
            "SELECT studentID,complete FROM student_assignment WHERE assignmentID=?;",
            new ArrayList<>(Arrays.asList(assignmentId)));
        for (List<String> comp : complete) {
          r.setComplete(comp.get(0), Boolean.parseBoolean(comp.get(1)));
        }
        return r;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns Userboard of every student in a class given a classID.
   *
   * @param classID The id of the class.
   * @return The Userboard wanted.
   */
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
   * Get Student password from database given a username.
   *
   * @param username The username of the Student.
   * @return The password corresponding to that Username.
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
   * @param username The username of the Teacher.
   * @return The password corresponding to that Teacher.
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
      s.setPetId(pets.get(0).get(0));
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
      return allQuestions;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get Class from Database given its id.
   *
   * @param classID The id of the class to get
   * @return The class that was wanted
   */
  public static Class getClass(String classID) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT p1.id,p1.name,p2.teacherID FROM classes "
              + "AS p1, teacher_classes AS p2 WHERE id=? AND p1.id = p2.classID;",
          new ArrayList<>(Arrays.asList(classID)));
      List<String> teacherIDs = new ArrayList<>();
      for (List<String> s : results) {
        teacherIDs.add(s.get(2));
      }

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
   * @param classID The classID.
   * @return True if classID is valid, False otherwise.
   */
  public static Boolean checkValidClassID(String classID) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM classes WHERE id=?;", new ArrayList<>(Arrays.asList(classID)));
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
   * Get the studentID from a username.
   *
   * @param username The username.
   * @return The student ID, if found. Returns null otherwise.
   */
  public static String getStudentIDFromUsername(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT id FROM students WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      if (results.size() != 0) {
        return results.get(0).get(0);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the teacherID from a username.
   *
   * @param username The username.
   * @return The teacher ID, if found. Returns null otherwise.
   */
  public static String getTeacherIDFromUsername(String username) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT id FROM teachers WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      if (results.size() != 0) {
        return results.get(0).get(0);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Accessor method for all class IDs.
   *
   * @return The list of all class IDs.
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

  /**
   * Accessor method for all students who are assigned to an assignment with id assignmentIDs.
   *
   * @param assignmentID The id of the assignment.
   * @return The list of student ids that have been allocated this assignment.
   */
  public static List<String> getStudentsFromAssignment(String assignmentID) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT DISTINCT studentID FROM student_assignment WHERE assignmentID=?;",
          new ArrayList<>(Arrays.asList(assignmentID)));
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
