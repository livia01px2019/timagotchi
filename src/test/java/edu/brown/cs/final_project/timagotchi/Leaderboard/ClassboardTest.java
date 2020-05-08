package edu.brown.cs.final_project.timagotchi.Leaderboard;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;

public class ClassboardTest {
  private Classboard u;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    List<String> s = new ArrayList<>();
    s.add("15238a4d-8610-441f-8792-775363e5d39b");
    s.add("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    u = new Classboard(s);
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    DBProxy.disconnect();
    u = null;
  }

  /**
   * Test the getClassXP function of CompareByAvgXP with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void CompareByAvgXPTest() throws Exception {
    Classboard.CompareByAvgXP compare = new Classboard.CompareByAvgXP();
    Class c1 = Accessors.getClass("15238a4d-8610-441f-8792-775363e5d39b");
    assertEquals(compare.getClassXP(c1), 11);
    Class c2 = Accessors.getClass("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    assertEquals(compare.getClassXP(c2), 50);
  }

  /**
   * Test the compare function of CompareByAvgXPTest with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void CompareTest() throws Exception {
    Classboard.CompareByAvgXP compare = new Classboard.CompareByAvgXP();
    Class c1 = Accessors.getClass("15238a4d-8610-441f-8792-775363e5d39b");
    Class c2 = Accessors.getClass("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    assertEquals(compare.compare(c1, c2), -1);
  }

  /**
   * Test with rankClassByTotalXP with a test database.
   *
   * @throws Exception Exception.
   */
  @Test
  public void rankClassByTotalXPTest() throws Exception {
    setUp();
    List<List<String>> s = u.rankClassByTotalXP();
    assertEquals(s.get(0).get(0), "c1");
    assertEquals(s.get(0).get(1), "50.0");
    assertEquals(s.get(1).get(0), "cs32");
    assertEquals(s.get(1).get(1), "11.0");
    tearDown();
  }

}
