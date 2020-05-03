package edu.brown.cs.final_project.timagotchi.assignments;

import edu.brown.cs.final_project.timagotchi.Controller;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuizTest {
  Question q1;
  Question q2;
  Quiz q;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    q1 = new Question("0", "question1", new ArrayList<>(Arrays.asList("a", "b", "c", "d")),
            new ArrayList<>(Arrays.asList(1)));
    q2 = new Question("1", "question2", new ArrayList<>(Arrays.asList("a", "b", "c", "d")),
            new ArrayList<>(Arrays.asList(1)));
    q = new Quiz("0", "test", 100, new ArrayList<>(Arrays.asList(q1, q2)), true);
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    q1 = null;
    q2 = null;
    q = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the record of student answers is being updated correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void setRecordTest() throws Exception {
    setUp();
    q.setRecord("0", 0, true);
    q.setRecord("0", 1, false);
    q.setRecord("1", 0, false);

    List<Boolean> r1 = q.getRecord("0");
    assertTrue(r1.get(0));
    assertFalse(r1.get(1));

    List<Boolean> r2 = q.getRecord("1");
    assertEquals(1, r2.size());
    tearDown();
  }

  /**
   * Test that makes sure the score of a student on the quiz is being calculated correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getScoreTest() throws Exception {
    setUp();
    q.setRecord("0", 0, true);
    q.setRecord("0", 1, false);
    q.setRecord("1", 0, false);

    assertTrue(q.getScore("0") == 1);
    assertTrue(q.getScore("1") == 0);
    assertTrue(q.getTotalScore() == 2);
    tearDown();
  }
}
