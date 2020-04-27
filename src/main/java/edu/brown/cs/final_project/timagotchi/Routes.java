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
import edu.brown.cs.final_project.timagotchi.assignments.Checkoff;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
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

  public static class FinishedQuizHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // TODO: Integration with backend
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classesHtml = generateClassSidebar(cookies);
      String id = "john";
      List<Question> qs = new ArrayList<Question>();
      Quiz quiz = new Quiz("john", "Quiz 1", 1, qs, false);
      String htmlQuizDone = "";
      List<Question> questionList = quiz.getQuestions();
      for (int i = 0; i < questionList.size(); i++) {
        Question question = questionList.get(i);
        htmlQuizDone += "<tr><td>" + "Q: " + question.getPrompt() + "</td>";
        if (quiz.getRecord(id).get(i)) {
          htmlQuizDone += "<td bgcolor=\"teal\"> A: ";
          for (int a : question.getAnswers()) {
            htmlQuizDone += question.getChoices().get(a) + "\n";
          }
          htmlQuizDone += "</td></tr>";
        } else {
          htmlQuizDone += "<td bgcolor=\"#C31F48\">" + "Student answer" + "</td></tr>";
        }
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "quizresult", htmlQuizDone, "classes", classesHtml);
      return new ModelAndView(variables, "quiz_result.ftl");
    }
  }

  public static class StudentQuizHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml);
      return new ModelAndView(variables, "student-quiz.ftl");
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
      try {
        String className = Controller.getClass(classId).getName();
        String classesHtml = generateClassSidebar(cookies);
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Class",
            "classes", classesHtml, "className", className);
        return new ModelAndView(variables, "student_class.ftl");
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
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz");
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
      String className = Controller.getClass(classId).getName();
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, className, classId);
      return new ModelAndView(variables, "teacher-class-page.ftl");
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
      String classId = req.params(":classid");
      String assignmentId = req.params(":assignmentid");
//      Class classObject = Controller.getClass(classId);
//      Assignment assignment = Controller.getAssignment(assignmentId);
//      System.out.println("assignment id" + assignmentId);
//      JsonArray students = new JsonArray();
//      for (String studentId: classObject.getStudentIds()) {
//        Student currStudent = Controller.getStudent(studentId);
//        JsonObject student = new JsonObject();
//        student.addProperty("id", studentId);
//        student.addProperty("name", currStudent.getName());
//        if (assignment.getComplete(studentId)) {
//
//        }
//      }

      JsonArray students = new JsonArray();
      for (int i = 0; i < 5; i++) {
        JsonObject student = new JsonObject();
        student.addProperty("id", "student" + i);
        student.addProperty("name", "student" + i);
        student.addProperty("score", i);
        students.add(student);
      }
      int totalScore = 5;
//      String className = Controller.getClass(classId).getName();
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "students", GSON.toJson(students), "totalScore", totalScore,
          "assignmentName", assignmentId);
      return new ModelAndView(variables, "teacher-assignment.ftl");
    }
  }

  public static class TeacherMainHandler implements TemplateViewRoute {
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
      classesHtml += "<a href=\"" + currClass.getId() + "\"> " + currClass.getName() + "</a>";
    }
    return classesHtml;
  }

  private static String generateUserboardHtml(Userboard ub) {
    StringBuilder sb = new StringBuilder();
    List<Student> studentList = ub.getRanking();
    int i = 1;
    for (Student s : studentList) {
      sb.append("<div class=\"leaderboard-item\">\n" + "        <div class=\"leaderboard-row\">\n"
          + "            <h2>");
      sb.append(i);
      sb.append("<h2> \n" + "            <p>");
      sb.append(s.getName());
      sb.append("<p>\n" + "        </div>\n" + "        <p>");
      double petXp = Controller.getPet(s.getPetId()).getXp();
      sb.append(petXp);
      sb.append("</p>\n" + "    </div>");
    }
    return sb.toString();
  }

  private static String generateClassboardHtml(Classboard cb) {
    StringBuilder sb = new StringBuilder();
    List<Class> classList = cb.getRanking();
    int i = 1;
    for (Class c : classList) {
      sb.append("<div class=\"leaderboard-item\">\n" + "        <div class=\"leaderboard-row\">\n"
          + "            <h2>");
      sb.append(i);
      sb.append("<h2> \n" + "            <p>");
      sb.append(c.getName());
      sb.append("<p>\n" + "        </div>\n" + "        <p>");
      sb.append(c.getAvgXp());
      sb.append("</p>\n" + "    </div>");
    }
    return sb.toString();
  }
}
