package edu.brown.cs.final_project.timagotchi.pets;

import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class PetTest {
  Pet p;

  /**
   * Set up with test.sqlite3
   *
   * @throws ClassNotFoundException Exception.
   * @throws SQLException           Exception.
   */
  @Before
  public void setUp() throws ClassNotFoundException, SQLException {
    DBProxy.connect("data/test.sqlite3");
    p = new Pet("0", "test");
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    p = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the image linked to pet is correct.
   *
   * @throws Exception Exception.
   */
  @Test
  public void imageTest() throws Exception {
    setUp();
    String path = p.getImage();
    assertTrue(path.equals("../img/skin1.png") || path.equals("../img/skin2.png")
            || path.equals("../img/skin3.png") || path.equals("../img/skin4.png")
            || path.equals("../img/skin5.png") || path.equals("../img/skin6.png")
            || path.equals("../img/skin7.png"));
    tearDown();
  }
}
