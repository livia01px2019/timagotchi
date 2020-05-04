package edu.brown.cs.final_project.timagotchi.users;

import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TeacherTest {
  Teacher t;

  /**
   * Set up with test.sqlite3
   *
   * @throws Exception Exception.
   */
  @Before
  public void setUp() throws Exception {
    DBProxy.connect("data/test.sqlite3");
    t = new Teacher("0", "user", "pass", "test");
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    t = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the class ids the teacher belongs to is being kept track correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void classIdTest() throws Exception {
    setUp();
    t.addClassId("55");
    t.addClassId("hello");
    t.addClassId("이상한 letters");

    List<String> cs = t.getClassIds();
    assertEquals(cs.size(), 3);
    assertEquals(cs.get(0), "55");
    assertEquals(cs.get(1), "hello");
    assertEquals(cs.get(2), "이상한 letters");
  }
}
