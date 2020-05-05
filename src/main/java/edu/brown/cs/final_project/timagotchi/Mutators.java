package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

/**
 * Class that has all the functions to modify things in the backend from the
 * frontend.
 */
public final class Mutators {

  private Mutators() {
  }

  /**
   * Create Teacher Command.
   *
   * @param username the new account username
   * @param password the new account password, unhashed
   * @param name     the new account name
   * @return Teacher The teacher that was added
   */
  public static Teacher createTeacherCommand(String username, String password, String name) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM teachers WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      if (results.size() == 0) {
        UUID teacherID = UUID.randomUUID();
        String hashedPassword = PasswordHashing.hashSHA256(password);
        DBProxy.updateQueryParameters("INSERT INTO teachers VALUES (?,?,?,?);",
            new ArrayList<>(Arrays.asList(teacherID.toString(), username, hashedPassword, name)));
        return new Teacher(teacherID.toString(), username, hashedPassword, name);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create Class Command.
   *
   * @param name      The name of the new account.
   * @param teacherID The id of the teacher.
   * @return Class The class that was added.
   */

  public static Class createClassCommand(String name, String teacherID) {
    try {
      UUID classID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID.toString(), name)));
      DBProxy.updateQueryParameters("INSERT INTO teacher_classes VALUES (?,?);",
          new ArrayList<>(Arrays.asList(teacherID, classID.toString())));
      return new Class(classID.toString(), name, new ArrayList<>(Arrays.asList(teacherID)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Adds pet to database.
   *
   * @param studentID The id of the student.
   * @param petName   The name of the pet.
   * @return The Pet that was added.
   */
  public static Pet addPet(String studentID, String petName) {
    try {
      UUID petID = UUID.randomUUID();
      Pet p = new Pet(petID.toString(), petName);
      if (petName.equals("TimNelson")) {
        p.updateSprite("../img/tim.jpg");
      }
      DBProxy.updateQueryParameters("INSERT INTO pets VALUES (?,?,?,?,?);",
          new ArrayList<>(Arrays.asList(petID.toString(), petName, "0", "1", p.getImage())));
      DBProxy.updateQueryParameters("INSERT INTO student_pet VALUES (?,?);",
          new ArrayList<>(Arrays.asList(studentID, petID.toString())));
      return p;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Student Record (Quiz) to Student.
   *
   * @param studentID    The id of the student.
   * @param assignmentID The id of the assignment.
   * @param classID      The id of the class.
   * @param answers      The list of answers the student gave.
   * @param inputList    List of True/Falses for students' answers where true
   *                     means correct and false means incorrect
   * @return Assignment that student record has been added to
   */
  public static Assignment addStudentRecord(String studentID, String assignmentID, String classID,
      List<String> answers, List<String> inputList) {
    try {
      Quiz a = (Quiz) Accessors.getAssignment(assignmentID); // TODO: Fix here
      List<List<String>> complete = DBProxy.executeQueryParameters(
          "SELECT complete FROM student_assignment WHERE studentID=? AND assignmentID=?;",
          new ArrayList<>(Arrays.asList(studentID, assignmentID)));
      if (!Boolean.parseBoolean(complete.get(0).get(0))) {
        for (int i = 0; i < inputList.size(); i++) {
          a.setRecord(studentID, i, Boolean.parseBoolean(inputList.get(i)));
          List<List<String>> questions = DBProxy.executeQueryParameters(
              "SELECT questionID FROM assignment_question WHERE assignmentID=?;",
              new ArrayList<>(Arrays.asList(assignmentID)));
          DBProxy.updateQueryParameters("INSERT INTO student_record VALUES (?,?,?,?,?);",
              new ArrayList<>(Arrays.asList(studentID, questions.get(i).get(0), answers.get(i),
                  inputList.get(i), classID)));
        }
        // update student status as complete
        Mutators.completeAssignment(studentID, assignmentID);
      }
      return a;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Complete Assignment for Student.
   *
   * @param studentID    The id of the student.
   * @param assignmentID The id of the assignment.
   * @return Assignment that has been set to complete
   */
  public static Assignment completeAssignment(String studentID, String assignmentID) {
    try {
      Assignment a = Accessors.getAssignment(assignmentID);
      a.setComplete(studentID, true);
      DBProxy.updateQueryParameters(
          "UPDATE student_assignment SET complete=? WHERE studentID=? AND assignmentID=?;",
          new ArrayList<>(Arrays.asList("true", studentID, assignmentID)));
      // update pet xp
      Student s = Accessors.getStudent(studentID);
      String p = s.getPetId();
      double xp = a.getReward() + Accessors.getPet(p).getXp(); // (a.getScore(studentID) /
                                                                // a.getTotalScore());
      DBProxy.updateQueryParameters("UPDATE pets SET xp=? WHERE id=?;",
          new ArrayList<>(Arrays.asList("" + xp, p)));
      return a;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Uncomplete Assignment for Student.
   *
   * @param studentID    The id of the student.
   * @param assignmentID The id of the assignment.
   * @return The Assignment that has been set to complete.
   */
  public static Assignment uncompleteAssignment(String studentID, String assignmentID) {
    try {
      Assignment a = Accessors.getAssignment(assignmentID);
      a.setComplete(studentID, false);
      DBProxy.updateQueryParameters(
          "UPDATE student_assignment SET complete=? WHERE studentID=? AND assignmentID=?;",
          new ArrayList<>(Arrays.asList("false", studentID, assignmentID)));
      // update pet xp
      Student s = Accessors.getStudent(studentID);
      String p = s.getPetId();
      double xp = Accessors.getPet(p).getXp() - a.getReward();
      // (a.getScore(studentID) / a.getTotalScore());
      DBProxy.updateQueryParameters("UPDATE pets SET xp=? WHERE id=?;",
          new ArrayList<>(Arrays.asList("" + xp, p)));
      return a;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add StudentID to Class.
   *
   * @param classID   The class id to be added to.
   * @param studentID The student id to be added.
   * @return The class with the studentID added.
   */
  public static Class addStudentIDToClassCommand(String classID, String studentID) {
    try {
      DBProxy.updateQueryParameters("INSERT INTO class_student VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID, studentID)));
      Class c = Accessors.getClass(classID);
      c.addStudentId(studentID);
      return c;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create Student Command.
   *
   * @param username The username of the new account.
   * @param password The password of the new account, unhashed.
   * @param name     The name of the new account.
   * @return The student that was added.
   */
  public static Student createStudentCommand(String username, String password, String name) {
    try {
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT * FROM students WHERE username=?;", new ArrayList<>(Arrays.asList(username)));
      if (results.size() == 0) {
        UUID studentID = UUID.randomUUID();
        String hashedPassword = PasswordHashing.hashSHA256(password);
        DBProxy.updateQueryParameters("INSERT INTO students VALUES (?,?,?,?);",
            new ArrayList<>(Arrays.asList(studentID.toString(), username, hashedPassword, name)));
        Pet p = null;
        if (name.equals("TimNelson")) {
          p = Mutators.addPet(studentID.toString(), "TimNelson");
        } else {
          p = Mutators.addPet(studentID.toString(), "aaa");
        }
        Student s = new Student(studentID.toString(), username, hashedPassword, name);
        s.setPetId(p.getId());
        return s;
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add assignment to the student.
   *
   * @param studentID    The ID for the student.
   * @param assignmentID The ID of the assignment, to be added to the student.
   */
  public static void addAssignmentToStudent(String studentID, String assignmentID) {
    try {
      DBProxy.updateQueryParameters("INSERT INTO student_assignment VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(studentID, assignmentID, "false")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes an assignment.
   *
   * @param assignmentID The id of the assignment.
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
   * Add Checkoff Assignment Command.
   *
   * @param classID The id of the class to add to.
   * @param name    The name of the assignment.
   * @param reward  The xp reward for completing the assignment.
   * @return The new Checkoff assignment.
   */
  public static Checkoff addCheckoffAssignment(String classID, String name, String reward) {
    try {
      UUID assignmentID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);",
          new ArrayList<>(Arrays.asList(assignmentID.toString(), name, "checkoff", reward)));
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID, assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classID)));
      Checkoff c = new Checkoff(assignmentID.toString(), name, Integer.parseInt(reward));
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0), assignmentID.toString());
        c.setComplete(student.get(0), false);
      }
      return c;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Create a Question.
   *
   * @param prompt      The prompt of the question.
   * @param option1     Multiple Choice 1.
   * @param option2     Multiple Choice 2.
   * @param option3     Multiple Choice 3.
   * @param option4     Multiple Choice 4.
   * @param answerIndex The index of the right option.
   * @return The newly created Question.
   */
  public static Question addQuestion(String prompt, String option1, String option2, String option3,
      String option4, String answerIndex) {
    try {
      UUID questionID = UUID.randomUUID();
      UUID answer1ID = UUID.randomUUID();
      UUID answer2ID = UUID.randomUUID();
      UUID answer3ID = UUID.randomUUID();
      UUID answer4ID = UUID.randomUUID();
      List<String> uuids = new ArrayList<>(Arrays.asList(answer1ID.toString(), answer2ID.toString(),
          answer3ID.toString(), answer4ID.toString()));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(answer1ID.toString(), questionID.toString(), option1)));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(answer2ID.toString(), questionID.toString(), option2)));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(answer3ID.toString(), questionID.toString(), option3)));
      DBProxy.updateQueryParameters("INSERT INTO options VALUES (?,?,?);",
          new ArrayList<>(Arrays.asList(answer4ID.toString(), questionID.toString(), option4)));
      DBProxy.updateQueryParameters("INSERT INTO questions VALUES (?,?,?);", new ArrayList<>(
          Arrays.asList(questionID.toString(), prompt, uuids.get(Integer.parseInt(answerIndex)))));
      Question q = new Question(questionID.toString(), prompt, uuids,
          new ArrayList<>(Arrays.asList(Integer.parseInt(answerIndex))));
      return q;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Review Assignment Command.
   *
   * @param classID The ID of the class that has the new review assignment.
   * @param name    The name of the assignment.
   * @param reward  The xp reward for completing the reward assignment.
   * @return The Review Assignment added.
   */
  public static Review addReviewAssignment(String classID, String name, String reward) {
    try {
      UUID assignmentID = UUID.randomUUID();
      DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);",
          new ArrayList<>(Arrays.asList(assignmentID.toString(), name, "review", reward)));
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID, assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classID)));
      Review r = new Review(assignmentID.toString(), name, Integer.parseInt(reward));

      // add assignment to each student in the class, and set the complete to false
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0), assignmentID.toString());
        r.setComplete(student.get(0), false);
      }
      return r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Add Quiz Assignment Command.
   *
   * @param classID           The class that this assignment belongs to.
   * @param name              The name of the assignment.
   * @param reward            The xp reward for completing the assignment.
   * @param competitiveToggle Whether the assignment is "competitive" or "regular"
   * @param qids              The list of question IDs that belong to this
   *                          assignment.
   * @return The quiz object.
   */
  public static Quiz addQuizAssignment(String classID, String name, String reward,
      String competitiveToggle, List<String> qids) {
    try {
      UUID assignmentID = UUID.randomUUID();
      if (competitiveToggle.equals("competitive")) {
        DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);",
            new ArrayList<>(Arrays.asList(assignmentID.toString(), name, "competitive", reward)));
      } else {
        DBProxy.updateQueryParameters("INSERT INTO assignments VALUES (?,?,?,?);",
            new ArrayList<>(Arrays.asList(assignmentID.toString(), name, "regular", reward)));
      }
      DBProxy.updateQueryParameters("INSERT INTO class_assignment VALUES (?,?);",
          new ArrayList<>(Arrays.asList(classID, assignmentID.toString())));
      List<List<String>> results = DBProxy.executeQueryParameters(
          "SELECT studentID FROM class_student WHERE classID=?;",
          new ArrayList<>(Arrays.asList(classID)));

      List<Question> convertedQ = new ArrayList<>();
      for (String q : qids) {
        // input question ID into database
        DBProxy.updateQueryParameters("INSERT INTO assignment_question VALUES (?,?);",
            new ArrayList<>(Arrays.asList(assignmentID.toString(), q)));
        // create list of questions
        List<List<String>> questionAttributes = DBProxy.executeQueryParameters(
            "SELECT prompt,answerID FROM questions WHERE id=?;", new ArrayList<>(Arrays.asList(q)));
        List<List<String>> options = DBProxy.executeQueryParameters(
            "SELECT option,id FROM options WHERE questionID=?;", new ArrayList<>(Arrays.asList(q)));
        List<String> questionOptions = new ArrayList<>();
        int index = 0;
        for (int o = 0; o < options.size(); o++) {
          questionOptions.add(options.get(o).get(0));
          if (options.get(o).get(1).equals(questionAttributes.get(0).get(1))) {
            index = o;
          }
        }
        List<Integer> answerIndex = new ArrayList<>(Arrays.asList(index));
        Question fullQuestion = new Question(q, questionAttributes.get(0).get(0), questionOptions,
            answerIndex);
        convertedQ.add(fullQuestion);
      }
      Quiz q = null;
      // create complete assignment object
      if (competitiveToggle.equals("competitive")) {
        q = new Quiz(assignmentID.toString(), name, Integer.parseInt(reward), convertedQ, true);
      } else {
        q = new Quiz(assignmentID.toString(), name, Integer.parseInt(reward), convertedQ, false);
      }
      // add assignment to each student in the class, and set the complete to false
      for (List<String> student : results) {
        addAssignmentToStudent(student.get(0), assignmentID.toString());
        q.setComplete(student.get(0), false);
      }
      return q;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
