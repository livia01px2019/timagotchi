package edu.brown.cs.final_project.timagotchi;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import edu.brown.cs.final_project.timagotchi.utils.REPL;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private static Controller controller = new Controller();

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Runs the program and adds commands to REPL.
   */
  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    try {
      DBProxy.connect("data/working.sqlite3");
    } catch (Exception e) {
      System.err.println("Database not connected");
    }

//    if (options.has("gui")) {
    runSparkServer((int) options.valueOf("port"));
//    }

    // REPL Handling.
    REPL repl = new REPL(new InputStreamReader(System.in));
    // repl.addCommand("startup", new Command(controller::startUpCommand));
    // repl.addCommand("addTeacher", new Command(controller::createTeacherCommand));
//    repl.addCommand("addStudentToClass", new Command(controller::addStudentIDToClassCommand));
    // repl.addCommand("addStudent", new Command(controller::createStudentCommand));
//    repl.addCommand("addCheckoff", new Command(controller::addCheckoffAssignment));
//    repl.addCommand("addQuestion", new Command(controller::addQuestion));
//    repl.addCommand("addQuiz", new Command(controller::addQuizAssignment));
    repl.begin();
  }

  /**
   * Creates a Spark Engine.
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Runs the Spark Server.
   */
  private void runSparkServer(int port) {
    Spark.port(getHerokuAssignedPort());
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/login", new Routes.LoginHandler(), freeMarker);
    Spark.get("/register", new Routes.RegisterHandler(), freeMarker);
    Spark.post("/register-submit", new Routes.RegisterSubmitHandler());
    Spark.post("/login-student", new Routes.LoginStudentHandler());
    Spark.post("/login-teacher", new Routes.LoginTeacherHandler());
    Spark.get("/main", new Routes.MainHandler(), freeMarker);
    Spark.get("/logout", new Routes.LogoutHandler(), freeMarker);

    Spark.get("/student/assignment", new StudentRoutes.StudentAssignmentHandler(), freeMarker);
    Spark.get("/student/view-quiz/:id", new StudentRoutes.StudentQuizHandler(), freeMarker);
    Spark.get("/student/main", new StudentRoutes.StudentMainHandler(), freeMarker);
    Spark.get("/student/all-classes", new StudentRoutes.StudentLeaderboardHandler(), freeMarker);
    Spark.get("/student/new-class", new StudentRoutes.StudentNewClassHandler(), freeMarker);
    Spark.get("/student/:id", new StudentRoutes.StudentClassHandler(), freeMarker);
    Spark.get("/student", new StudentRoutes.StudentMainHandler(), freeMarker);
    Spark.post("/student/quiz-finished", new StudentRoutes.FinishedQuizHandler());
    Spark.post("/student/load-quiz/:id", new StudentRoutes.StudentAssignmentLoader());
    Spark.post("/student/finish-quiz/:id", new StudentRoutes.FinishedQuizHandler());
    Spark.post("/student-class-get", new StudentRoutes.StudentClassGetHandler());
    Spark.post("/student/submit-new-class", new StudentRoutes.SubmitStudentNewClassHandler());

    Spark.get("/teacher/create-assignment", new TeacherRoutes.TeacherNewAssignmentHandler(),
        freeMarker);
    Spark.get("/teacher/main", new TeacherRoutes.TeacherMainHandler(), freeMarker);
    Spark.get("/teacher/new-class", new TeacherRoutes.TeacherNewClassHandler(), freeMarker);
    Spark.get("/teacher/:id", new TeacherRoutes.TeacherClassHandler(), freeMarker);
    Spark.get("/teacher/viewAssignment/:assignmentid", new TeacherRoutes.TeacherAssignmentHandler(),
        freeMarker);
    Spark.get("teacher/viewAssignmentStudent/:studentid",
        new TeacherRoutes.TeacherAssignmentStudentHandler(), freeMarker);
    Spark.get("/teacher", new TeacherRoutes.TeacherMainHandler(), freeMarker);
    Spark.post("/teacher/create-assignment-submit", new TeacherRoutes.CreateNewAssignmentHandler());
    Spark.post("/teacher/submit-new-class", new TeacherRoutes.SubmitTeacherNewClassHandler());
    Spark.post("/teacher-class-get", new TeacherRoutes.TeacherClassGetHandler());
    Spark.post("/teacher/viewAssignment/delete-assignment",
        new TeacherRoutes.DeleteAssignmentHandler());
    Spark.post("/teacher/update-checkoff-submit", new TeacherRoutes.UpdateCheckoffHandler());
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
  }

}
