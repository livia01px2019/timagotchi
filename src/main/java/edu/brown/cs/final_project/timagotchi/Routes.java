package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.jscookie.javacookie.Cookies;
import com.google.common.collect.ImmutableMap;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Classboard;
import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;
import com.google.gson.Gson;

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
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = qmap.value("password");

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
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

  public static class LoginTeacherHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String username = qmap.value("username");
      String password = qmap.value("password");

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

  public static class StudentClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = req.params(":id");
      // TODO: generate class name from Id
      String classesHtml = generateClassSidebar();
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Class",
          "classes", classesHtml, "className", classId);
      return new ModelAndView(variables, "quiz_content.ftl");
    }
  }

  public static class StudentMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String username = cookies.get("username");
      Student currStudent = Controller.getStudent(Controller.getStudentIDFromUsername(username));
      Pet currPet = Controller.getPet(currStudent.getPetId());
      
      String classesHtml = generateClassSidebar();
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "fileNameUsername", new String[] {
              currPet.getImage(), currStudent.getName(), username
          }, "lvlXp", new int[] {
              currPet.getLevel(), currPet.getXp()
          });
      return new ModelAndView(variables, "student-me.ftl");
    }
  }

  public static class StudentLeaderboardHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classesHtml = generateClassSidebar();
      // TODO make leaderboardhtml
      String leaderboardHtml = "";
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "student-all-classes.ftl");
    }
  }

  public static class TeacherClassHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = req.params(":id");
      // TODO: generate class name from Id
      String classesHtml = generateClassSidebar();
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "className", classId);
      return new ModelAndView(variables, "teacher-class-page.ftl");
    }
  }

  public static class TeacherMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classesHtml = generateClassSidebar();
      // TODO get teacher's classboard
      String leaderboardHtml = "";
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher", "classes",
          classesHtml, "fileNameUsername", new String[] {
              "../img/skin6.png", "Teacher Name", "teacherusername"
          }, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "teacher-me.ftl");
    }
  }

  public static class RegisterSubmitHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String role = qmap.value("role");
      String name = qmap.value("name");
      String username = qmap.value("username");
      String password = qmap.value("password");
      // send to backend

      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Register", "message",
          "<p style=\"text-align: center; width: 100%; background-color: green\"> Register successful. Please login.</p>");
      return new ModelAndView(variables, "register.ftl");
    }
  }

  private static String generateClassSidebar() {
    List<Class> classes = new ArrayList<Class>();
    classes.add(new Class("class1id", "Class 1", new ArrayList<String>()));
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
      int petXp = 100; // TODO fix
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
      int classXp = 100; // TODO fix
      sb.append(classXp);
      sb.append("</p>\n" + "    </div>");
    }
    return sb.toString();
  }
}
