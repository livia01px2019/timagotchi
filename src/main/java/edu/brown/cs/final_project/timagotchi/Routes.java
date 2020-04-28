package edu.brown.cs.final_project.timagotchi;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.jscookie.javacookie.Cookies;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import edu.brown.cs.final_project.timagotchi.utils.PasswordHashing;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

/**
 * Routes class! Holds and handles all web server routing.
 */
public class Routes {

  private static final Gson GSON = new Gson();

  public static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // Example usage of cookies to remember username information!
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String userId = cookies.get("userId");
      cookies.set("userId", "David Lee");
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Login");
      return new ModelAndView(variables, "login.ftl");
    }
  }

  public static class LoginStudentHandler implements Route {
    @Override
    public String handle(Request req, Response res) throws NoSuchAlgorithmException {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = PasswordHashing.hashSHA256(qmap.value("password"));

      // Check that username and password are valid
      String valid = "Invalid username and password!";
      String correctPass = Controller.getStudentPassword(username);
      if (username.equals("")) {
        valid = "Please enter a username.";
      } else if (password.equals("")) {
        valid = "Please enter a password.";
      } else if (password.equals(correctPass)) {
        valid = "Success!";
        cookies.set("student", "true");
        cookies.set("username", username);
        cookies.set("password", password);
      } else if (correctPass.equals("")) {
        valid = "Account with that username does not exist.";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class LoginTeacherHandler implements Route {
    @Override
    public String handle(Request req, Response res) throws NoSuchAlgorithmException {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = PasswordHashing.hashSHA256(qmap.value("password"));

      // Check that username and password are valid
      String valid = "Invalid username and password!";
      String correctPass = Controller.getTeacherPassword(username);
      if (username.equals("")) {
        valid = "Please enter a username.";
      } else if (password.equals("")) {
        valid = "Please enter a password.";
      } else if (password.equals(correctPass)) {
        valid = "Success!";
        cookies.set("student", "false");
        cookies.set("username", username);
        cookies.set("password", password);
      } else if (correctPass.equals("")) {
        valid = "Account with that username does not exist.";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class SubmitTeacherNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String name = qmap.value("name");
      Class newClass = Controller.createClassCommand(new String[] {
          name, Controller.getTeacherIDFromUsername(cookies.get("username"))
      });
      Controller.addReviewAssignment(newClass.getId() + " Review 100");

      // Check that name is valid
      String valid = "Name invalid!";
      if (name.equals("")) {
        valid = "Please enter a class name.";
      } else if (!(newClass == null)) {
        valid = "Success!";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class SubmitStudentNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String classID = qmap.value("code");
      // Check that name is valid
      String valid = "Name invalid!";
      if (Controller.checkValidClassID(classID)) {
        Controller.addStudentIDToClassCommand(new String[] {
            classID, Controller.getStudentIDFromUsername(cookies.get("username"))
        });
        valid = "Success!";
      } else {
        valid = "Code not valid";
      }
      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class DeleteAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      System.out.println("delete assignment called");
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String assignmentId = cookies.get("assignmentId");
      String classId = cookies.get("classId");
      cookies.remove("assignmentId");

      String valid = "";

      try {
        Controller.deleteAssignment(assignmentId);
        valid = "Success!";
      } catch (Exception e) {
        e.printStackTrace();
        valid = "Error deleting assignment.";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid, "classId", classId);
      System.out.println("i am here!!");
      return GSON.toJson(responseObject);
    }
  }

  public static class RegisterSubmitHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String student = qmap.value("student");
      String teacher = qmap.value("teacher");
      System.out.println(student);
      String name = qmap.value("name");
      String username = qmap.value("username");
      String password = qmap.value("password");
      String confirm = qmap.value("confirm");
      String valid = "Passwords don't match!";

      if (name.equals("")) {
        valid = "Please enter a name.";
      } else if (username.equals("")) {
        valid = "Please enter a username.";
      } else if (password.equals("")) {
        valid = "Please enter a password.";
      } else if (password.equals(confirm)) {
        try {
          if (student.equals("true")) {
            // Create a student
            Student s = Controller.createStudentCommand(username + " " + password + " " + name);
            valid = "Success!";
          } else if (teacher.equals("true")) {
            // Create a teacher
            Teacher t = Controller.createTeacherCommand(username + " " + password + " " + name);
            valid = "Success!";
          } else {
            valid = "Please select Student or Teacher.";
          }
        } catch (Exception e) {
          valid = "Error: Unknown";
        }
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class CreateNewAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classID = cookies.get("classId");
      System.out.println(classID);
      QueryParamsMap qmap = req.queryMap();
      String title = qmap.value("title");
      String points = qmap.value("points");
      String isQuiz = qmap.value("quiz");
      String isCheckoff = qmap.value("checkoff");
      String competitive = qmap.value("competitive");

      List<String> questions = GSON.fromJson(qmap.value("questions"), ArrayList.class);
      List<String> firstAnswers = GSON.fromJson(qmap.value("firstAnswers"), ArrayList.class);
      List<String> secondAnswers = GSON.fromJson(qmap.value("secondAnswers"), ArrayList.class);
      List<String> thirdAnswers = GSON.fromJson(qmap.value("thirdAnswers"), ArrayList.class);
      List<String> fourthAnswers = GSON.fromJson(qmap.value("fourthAnswers"), ArrayList.class);
      List<String> correctAnswers = GSON.fromJson(qmap.value("correctAnswers"), ArrayList.class);
      String valid = "ERROR: Unknown";
      String assignmentID = "";

      if (title.equals("")) {
        valid = "Assignment needs a title!";
      } else {
        try {
          double pointNum = Double.parseDouble(points);
          String assignmentString = classID + " " + title + " " + points;
          System.out.println("BRUH:" + assignmentString);
          if (isCheckoff.equals("true")) {
            Checkoff assignment = Controller.addCheckoffAssignment(assignmentString);
            assignmentID = assignment.getId();
            valid = "Assignment successfully created!";
          } else if (isQuiz.equals("true")) {
            List<String> quizList = new ArrayList<>();
            quizList.add(classID);
            quizList.add(title);
            quizList.add(points);
            if (competitive.equals("true")) {
              quizList.add("competitive");
            } else {
              quizList.add("regular");
            }
            List<List<String>> questionLists = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
              String question = questions.get(i);
              String firstAnswer = firstAnswers.get(i);
              String secondAnswer = secondAnswers.get(i);
              String thirdAnswer = thirdAnswers.get(i);
              String fourthAnswer = fourthAnswers.get(i);
              String correctAnswer = correctAnswers.get(i);

              if (question.equals("") || correctAnswer.equals("") || firstAnswer.equals("")
                  || secondAnswer.equals("") || thirdAnswer.equals("") || fourthAnswer.equals("")) {
                valid = "At least one question is missing the prompt, correct answer, or answer choices";
                break;
              } else {
                try {
                  int correctNum = Integer.parseInt(correctAnswer);
                  if (correctNum < 1 || correctNum > 4) {
                    throw new NumberFormatException();
                  } else {
                    correctNum--;
                  }
                  List<String> toAdd = new ArrayList<>();
                  toAdd.add(question);
                  toAdd.add(firstAnswer);
                  toAdd.add(secondAnswer);
                  toAdd.add(thirdAnswer);
                  toAdd.add(fourthAnswer);
                  toAdd.add(Integer.toString(correctNum));
                  String questionString = question + " " + firstAnswer + " " + secondAnswer + " "
                      + thirdAnswer + " " + fourthAnswer + " " + correctNum + " ";
                  System.out.println(questionString);
                  questionLists.add(toAdd);
                } catch (NumberFormatException numErr) {
                  valid = "Correct answer must be between 1 and 4";
                  break;
                } catch (Exception err) {
                  err.printStackTrace();
                  break;
                }
              }
            }
            if (valid.equals("ERROR: Unknown")) {
              for (List<String> qList : questionLists) {
                Question q = Controller.addQuestion(qList);
                quizList.add(q.getId());
              }
              Quiz assignment = Controller.addQuizAssignment(quizList);
              assignmentID = assignment.getId();
              valid = "Assignment successfully created!";
            }
          } else {
            valid = "Please select Quiz or Checkoff";
          }
        } catch (NumberFormatException numErr) {
          valid = "Invalid number of points";
        } catch (Exception err) {
          err.printStackTrace();
        }
      }
      Map<String, Object> responseObject = ImmutableMap.of("results", valid, "assignmentid",
          assignmentID);
      return GSON.toJson(responseObject);
    }
  }

  public static class RegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Register", "message",
          "");
      return new ModelAndView(variables, "register.ftl");
    }
  }

  public static class StudentAssignmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // TODO: Call method to get the list of quizzes; get student id from somewhere
      String id = "john";
      List<Quiz> quizList = new ArrayList<>();
      List<Question> qs = new ArrayList<Question>();
      Quiz quiz1 = new Quiz("john", "Quiz 1", 1, qs, false);
      quizList.add(quiz1);
      String htmlQuiz = "";
      for (Quiz quiz : quizList) {
        htmlQuiz += "<tr><td>" + quiz.getName() + "</td><td>";
        if (quiz.getComplete(id) == null) {
          htmlQuiz += "Incomplete" + "</td></tr>";
        } else {
          List<Boolean> scores = quiz.getRecord(id);
          int numQs = scores.size();
          int numCorrect = 0;
          for (boolean score : scores) {
            if (score) {
              numCorrect++;
            }
          }
          htmlQuiz += numCorrect + "/" + numQs;
        }
      }
      List<Checkoff> checkoffList = new ArrayList<>();
      String htmlCheckoff = "";
      for (Checkoff c : checkoffList) {
        htmlCheckoff += "<tr><td>" + c.getName() + "</td><td>";
        if (c.getComplete(id) == null || !c.getComplete(id)) {
          htmlCheckoff += "Incomplete" + "</td></tr>";
        } else {
          htmlCheckoff += "Completed" + "</td></tr>";
        }
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "quizlist", htmlQuiz, "checkofflist", htmlCheckoff);
      return new ModelAndView(variables, "student-assignment.ftl");
    }
  }

  public static class FinishedQuizHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      // TODO: Integration with backend
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String username = cookies.get("username");
      String studentId = Controller.getStudentIDFromUsername(username);
