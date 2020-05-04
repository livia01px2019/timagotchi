package edu.brown.cs.final_project.timagotchi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.jscookie.javacookie.Cookies;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

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
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

/**
 * Student Routes class! Holds and handles all web server routing for student
 * side.
 */
public final class StudentRoutes {
  private static final int XP_PER_LEVEL = 100;
  private static final Gson GSON = new Gson();
  private static final int[] LEVELS = new int[] {
      2, 5, 10, 15
  };

  private StudentRoutes() {
  }

  /**
   * Handler for student assignment page where student can view all of their
   * assignments.
   */
  public static class StudentAssignmentHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
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

  /**
   * Handler for when a student attempts a quiz.
   */
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

      Student currStudent = Controller
          .getStudent(Controller.getStudentIDFromUsername(cookies.get("username")));
      Pet currPet = Controller.getPet(currStudent.getPetId());

      String imageFile = "../" + currPet.getImage();
      int level = (int) currPet.getXp() / XP_PER_LEVEL;
      if (level < LEVELS[0]) {
        imageFile = "../../img/stage1.png";
      } else if (level < LEVELS[1]) {
        imageFile = "../../img/stage2.png";
      } else if (level < LEVELS[2]) {
        imageFile = "../../img/stage3.png";
      } else if (level < LEVELS[3]) {
        imageFile = "../../img/stage4.png";
      }

      String assignmentID = req.params(":id");
      String assignmentName = Controller.getAssignment(assignmentID).getName();
      String classesHtml = Routes.generateClassSidebar(cookies);
      String hidden = "<p id=\"hidden\" class=\"" + assignmentID + "\" hidden></p>";
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml, "hidden", hidden, "assignmentName", assignmentName,
          "lvlXpProgressImage", new String[] {
              "" + level, "" + ((int) currPet.getXp()), "" + (currPet.getXp() % XP_PER_LEVEL),
              imageFile
          });
      return new ModelAndView(variables, "student-quiz.ftl");
    }
  }

  /**
   * Handler for the main student page where they can see their pet.
   */
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

      String imageFile = currPet.getImage();
      int level = (int) currPet.getXp() / XP_PER_LEVEL;
      if (level < LEVELS[0]) {
        imageFile = "../img/stage1.png";
      } else if (level < LEVELS[1]) {
        imageFile = "../img/stage2.png";
      } else if (level < LEVELS[2]) {
        imageFile = "../img/stage3.png";
      } else if (level < LEVELS[3]) {
        imageFile = "../img/stage4.png";
      }

      String classesHtml = Routes.generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "fileNameUsername", new String[] {
              imageFile, currStudent.getName(), username
          }, "lvlXpProgress", new double[] {
              level, currPet.getXp(), currPet.getXp() % XP_PER_LEVEL
          });
      return new ModelAndView(variables, "student-me.ftl");
    }
  }

  /**
   * Handler for page where student can see Leaderboard for all of the classes.
   */
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
      String classesHtml = Routes.generateClassSidebar(cookies);
      Classboard cb = new Classboard(Controller.getAllClassIds());
      String leaderboardHtml = Routes.generateClassboardHtml(cb);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "student-all-classes.ftl");
    }
  }

  /**
   * Handler for when student adds themselves to a class.
   */
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
      String classesHtml = Routes.generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student", "classes",
          classesHtml);
      return new ModelAndView(variables, "student-new-class.ftl");
    }
  }

  /**
   * Handler for what a student sees when in a class's page.
   */
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
        String classesHtml = Routes.generateClassSidebar(cookies);
        Userboard userboard = Controller.getLeaderboard(classId);
        String leaderboardHtml = Routes.generateClassUserboardHtml(userboard);
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

  /**
   * Handler for when student finishes a quiz.
   */
  public static class FinishedQuizHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String username = cookies.get("username");
      String studentId = Controller.getStudentIDFromUsername(username);

      QueryParamsMap qm = req.queryMap();
      String assignmentID = qm.value("id");
      List<String> record = GSON.fromJson(qm.value("record"), ArrayList.class);
      List<String> studentRecord = GSON.fromJson(qm.value("studentRecord"), ArrayList.class);
      try {
        Assignment assignment = Controller.addStudentRecord(studentId, assignmentID,
            cookies.get("classId"), studentRecord, record);
      } catch (NumberFormatException numErr) {
        numErr.printStackTrace();
      }
      return "";
    }
  }

  /**
   * Post request handler to load a student's quiz.
   */
  public static class StudentAssignmentLoader implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String username = cookies.get("username");
      String userId = Controller.getStudentIDFromUsername(username);
      Student s = Controller.getStudent(userId);
      String classId = cookies.get("classId");
      String assignmentID = req.params(":id");
      Assignment assignment = Controller.getAssignment(assignmentID);
      Map<String, Object> variables = ImmutableMap.of("assignment", assignment);
      if (assignment instanceof Review) {
        ((Review) assignment).generateQuestions(userId, classId);
      } else {
        Userboard userboard = new Userboard(classId);
        List<List<String>> ranking = userboard.getRankingScore(assignmentID);
        Boolean retry = assignment.getComplete(userId);
        variables = ImmutableMap.of("assignment", assignment, "ranking", ranking, "name",
            s.getName(), "retry", retry);
      }
      return GSON.toJson(variables);
    }
  }

  /**
   * Post request handler to get info in student class page.
   */
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
            ((Review) temp).generateQuestions(userId, classId);
            assignmentIds.add(id);
          }
        }
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds.get(0));
        return GSON.toJson(responseObject);
      }
      return null;
    }
  }

  /**
   * Post request handler when student adds themself to a class.
   */
  public static class SubmitStudentNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String classID = qmap.value("code");
      // Check that name is valid
      String valid = "Name invalid!";
      if (Controller.checkValidClassID(classID)) {
        Class c = Controller.addStudentIDToClassCommand(classID,
            Controller.getStudentIDFromUsername(cookies.get("username")));
        List<String> assignments = c.getAssignmentIds();
        for (String a : assignments) {
          Controller.addAssignmentToStudent(
              Controller.getStudentIDFromUsername(cookies.get("username")), a);
        }
        valid = "Success!";
      } else {
        valid = "Code not valid";
      }
      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }
}
