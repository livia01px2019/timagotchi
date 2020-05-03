package edu.brown.cs.final_project.timagotchi.assignments;

import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class QuestionTest {
  Question q1;
  Question q2;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    q1 = new Question("0", "This is Question1", new ArrayList<>(Arrays.asList("a", "b", "c", "d")),
            new ArrayList<>(Arrays.asList(1)));
    q2 = new Question("1", "This is Question2", new ArrayList<>(Arrays.asList("a", "b", "c", "d")),
            new ArrayList<>(Arrays.asList(1)));
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    q1 = null;
    q2 = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the comparator that compares questions by score works correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void compareByScore() throws Exception {
    setUp();
    q1.setScore(10.0);
    q2.setScore(20.0);
    List<Question> qs = new ArrayList<>(Arrays.asList(q1, q2));
    Collections.sort(qs, new Question.CompareByScore());

    assertTrue(qs.get(0).equals(q2));
    assertTrue(qs.get(1).equals(q1));
    tearDown();
  }

  /**
   * Test that makes sure the prompt is being split correctly.
   *
   * @throws Exception Exception.
   */
  public void promptSplitTest() throws Exception {
    setUp();
    String[] p = q1.getPromptSplit();
    assertTrue(p.length == 3);
    assertTrue(p[0].equals("this"));
    assertTrue(p[1].equals("is"));
    assertTrue(p[2].equals("question1"));
  }
}
