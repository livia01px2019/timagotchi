package edu.brown.cs.final_project.timagotchi.users;

import edu.brown.cs.final_project.timagotchi.Accessors;
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

public class ClassTest {
  Class c;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    c = new Class("0", "test", new ArrayList<>(Arrays.asList("0")));
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
   * Test that makes sure classes can be ranked according to average xp of a student.
   *
   * @throws Exception Exception.
   */
  @Test
  public void compareByXpTest() throws Exception {
    setUp();
    Class c1 = Accessors.getClass("15238a4d-8610-441f-8792-775363e5d39b");
    Class c2 = Accessors.getClass("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    List<Class> cs = new ArrayList<>(Arrays.asList(c1, c2));
    Collections.sort(cs, new Class.CompareByAverageXp());

    assertTrue(cs.get(0).equals(c1));
    assertTrue(cs.get(1).equals(c2));
    tearDown();
  }
}
