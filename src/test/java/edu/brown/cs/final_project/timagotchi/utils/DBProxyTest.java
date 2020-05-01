package edu.brown.cs.final_project.timagotchi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBProxyTest {

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
   * Test that DBProxy connection is true.
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Test
  public void connectionTrueTest() throws ClassNotFoundException, SQLException {
    setUp();
    assertTrue(DBProxy.isConnected());
    tearDown();
  }

  /**
   * Test with a generic SQL Query.
   *
   * @throws Exception Exception.
   */
  @Test
  public void executeQueryTest() throws Exception {
    setUp();
    // generic query
    List<List<String>> result = DBProxy.executeQuery("SELECT * FROM classes;");
    for (List<String> entry : result) {
      assertEquals("15238a4d-8610-441f-8792-775363e5d39b", entry.get(0));
      assertEquals("cs32", entry.get(1));
    }
    tearDown();
  }

}