//      String classesHtml = generateClassSidebar(cookies);
//      String id = "john";
//      List<Question> qs = new ArrayList<Question>();
//      Quiz quiz = new Quiz("john", "Quiz 1", 1, qs, false);
//      String htmlQuizDone = "";
//      List<Question> questionList = quiz.getQuestions();
//      for (int i = 0; i < questionList.size(); i++) {
//        Question question = questionList.get(i);
//        htmlQuizDone += "<tr><td>" + "Q: " + question.getPrompt() + "</td>";
//        if (quiz.getRecord(id).get(i)) {
//          htmlQuizDone += "<td bgcolor=\"teal\"> A: ";
//          for (int a : question.getAnswers()) {
//            htmlQuizDone += question.getChoices().get(a) + "\n";
//          }
//          htmlQuizDone += "</td></tr>";
//        } else {
//          htmlQuizDone += "<td bgcolor=\"#C31F48\">" + "Student answer" + "</td></tr>";
//        }
//      }
//      int xp = quiz.getReward();
      QueryParamsMap qm = req.queryMap();
      String assignmentID = qm.value("id");
      List<String> record = GSON.fromJson(qm.value("record"), ArrayList.class);
      try {
        // TODO: add xp reward to Student
        int reward = Integer.parseInt(qm.value("reward"));
        Assignment assignment = Controller.addStudentRecord(studentId, assignmentID,
            cookies.get("classId"), record);
      } catch (NumberFormatException numErr) {
        numErr.printStackTrace();
      }
      return "";
    }
  }

  public static class StudentQuizHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("false")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/main';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }
      String assignmentID = req.params(":id");
      String assignmentName = Controller.getAssignment(assignmentID).getName();
      String classesHtml = generateClassSidebar(cookies);
      String hidden = "<p id=\"hidden\" class=\"" + assignmentID + "\" hidden></p>";
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml, "hidden", hidden, "assignmentName", assignmentName);
      return new ModelAndView(variables, "student-quiz.ftl");
    }
  }

  public static class StudentAssignmentLoader implements Route {
    @Override
    public String handle(Request req, Response res) {
      String assignmentID = req.params(":id");
      Assignment assignment = Controller.getAssignment(assignmentID);
//      List<String> ans = new ArrayList<>();
//      ans.add("first");
//      ans.add("second");
//      ans.add("third");
//      ans.add("fourth");
//      List<Integer> correct = new ArrayList<>();
//      correct.add(1);
//      Question question = new Question("Q1", "Test", ans, correct);
//      List<Question> qs = new ArrayList<Question>();
//      qs.add(question);
//      Quiz assignment = new Quiz(assignmentID, "Quiz 1", 1, qs, false);
//      assignment.setReward(100);
      Map<String, Object> variables = ImmutableMap.of("assignment", assignment);
      return GSON.toJson(variables);
    }
  }

  public static class StudentClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("false")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/main';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }
      String classId = req.params(":id");
      cookies.set("classId", classId);
      try {
        String className = Controller.getClass(classId).getName();
        String classesHtml = generateClassSidebar(cookies);
        Userboard userboard = Controller.getLeaderboard(classId);
        String leaderboardHtml = generateUserboardHtml(userboard);

        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Class",
            "classes", classesHtml, "className", className, "leaderboard", leaderboardHtml);
        return new ModelAndView(variables, "student-class.ftl");
      } catch (Exception e) {
        e.printStackTrace();
        // classID is incorrect
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error.ftl");
      }
    }
  }

  public static class StudentMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (!(cookies.get("classId") == null)) {
        cookies.remove("classId");
      }
      String username = cookies.get("username");
      if (username == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("false")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/main';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }

      Student currStudent = Controller.getStudent(Controller.getStudentIDFromUsername(username));
      Pet currPet = Controller.getPet(currStudent.getPetId());

      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "fileNameUsername", new String[] {
              currPet.getImage(), currStudent.getName(), username
          }, "lvlXp", new double[] {
              currPet.getLevel(), currPet.getXp()
          });
      return new ModelAndView(variables, "student-me.ftl");
    }
  }

  public static class TeacherNewAssignmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("true")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error-student.ftl");
      }
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml);
      return new ModelAndView(variables, "teacher-create-assignment.ftl");
    }
  }

  public static class StudentLeaderboardHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("false")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/main';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }
      String classesHtml = generateClassSidebar(cookies);
      Classboard cb = new Classboard(Controller.getAllClassIds());
      String leaderboardHtml = generateClassboardHtml(cb);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "student-all-classes.ftl");
    }
  }

  public static class TeacherClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("true")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error-student.ftl");
      }
      String classId = req.params(":id");
      cookies.set("classId", classId);
      String className = Controller.getClass(classId).getName();
      String classesHtml = generateClassSidebar(cookies);
      Userboard userboard = Controller.getLeaderboard(classId);
      String leaderBoardHtml = generateUserboardHtml(userboard);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "className", className, "classId", classId, "leaderboard",
          leaderBoardHtml);
      return new ModelAndView(variables, "teacher-class.ftl");
    }
  }

  public static class TeacherClassGetHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = cookies.get("classId");
      QueryParamsMap qmap = req.queryMap();
      if (qmap.value("type").equals("assignments")) {
        List<String> assignmentIds = Controller.getClass(classId).getAssignmentIds();
        System.out.println("assignments");
        System.out.println(assignmentIds);
        List<String> assignmentNames = new ArrayList<>();
        for (String id : assignmentIds) {
          assignmentNames.add(Controller.getAssignment(id).getName());
        }
        System.out.println("in assignments");
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds, "names",
            assignmentNames);
        System.out.println(responseObject);
        return GSON.toJson(responseObject);
      }
      return null;
    }
  }

  public static class StudentClassGetHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = cookies.get("classId");
      String username = cookies.get("username");
      String userId = Controller.getStudentIDFromUsername(username);
      QueryParamsMap qmap = req.queryMap();

      List<String> allAssignmentIds = Controller.getClass(classId).getAssignmentIds();
      List<String> assignmentIds = new ArrayList<>();
      List<String> assignmentNames = new ArrayList<>();
      List<String> scores = new ArrayList<>();
      List<String> totalScores = new ArrayList<>();
      List<String> complete = new ArrayList<>();

      if (qmap.value("type").equals("quiz")) {
        for (String id : allAssignmentIds) {
          System.out.println("id " + id);
          Assignment temp = Controller.getAssignment(id);
          if (temp instanceof Quiz) {
            assignmentIds.add(id);
            assignmentNames.add(temp.getName());
            if (temp.getScore(userId) == null) {
              scores.add("INCOMPLETE");
            } else {
              scores.add(temp.getScore(userId).toString());
            }
            totalScores.add(temp.getTotalScore().toString());
            if (temp.getComplete(userId) == null) {
              complete.add("false");
            } else {
              complete.add(temp.getComplete(userId).toString());
            }
          }
        }
        System.out.println("Assignments");
        System.out.println(assignmentIds);
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds, "names",
            assignmentNames, "scores", scores, "totalScores", totalScores, "completed", complete);
        return GSON.toJson(responseObject);
      } else if (qmap.value("type").equals("checkoff")) {
        for (String id : allAssignmentIds) {
          Assignment temp = Controller.getAssignment(id);
          if (temp instanceof Checkoff) {
            assignmentIds.add(id);
            assignmentNames.add(temp.getName());
            try {
              scores.add(temp.getScore(userId).toString());
              complete.add(temp.getComplete(userId).toString());
            } catch (Exception e) {
              scores.add("INCOMPLETE");
              complete.add("false");
            }
            totalScores.add(temp.getTotalScore().toString());
          }
        }
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds, "names",
            assignmentNames, "scores", scores, "totalScores", totalScores, "completed", complete);
        return GSON.toJson(responseObject);
      } else if (qmap.value("type").equals("review")) {
        for (String id : allAssignmentIds) {
          Assignment temp = Controller.getAssignment(id);
          if (temp instanceof Review) {
            Student s = Controller.getStudent(userId);
            ((Review) temp).generateQuestions(s, classId);
            assignmentIds.add(id);
          }
        }
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds.get(0));
        return GSON.toJson(responseObject);
      }
      return null;
    }
  }

  public static class TeacherAssignmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("true")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error-student.ftl");
      }
      String assignmentId = req.params(":assignmentid");
      cookies.set("assignmentId", assignmentId);
      Class classObject = Controller.getClass(cookies.get("classId"));
      System.out.println("sstudents from class:" + classObject.getStudentIds());

      Assignment assignment = Controller.getAssignment(assignmentId);
      System.out.println("assignment id" + assignmentId);

      JsonArray students = new JsonArray();
      for (String studentId : classObject.getStudentIds()) {
        Student currStudent = Controller.getStudent(studentId);
        System.out.println("a student added");
        JsonObject student = new JsonObject();
        student.addProperty("id", studentId);
        student.addProperty("name", currStudent.getName());
        if (assignment.getScore(studentId) == null) {
          student.addProperty("score", "INCOMPLETE");
        } else {
          student.addProperty("score", assignment.getScore(studentId));
        }
        students.add(student);
      }
      System.out.println("students: " + students);
      int totalScore = assignment.getTotalScore();
      String className = classObject.getName();
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "students", GSON.toJson(students), "totalScore", totalScore,
          "assignmentName", assignment.getName());
      return new ModelAndView(variables, "teacher-assignment.ftl");
    }
  }

  public static class TeacherMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (!(cookies.get("classId") == null)) {
        cookies.remove("classId");
      }
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("true")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error-student.ftl");
      }
      String classesHtml = generateClassSidebar(cookies);
      String username = cookies.get("username");
      Teacher currTeacher = Controller.getTeacher(Controller.getTeacherIDFromUsername(username));
      Classboard cb = new Classboard(currTeacher.getClassIds());
      String leaderboardHtml = generateClassboardHtml(cb);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher", "classes",
          classesHtml, "nameUsername", new String[] {
              currTeacher.getName(), currTeacher.getUsername()
          }, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "teacher-me.ftl");
    }
  }

  public static class TeacherNewClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (!(cookies.get("classId") == null)) {
        cookies.remove("classId");
      }
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("true")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/student/main';</script>");
        return new ModelAndView(variables, "error-student.ftl");
      }
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher", "classes",
          classesHtml);
      return new ModelAndView(variables, "teacher-new-class.ftl");
    }
  }

  public static class StudentNewClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      if (!(cookies.get("classId") == null)) {
        cookies.remove("classId");
      }
      if (cookies.get("username") == null) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/login';</script>");
        return new ModelAndView(variables, "error.ftl");
      } else if (cookies.get("student").equals("false")) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/main';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml);
      return new ModelAndView(variables, "student-new-class.ftl");
    }
  }

  public static class LogoutHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      cookies.remove("username");
      cookies.remove("password");
      cookies.remove("student");
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
          "<script>window.location.href = '/login';</script>");
      return new ModelAndView(variables, "error.ftl");
    }
  }

  private static String generateClassSidebar(Cookies cookies) {
    String username = cookies.get("username");
    List<Class> classes = new ArrayList<Class>();
    List<String> classIds = new ArrayList<String>();
    if (cookies.get("student").equals("true")) {
      String id = Controller.getStudentIDFromUsername(username);
      System.out.println(id);
      Student currStudent = Controller.getStudent(id);
      System.out.println(currStudent.getClassIds());
      if (currStudent.getClassIds() != null) {
        classIds = currStudent.getClassIds();
      }

    } else {
      String id = Controller.getTeacherIDFromUsername(username);
      Teacher currTeacher = Controller.getTeacher(id);
      if (currTeacher.getClassIds() != null) {
        classIds = currTeacher.getClassIds();
      }
    }
    for (String id : classIds) {
      classes.add(Controller.getClass(id));
    }
    String classesHtml = "";
    for (Class currClass : classes) {
      if (cookies.get("student").equals("true")) {
        classesHtml += "<a href=\"/student/" + currClass.getId() + "\"> " + currClass.getName()
            + "</a>";
      } else {
        classesHtml += "<a href=\"/teacher/" + currClass.getId() + "\"> " + currClass.getName()
            + "</a>";
      }

    }
    return classesHtml;
  }

  private static String generateUserboardHtml(Userboard ub) {
    StringBuilder sb = new StringBuilder();
    List<Student> studentList = ub.getRanking();
    int i = 1;
    if (studentList.size() == 0) {
      sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\">");
      sb.append("<p>");
      sb.append("No students yet!");
      sb.append("</p></div>");
    } else {
      for (Student s : studentList) {
        sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\"><h2>");
        sb.append(i);
        sb.append("<h2><p>");
        sb.append(s.getName());
        sb.append("<p></div><p>");
        double petXp = Controller.getPet(s.getPetId()).getXp();
        sb.append(petXp);
        sb.append("</p></div>");
        i++;
      }
    }
    return sb.toString();
  }

  private static String generateClassboardHtml(Classboard cb) {
    StringBuilder sb = new StringBuilder();
    List<Class> classList = cb.getRanking();
    int i = 1;
    if (classList.size() == 0) {
      sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\">");
      sb.append("<p>");
      sb.append("No classes yet!");
      sb.append("</p></div>");
    } else {
      for (Class c : classList) {
        sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\"><h2>");
        sb.append(i);
        sb.append("<h2><p>");
        sb.append(c.getName());
        sb.append("<p></div><p>");
        sb.append(c.getAvgXp());
        sb.append("</p></div>");
        i++;
      }
    }
    return sb.toString();
  }
}
