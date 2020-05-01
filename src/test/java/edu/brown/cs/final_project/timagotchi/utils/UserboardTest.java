package edu.brown.cs.final_project.timagotchi.utils;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;

public class UserboardTest {

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
   * Test with getRankingScore with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getRankingScoreTest() throws Exception {
    setUp();
    Userboard u = new Userboard("15238a4d-8610-441f-8792-775363e5d39b");
    List<List<String>> s = u.getRankingScore("f2173437-7503-4b20-896f-997bae7d1173");
    assertEquals("s2", s.get(0).get(0));
    assertEquals("1", s.get(0).get(1));
    assertEquals("s3", s.get(1).get(0));
    assertEquals("1", s.get(1).get(1));
    assertEquals("s1", s.get(2).get(0));
    assertEquals("0", s.get(2).get(1));
    tearDown();
  }

}
