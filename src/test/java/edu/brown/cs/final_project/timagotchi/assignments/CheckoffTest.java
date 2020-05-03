package edu.brown.cs.final_project.timagotchi.assignments;

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

public class CheckoffTest {
  Checkoff c;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    c = new Checkoff("0", "test", 100);
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    c = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the checkoff assignment is tracking completeness correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getCompleteTest() throws Exception {
    setUp();
    c.setComplete("0", true);
    c.setComplete("1", true);
    c.setComplete("1", false);

    assertTrue(c.getComplete("0"));
    assertFalse(c.getComplete("1"));
    tearDown();
  }

  /**
   * Test that makes sure the checkoff assignment score is being calculated correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getScoreTest() throws Exception {
    setUp();
    c.setComplete("0", true);
    c.setComplete("1", true);
    c.setComplete("1", false);

    assertTrue(c.getScore("0") == 1);
    assertTrue(c.getScore("1") == 0);
    tearDown();
  }
}
