package edu.brown.cs.final_project.timagotchi;

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
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

/**
 * Teacher Routes class! Holds and handles all web server routing for teacher
 * side.
 */
public final class TeacherRoutes {
  private static final Gson GSON = new Gson();

  private TeacherRoutes() {
  }

  /**
   * Handler for creating a new assignment.
   */
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
      String classesHtml = Routes.generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Student Quiz",
          "classes", classesHtml);
      return new ModelAndView(variables, "teacher-create-assignment.ftl");
    }
  }

  /**
   * Handler to display information for a class.
   */
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
      String className = Accessors.getClass(classId).getName();
      String classesHtml = Routes.generateClassSidebar(cookies);
      Userboard userboard = Accessors.getLeaderboard(classId);
      String leaderBoardHtml = Routes.generateClassUserboardHtml(userboard);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "className", className, "classId", classId, "leaderboard",
          leaderBoardHtml);
      return new ModelAndView(variables, "teacher-class.ftl");
    }
  }

  /**
   * Post request handler to display information for a class.
   */
  public static class TeacherClassGetHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = cookies.get("classId");
      QueryParamsMap qmap = req.queryMap();
      if (qmap.value("type").equals("assignments")) {
        List<String> assignmentIds = Accessors.getClass(classId).getAssignmentIds();
        List<String> assignmentNames = new ArrayList<>();
        for (String id : assignmentIds) {
          assignmentNames.add(Accessors.getAssignment(id).getName());
        }

        JsonArray assignments = new JsonArray();
        for (String id : assignmentIds) {
          Assignment currAssignment = Accessors.getAssignment(id);
          JsonObject assignment = new JsonObject();
          assignment.addProperty("id", id);
          assignment.addProperty("name", currAssignment.getName());
          if (currAssignment instanceof Checkoff) {
            assignment.addProperty("type", "checkoff");
          } else {
            assignment.addProperty("type", "quiz");
          }
          assignments.add(assignment);
        }

        Map<String, Object> responseObject = ImmutableMap.of("assignments",
            GSON.toJson(assignments));
        return GSON.toJson(responseObject);
      }
      return null;
    }
  }

  /**
   * Handler to get information on a specific assignment in a class.
   */
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
      Class classObject = Accessors.getClass(cookies.get("classId"));

      Assignment assignment = Accessors.getAssignment(assignmentId);

      JsonArray students = new JsonArray();
      for (String studentId : classObject.getStudentIds()) {
        Student currStudent = Accessors.getStudent(studentId);
        JsonObject student = new JsonObject();
        student.addProperty("id", studentId);
        student.addProperty("name", currStudent.getName());
        student.addProperty("score", assignment.getScore(studentId));
        students.add(student);
      }
      int totalScore = assignment.getTotalScore();
      String className = classObject.getName();
      String classesHtml = Routes.generateClassSidebar(cookies);

      if (assignment instanceof Checkoff) {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
            "classes", classesHtml, "students", GSON.toJson(students), "totalScore", totalScore,
            "assignmentName", assignment.getName());
        return new ModelAndView(variables, "teacher-assignment-checkoff.ftl");
      } else {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
            "classes", classesHtml, "students", GSON.toJson(students), "totalScore", totalScore,
            "assignmentName", assignment.getName());
        return new ModelAndView(variables, "teacher-assignment.ftl");
      }
    }
  }

  /**
   * Handler to get information on a specific assignment in a class.
   */
  public static class TeacherAssignmentStudentHandler implements TemplateViewRoute {
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
      String studentId = req.params(":studentid");
      String assignmentId = cookies.get("assignmentId");

      Assignment assignment = Accessors.getAssignment(assignmentId);

      if (assignment instanceof Quiz) {
        Quiz quiz = (Quiz) assignment;
        List<Boolean> studentRecord = quiz.getRecord(studentId);
        List<Question> questions = quiz.getQuestions();
        JsonArray record = new JsonArray();
        for (int i = 0; i < questions.size(); i++) {
          JsonObject row = new JsonObject();
          Question currQ = questions.get(i);
          row.addProperty("questionPrompt", currQ.getPrompt());
          row.addProperty("correctAnswer", currQ.getChoices().get(currQ.getAnswers().get(0)));
          row.addProperty("answer", Accessors.getRecord(studentId, currQ.getId()));
          row.addProperty("correct", studentRecord.get(i));
          record.add(row);
        }

        int score = quiz.getScore(studentId);
        int totalScore = quiz.getTotalScore();
        Student student = Accessors.getStudent(studentId);
        String name = student.getName();
        String classesHtml = Routes.generateClassSidebar(cookies);
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
            "classes", classesHtml, "record", GSON.toJson(record), "scoreTotalscore", new int[] {
                score, totalScore
            }, "assignmentnameStudentname", new String[] {
                assignment.getName(), name
            });
        return new ModelAndView(variables, "teacher-indiv-assignment.ftl");
      } else {
        Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Error", "redirect",
            "<script>window.location.href = '/teacher/viewAssignment/" + assignmentId
                + "';</script>");
        return new ModelAndView(variables, "error-teacher.ftl");
      }
    }
  }

  /**
   * Handler for teacher's main page.
   */
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
      String classesHtml = Routes.generateClassSidebar(cookies);
      String username = cookies.get("username");
      Teacher currTeacher = Accessors.getTeacher(Accessors.getTeacherIDFromUsername(username));
      Classboard cb = new Classboard(currTeacher.getClassIds());
      String leaderboardHtml = Routes.generateClassboardHtml(cb);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher", "classes",
          classesHtml, "nameUsername", new String[] {
              currTeacher.getName(), currTeacher.getUsername()
          }, "leaderboard", leaderboardHtml);
      return new ModelAndView(variables, "teacher-me.ftl");
    }
  }

  /**
   * Handler for creating a new class.
   */
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
      String classesHtml = Routes.generateClassSidebar(cookies);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher", "classes",
          classesHtml);
      return new ModelAndView(variables, "teacher-new-class.ftl");
    }
  }

  /**
   * Post request handler for creating a new assignment.
   */
  public static class CreateNewAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classID = cookies.get("classId");
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
          List<String> quizList = new ArrayList<>();
          if (isCheckoff.equals("true")) {
            Checkoff assignment = Mutators.addCheckoffAssignment(classID, title, points);
            assignmentID = assignment.getId();
            valid = "Assignment successfully created!";
          } else if (isQuiz.equals("true")) {

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
                valid = "At least one question is missing the prompt, "
                    + "correct answer, or answer choices";
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
                Question q = Mutators.addQuestion(qList.get(0), qList.get(1), qList.get(2),
                    qList.get(3), qList.get(4), qList.get(5));
                quizList.add(q.getId());
              }
              Quiz assignment = null;
              if (competitive.equals("true")) {
                assignment = Mutators.addQuizAssignment(classID, title, points, "competitive",
                    quizList);
              } else {
                assignment = Mutators.addQuizAssignment(classID, title, points, "regular",
                    quizList);
              }
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

  /**
   * Post request handler for creating a new class.
   */
  public static class SubmitTeacherNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String name = qmap.value("name");
      Class newClass = Mutators.createClassCommand(name,
          Accessors.getTeacherIDFromUsername(cookies.get("username")));
      Mutators.addReviewAssignment(newClass.getId(), "Review", "100");

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

  /**
   * Post request handler for deleting an assignment.
   */
  public static class DeleteAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String assignmentId = cookies.get("assignmentId");
      String classId = cookies.get("classId");
      cookies.remove("assignmentId");

      String valid = "";

      try {
        Mutators.deleteAssignment(assignmentId);
        valid = "Success!";
      } catch (Exception e) {
        e.printStackTrace();
        valid = "Error deleting assignment.";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid, "classId", classId);
      return GSON.toJson(responseObject);
    }
  }

  /**
   * Post request handler for updating checkoff.
   */
  public static class UpdateCheckoffHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String assignmentID = cookies.get("assignmentId");
      QueryParamsMap qmap = req.queryMap();

      List<String> completed = GSON.fromJson(qmap.value("complete"), ArrayList.class);
      List<String> notCompleted = GSON.fromJson(qmap.value("notComplete"), ArrayList.class);

      String valid = "ERROR: Unknown";

      try {
        for (String s : completed) {
          Mutators.completeAssignment(s, assignmentID);
        }
        for (String s : notCompleted) {
          Mutators.uncompleteAssignment(s, assignmentID);
        }
        valid = "Update successful";
      } catch (Exception e) {
        valid = e.toString();
        e.printStackTrace();
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }
}
