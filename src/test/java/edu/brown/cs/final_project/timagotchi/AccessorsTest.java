package edu.brown.cs.final_project.timagotchi;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Checkoff;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.assignments.Review;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AccessorsTest {
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
    String r1 = Accessors.getRecord("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6",
            "cb85fe08-2ff0-4239-b49e-aefede092ff6");
    String r2 = Accessors.getRecord("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6",
            "d14ade97-1f3b-4cc9-b1e6-20d95f619c65");
    String r3 = Accessors.getRecord("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6", "fake-question");
    String r4 = Accessors.getRecord("fake-student", "cb85fe08-2ff0-4239-b49e-aefede092ff6");

    assertEquals("a", r1);
    assertEquals("d", r2);
    assertNull(r3);
    assertNull(r4);
    tearDown();
  }

  /**
   * Test for the getWrongQuestionIDs function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getWrongQuestionIDsTest() throws Exception {
    setUp();
    List<String> qs1 = Accessors.getWrongQuestionIDs("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6",
            "00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    List<String> qs2 = Accessors.getWrongQuestionIDs("1148bc88-9a4b-40f4-acce-0c86ba1f67d9",
            "00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    String r3 = Accessors.getRecord("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6", "fake-class");
    String r4 = Accessors.getRecord("fake-student", "00cfee5e-c9f0-4420-96ea-b94cd36f289d");

    assertEquals(1, qs1.size());
    assertEquals("d14ade97-1f3b-4cc9-b1e6-20d95f619c65", qs1.get(0));
    assertEquals(2, qs2.size());
    assertTrue(qs1.get(0).equals("d14ade97-1f3b-4cc9-b1e6-20d95f619c65")
            || qs1.get(0).equals("cb85fe08-2ff0-4239-b49e-aefede092ff6"));
    assertTrue(qs2.get(1).equals("d14ade97-1f3b-4cc9-b1e6-20d95f619c65")
            || qs2.get(1).equals("cb85fe08-2ff0-4239-b49e-aefede092ff6"));
    assertNull(r3);
    assertNull(r4);
    tearDown();
  }

  /**
   * Test for the getAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getAssignmentTest() throws Exception {
    setUp();
    Assignment a1 = Accessors.getAssignment("f2173437-7503-4b20-896f-997bae7d1173");
    Assignment a2 = Accessors.getAssignment("864e94ad-08c8-4c96-b78c-0399644d0334");
    Assignment a3 = Accessors.getAssignment("8ea55235-c480-4d0b-b229-b70968763874");
    Assignment a4 = Accessors.getAssignment("792167b6-b4bc-454d-9da6-812eed5141b6");
    Assignment a5 = Accessors.getAssignment("fake-assignment");

    assertTrue(a1.getName().equals("midterm1"));
    assertTrue(a1.getReward() == 10);
    assertTrue(a1 instanceof Quiz);

    assertTrue(a2.getName().equals("test"));
    assertTrue(a2.getReward() == 50);
    assertTrue(a2 instanceof Quiz);

    assertTrue(a3.getName().equals("checkoff1"));
    assertTrue(a3.getReward() == 5);
    assertTrue(a3 instanceof Checkoff);

    assertTrue(a4.getName().equals("Review"));
    assertTrue(a4.getReward() == 100);
    assertTrue(a4 instanceof Review);

    assertNull(a5);
    tearDown();
  }

  /**
   * Test for the getLeaderboard function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getLeaderboardTest() throws Exception {
    setUp();
    Userboard l1 = Accessors.getLeaderboard("00cfee5e-c9f0-4420-96ea-b94cd36f289d");

    List<Student> sts = l1.getRanking();
    assertEquals(2, sts.size());
    assertTrue(sts.get(0).getName().equals("student"));
    assertTrue(sts.get(1).getName().equals("student"));
    tearDown();
  }
}
