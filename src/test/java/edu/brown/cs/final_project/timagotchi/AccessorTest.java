package edu.brown.cs.final_project.timagotchi;

import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessorTest {
  /**
   * Set up with test.sqlite3
   *
   * @throws Exception Exception.
   */
  @Before
  public void setUp() throws Exception {
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
   * Test for the getRecord function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getRecordTest() throws Exception {
    setUp();
    /*String r = Controller.getRecord("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6",
            "cb85fe08-2ff0-4239-b49e-aefede092ff6");
    System.out.println(r);*/
    tearDown();
  }
}
