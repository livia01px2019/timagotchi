package edu.brown.cs.final_project.timagotchi;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.jscookie.javacookie.Cookies;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Classboard;
import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;
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
 * Routes class! Holds and handles all web server routing for main routes.
 */
public final class Routes {
  private static final Gson GSON = new Gson();

  private Routes() {
  }

  /**
   * Handler for main info page.
   */
  public static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // Example usage of cookies to remember username information!
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Info");
      return new ModelAndView(variables, "info.ftl");
    }
  }

  /**
   * Handler for logging in.
   */
  public static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Login");
      return new ModelAndView(variables, "login.ftl");
    }
  }

  /**
   * Post request handler for logging in as student.
   */
  public static class LoginStudentHandler implements Route {
    @Override
    public String handle(Request req, Response res) throws NoSuchAlgorithmException {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = PasswordHashing.hashSHA256(qmap.value("password"));

      // Check that username and password are valid
      String valid = "Invalid username and password!";
      String correctPass = Accessors.getStudentPassword(username);
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

  /**
   * Post request handler for logging in as teacher.
   */
  public static class LoginTeacherHandler implements Route {
    @Override
    public String handle(Request req, Response res) throws NoSuchAlgorithmException {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = PasswordHashing.hashSHA256(qmap.value("password"));

      // Check that username and password are valid
      String valid = "Invalid username and password!";
      String correctPass = Accessors.getTeacherPassword(username);
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

  /**
   * Post request handler for registering.
   */
  public static class RegisterSubmitHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String student = qmap.value("student");
      String teacher = qmap.value("teacher");
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
            Student s = Mutators.createStudentCommand(username, password, name);
            if (s == null) {
              valid = "Username is taken. Please pick another one.";
            } else {
              valid = "Success!";
            }
          } else if (teacher.equals("true")) {
            // Create a teacher
            Teacher t = Mutators.createTeacherCommand(username, password, name);
            if (t == null) {
              valid = "Username is taken. Please pick another one.";
            } else {
              valid = "Success!";
            }
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

  /**
   * Handler for register page.
   *
   */
  public static class RegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Register", "message",
          "");
      return new ModelAndView(variables, "register.ftl");
    }
  }

  /**
   * Handler for logging out.
   *
   */
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

  /**
   * Given cookies, returns HTML code to display classes in sidebar.
   *
   * @param cookies cookies for current user
   * @return HTML code for sidebar.
   */
  public static String generateClassSidebar(Cookies cookies) {
    String username = cookies.get("username");
    List<Class> classes = new ArrayList<Class>();
    List<String> classIds = new ArrayList<String>();
    if (cookies.get("student").equals("true")) {
      String id = Accessors.getStudentIDFromUsername(username);
      Student currStudent = Accessors.getStudent(id);
      if (currStudent.getClassIds() != null) {
        classIds = currStudent.getClassIds();
      }

    } else {
      String id = Accessors.getTeacherIDFromUsername(username);
      Teacher currTeacher = Accessors.getTeacher(id);
      if (currTeacher.getClassIds() != null) {
        classIds = currTeacher.getClassIds();
      }
    }
    for (String id : classIds) {
      classes.add(Accessors.getClass(id));
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

  /**
   * Generates HTML code for a Userboard.
   *
   * @param ub Userboard.
   * @return HTML code for Userboard.
   */
  public static String generateClassUserboardHtml(Userboard ub) {
    StringBuilder sb = new StringBuilder();
    List<List<String>> studentList = ub.allAssignmentsXP();
    int i = 1;
    if (studentList.size() == 0) {
      sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\">");
      sb.append("<p>");
      sb.append("No students yet!");
      sb.append("</p></div>");
    } else {
      for (List<String> s : studentList) {
        sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\"><h2>");
        sb.append(i);
        sb.append("<h2><p>");
        sb.append(s.get(0));
        sb.append("<p></div><p>");
        sb.append(s.get(1));
        sb.append("</p></div>");
        i++;
      }
    }
    return sb.toString();
  }

  /**
   * Generates HTML code for classboard.
   *
   * @param cb classboard
   * @return HTML code for cb
   */
  public static String generateClassboardHtml(Classboard cb) {
    StringBuilder sb = new StringBuilder();
    List<List<String>> classList = cb.rankClassByTotalXP();
    int i = 1;
    if (classList.size() == 0) {
      sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\">");
      sb.append("<p>");
      sb.append("No classes yet!");
      sb.append("</p></div>");
    } else {
      for (List<String> c : classList) {
        sb.append("<div class=\"leaderboard-item\"><div class=\"leaderboard-row\"><h2>");
        sb.append(i);
        sb.append("<h2><p>");
        sb.append(c.get(0));
        sb.append("<p></div><p>");
        sb.append(c.get(1));
        sb.append("</p></div>");
        i++;
      }
    }
    return sb.toString();
  }
}
