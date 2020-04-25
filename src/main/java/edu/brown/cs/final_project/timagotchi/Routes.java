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
      System.out.println(correctPass);
      System.out.println(password);
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

  public static class SubmitNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String name = qmap.value("name");
      Class newClass = Controller.createClassCommand(new String[] {
          name, cookies.get("username")
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
      String className = Controller.getClass(classId).getName();
      String classesHtml = generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Class",
          "classes", classesHtml, "className", className);
      return new ModelAndView(variables, "quiz_content.ftl");
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

  public static class NewClassHandler implements TemplateViewRoute {
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
      Student currStudent = Controller.getStudent(id);
      classIds = currStudent.getClassIds();
    } else {
      String id = Controller.getTeacherIDFromUsername(username);
      Teacher currTeacher = Controller.getTeacher(id);
      classIds = currTeacher.getClassIds();
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
