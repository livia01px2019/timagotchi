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

  /**
   * Handle GET requests to the front page - /stars.
   */
  public static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("stars", "");
      return new ModelAndView(variables, "query.ftl");
    }
  }
}
