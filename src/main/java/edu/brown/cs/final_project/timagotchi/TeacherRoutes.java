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
public class TeacherRoutes {

  private static final Gson GSON = new Gson();

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
      String className = Controller.getClass(classId).getName();
      String classesHtml = Routes.generateClassSidebar(cookies);
      Userboard userboard = Controller.getLeaderboard(classId);
      String leaderBoardHtml = Routes.generateUserboardHtml(userboard);
      Map<String, Object> variables = ImmutableMap.of("title", "Timagotchi: Teacher Class",
          "classes", classesHtml, "className", className, "classId", classId, "leaderboard",
          leaderBoardHtml);
      return new ModelAndView(variables, "teacher-class.ftl");
    }
  }

  public static class TeacherClassGetHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classId = cookies.get("classId");
      QueryParamsMap qmap = req.queryMap();
      if (qmap.value("type").equals("assignments")) {
        List<String> assignmentIds = Controller.getClass(classId).getAssignmentIds();
        System.out.println("assignments");
        System.out.println(assignmentIds);
        List<String> assignmentNames = new ArrayList<>();
        for (String id : assignmentIds) {
          assignmentNames.add(Controller.getAssignment(id).getName());
        }
        System.out.println("in assignments");
        Map<String, Object> responseObject = ImmutableMap.of("ids", assignmentIds, "names",
            assignmentNames);
        System.out.println(responseObject);
        return GSON.toJson(responseObject);
      }
      return null;
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
      String assignmentId = req.params(":assignmentid");
      cookies.set("assignmentId", assignmentId);
      Class classObject = Controller.getClass(cookies.get("classId"));
      System.out.println("students from class:" + classObject.getStudentIds());

      Assignment assignment = Controller.getAssignment(assignmentId);
      System.out.println("assignment id" + assignmentId);

      JsonArray students = new JsonArray();
      for (String studentId : classObject.getStudentIds()) {
        Student currStudent = Controller.getStudent(studentId);
        System.out.println("a student added");
        JsonObject student = new JsonObject();
        student.addProperty("id", studentId);
        student.addProperty("name", currStudent.getName());
        student.addProperty("score", assignment.getScore(studentId));
        students.add(student);
      }
      System.out.println("students: " + students);
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
      Teacher currTeacher = Controller.getTeacher(Controller.getTeacherIDFromUsername(username));
      Classboard cb = new Classboard(currTeacher.getClassIds());
      String leaderboardHtml = Routes.generateClassboardHtml(cb);
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

  public static class CreateNewAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String classID = cookies.get("classId");
      System.out.println(classID);
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
          quizList.add(classID);
          quizList.add(title);
          quizList.add(points);
          if (isCheckoff.equals("true")) {
            Checkoff assignment = Controller.addCheckoffAssignment(quizList);
            assignmentID = assignment.getId();
            valid = "Assignment successfully created!";
          } else if (isQuiz.equals("true")) {
            if (competitive.equals("true")) {
              quizList.add("competitive");
            } else {
              quizList.add("regular");
            }
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
                valid = "At least one question is missing the prompt, correct answer, or answer choices";
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
                  String questionString = question + " " + firstAnswer + " " + secondAnswer + " "
                      + thirdAnswer + " " + fourthAnswer + " " + correctNum + " ";
                  System.out.println(questionString);
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
                Question q = Controller.addQuestion(qList);
                quizList.add(q.getId());
              }
              Quiz assignment = Controller.addQuizAssignment(quizList);
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

  public static class SubmitTeacherNewClassHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String name = qmap.value("name");
      Class newClass = Controller.createClassCommand(name,
          Controller.getTeacherIDFromUsername(cookies.get("username")));
      Controller.addReviewAssignment(newClass.getId() + " Review 100");

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

  public static class DeleteAssignmentHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      System.out.println("delete assignment called");
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      QueryParamsMap qmap = req.queryMap();
      String assignmentId = cookies.get("assignmentId");
      String classId = cookies.get("classId");
      cookies.remove("assignmentId");

      String valid = "";

      try {
        Controller.deleteAssignment(assignmentId);
        valid = "Success!";
      } catch (Exception e) {
        e.printStackTrace();
        valid = "Error deleting assignment.";
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid, "classId", classId);
      System.out.println("i am here!!");
      return GSON.toJson(responseObject);
    }
  }

  public static class UpdateCheckoffHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Cookies cookies = Cookies.initFromServlet(req.raw(), res.raw());
      String assignmentID = cookies.get("assignmentId");
      System.out.println(assignmentID);
      QueryParamsMap qmap = req.queryMap();

      List<String> completed = GSON.fromJson(qmap.value("complete"), ArrayList.class);
      List<String> notCompleted = GSON.fromJson(qmap.value("notComplete"), ArrayList.class);

      String valid = "ERROR: Unknown";

      try {
        for (String s : completed) {
          Controller.completeAssignment(s, assignmentID);
        }
        for (String s : notCompleted) {
          Controller.uncompleteAssignment(s, assignmentID);
        }
      } catch (Exception e) {
        valid = e.toString();
        e.printStackTrace();
      }

      Map<String, Object> responseObject = ImmutableMap.of("results", valid);
      return GSON.toJson(responseObject);
    }
  }

}
