package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.final_project.timagotchi.users.Class;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Routes class! Holds and handles all web server routing.
 */
public class Routes {

  public static class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Login");
      return new ModelAndView(variables, "login.ftl");
    }
  }

  public static class RegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Register");
      return new ModelAndView(variables, "register.ftl");
    }
  }

  public static class StudentQuizHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String classesHtml = generateClassSidebar();
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml);
      return new ModelAndView(variables, "quiz_content.ftl");
    }
  }

  public static class StudentMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      String classesHtml = generateClassSidebar();
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "fileNameUsername", new String[] {
              "../img/skin1.png", "Student Name", "studentusername"
          }, "lvlXp", new int[] {
              10, 30
          });
      return new ModelAndView(variables, "student-me.ftl");
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
}
