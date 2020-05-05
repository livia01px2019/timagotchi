package edu.brown.cs.final_project.timagotchi.assignments;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ReviewTest {
  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the questions most similar to ones gotten wrong are what appear in Review,
   * with one question wrong.
   *
   * @throws Exception Exception.
   */
  @Test
  public void generateQuestionsOneWrongTest() throws Exception {
    setUp();
    String sid = Accessors.getStudentIDFromUsername("st1");
    Student s = Accessors.getStudent(sid);
    String cid = s.getClassIds().get(0);
    Review r = new Review("0", "test", 100);
    r.setNumQuestions(2);
    r.generateQuestions(sid, cid);
    List<Question> qs = r.getQuestions();

    assertTrue(qs.get(0).getPrompt().equals("hi mom") || qs.get(0).getPrompt().equals("hi dad"));
    assertTrue(qs.get(1).getPrompt().equals("hi mom") || qs.get(1).getPrompt().equals("hi dad"));
    assertFalse(qs.get(0).getPrompt().equals("different words") || qs.get(0).getPrompt().equals("should not appear"));
    assertFalse(qs.get(1).getPrompt().equals("different words") || qs.get(1).getPrompt().equals("should not appear"));
    tearDown();
  }

  /**
   * Test that makes sure the questions most similar to ones gotten wrong are what appear in Review,
   * with two questions wrong.
   *
   * @throws Exception Exception.
   */
  @Test
  public void generateQuestionsTwoWrongTest() throws Exception {
    setUp();
    String sid = Accessors.getStudentIDFromUsername("st2");
    Student s = Accessors.getStudent(sid);
    String cid = s.getClassIds().get(0);
    Review r = new Review("0", "test", 100);
    r.setNumQuestions(3);
    r.generateQuestions(sid, cid);
    List<Question> qs = r.getQuestions();

    assertTrue(qs.get(0).getPrompt().equals("hi mom")
            || qs.get(0).getPrompt().equals("hi dad")
            || qs.get(0).getPrompt().equals("different words"));
    assertTrue(qs.get(1).getPrompt().equals("hi mom")
            || qs.get(1).getPrompt().equals("hi dad")
            || qs.get(1).getPrompt().equals("different words"));
    assertTrue(qs.get(2).getPrompt().equals("hi mom")
            || qs.get(2).getPrompt().equals("hi dad")
            || qs.get(2).getPrompt().equals("different words"));
    assertNotEquals("should not appear", qs.get(0));
    assertNotEquals("should not appear", qs.get(1));
    assertNotEquals("should not appear", qs.get(2));
    tearDown();
  }
}
