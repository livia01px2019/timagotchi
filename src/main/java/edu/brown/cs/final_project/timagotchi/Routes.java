package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;
import edu.brown.cs.final_project.timagotchi.assignments.Checkoff;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
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

  public static class StudentAssignmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // TODO: Call method to get the list of quizzes; get student id from somewhere
      String id = "john";
      List<Quiz> quizList = new ArrayList<>();
      List<Question> qs = new ArrayList<Question>();
      Quiz quiz1 = new Quiz("john", "Quiz 1", false, 1, qs, false);
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
      String id = "john";
      List<Question> qs = new ArrayList<Question>();
      Quiz quiz = new Quiz("john", "Quiz 1", false, 1, qs, false);
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
          htmlQuizDone += "<td bgcolor=\"#C31F48\">" + "Student answer"  + "</td></tr>";
        }
      }
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
              "quizresult", htmlQuizDone);
      return new ModelAndView(variables, "quiz_result.ftl");
    }
  }

  public static class StudentQuizHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz");
      return new ModelAndView(variables, "student-quiz.ftl");
    }
  }

  public static class StudentMainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "skinFile", "img/skin1.png", "name", "Student Name", "username", "studentusername",
          "lvlXp", new int[] {
              0, 0
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
}
