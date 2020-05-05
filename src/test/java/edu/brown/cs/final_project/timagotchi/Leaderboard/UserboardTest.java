package edu.brown.cs.final_project.timagotchi.Leaderboard;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;

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

  /**
   * Test the getScore of CompareByAllXPTest with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getScoreTest() throws Exception {
    setUp();
    Userboard.CompareByAllXP comparator = new Userboard.CompareByAllXP(
        "15238a4d-8610-441f-8792-775363e5d39b");
    assertEquals(15, comparator.getScore("15238a4d-8610-441f-8792-775363e5d39b",
        "ae5feeb3-8331-4982-9f90-e4a35198596d"));
    assertEquals(10, comparator.getScore("15238a4d-8610-441f-8792-775363e5d39b",
        "36fafe9b-109f-4f3c-a185-31a544f2c172"));
    assertEquals(10, comparator.getScore("15238a4d-8610-441f-8792-775363e5d39b",
        "5b4aa906-e046-4c2f-87f1-111ba770673f"));
    tearDown();
  }

  /**
   * Test the compare function of CompareByAllXPTest with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void CompareByAllXPTest() throws Exception {
    setUp();
    Userboard.CompareByAllXP comparator = new Userboard.CompareByAllXP(
        "15238a4d-8610-441f-8792-775363e5d39b");
    // student 1 should be > than student 2
    assertEquals(1, comparator.compare(Accessors.getStudent("ae5feeb3-8331-4982-9f90-e4a35198596d"),
        Accessors.getStudent("36fafe9b-109f-4f3c-a185-31a544f2c172")));
    // student 1 should be > than student 3
    assertEquals(1, comparator.compare(Accessors.getStudent("ae5feeb3-8331-4982-9f90-e4a35198596d"),
        Accessors.getStudent("5b4aa906-e046-4c2f-87f1-111ba770673f")));
    // student 2 should be = to student 3
    assertEquals(0, comparator.compare(Accessors.getStudent("5b4aa906-e046-4c2f-87f1-111ba770673f"),
        Accessors.getStudent("36fafe9b-109f-4f3c-a185-31a544f2c172")));
    tearDown();
  }

  /**
   * Test with allAssignmentsXP with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void allAssignmentsXPTest() throws Exception {
    setUp();
    Userboard u = new Userboard("15238a4d-8610-441f-8792-775363e5d39b");
    List<List<String>> s = u.allAssignmentsXP();
    assertEquals(s.get(0).get(0), "s1");
    assertEquals(s.get(0).get(1), "15");
    assertEquals(s.get(1).get(0), "s2");
    assertEquals(s.get(1).get(1), "10");
    assertEquals(s.get(2).get(0), "s3");
    assertEquals(s.get(2).get(1), "10");
    // another class
    Userboard b = new Userboard("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    List<List<String>> s2 = b.allAssignmentsXP();
    assertEquals(s2.get(0).get(0), "student");
    assertEquals(s2.get(0).get(1), "50");
    assertEquals(s2.get(1).get(0), "student");
    assertEquals(s2.get(1).get(1), "50");
    tearDown();
  }

}
