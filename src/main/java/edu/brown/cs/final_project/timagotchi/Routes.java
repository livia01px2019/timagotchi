package edu.brown.cs.final_project.timagotchi;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz");
      return new ModelAndView(variables, "quiz_content.ftl");
    }
  }

  public static class StudentMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "skin-file", "img/skin1.png", "name", "Student Name", "username", "studentusername",
          "lvlXp", new int[] {
              0, 0
          });
      return new ModelAndView(variables, "student-me.ftl");
    }
  }
}
