package edu.brown.cs.final_project.timagotchi.users;

import edu.brown.cs.final_project.timagotchi.Accessors;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StudentTest {
  Student s;

  /**
   * Set up with test.sqlite3
   *
   * @throws Exception Exception.
   */
  @Before
  public void setUp() throws Exception {
    DBProxy.connect("data/test.sqlite3");
    s = new Student("0", "user", "pass", "test");
  }

  /**
   * Reset DBProxy.
   */
  @After
  public void tearDown() {
    s = null;
    DBProxy.disconnect();
  }

  /**
   * Test that makes sure the wrong questions the student got are being kept track of correctly.
   *
   * @throws Exception Exception.
   */
  @Test
  public void wrongQuestionIdTest() throws Exception {
    setUp();
    s.addClassId("c1");
    s.addClassId("c2");
    s.addWrongQuestionId("q1", "c1");
    s.addWrongQuestionId("q2", "c2");
    s.addWrongQuestionId("q3", "c2");
    s.addWrongQuestionId("q4", "c1");
    s.addWrongQuestionId("q4", "c2");

    Set<String> qsc1 = s.getWrongQuestionIds("c1");
    Set<String> qsc2 = s.getWrongQuestionIds("c2");

    assertEquals(qsc1.size(), 2);
    assertEquals(qsc2.size(), 3);
    assertTrue(qsc1.contains("q1"));
    assertFalse(qsc2.contains("q1"));
    assertTrue(qsc2.contains("q2"));
    assertFalse(qsc1.contains("q2"));
    assertTrue(qsc2.contains("q3"));
    assertFalse(qsc1.contains("q3"));
    assertTrue(qsc2.contains("q4"));
    assertTrue(qsc1.contains("q4"));
    tearDown();
  }

  /**
   * Test that makes sure students can be sorted by the experience levels of their pets.
   *
   * @throws Exception Exception.
   */
  @Test
  public void compareByXpTest() throws Exception {
    setUp();
    Student s1 = Accessors.getStudent("ae5feeb3-8331-4982-9f90-e4a35198596d");
    Student s2 = Accessors.getStudent("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6");
    List<Student> sts = new ArrayList<>(Arrays.asList(s1, s2));
    Collections.sort(sts, new Student.CompareByXp());

    assertTrue(sts.get(0).equals(s1));
    assertTrue(sts.get(1).equals(s2));
    tearDown();
  }

  /**
   * Test that makes sure students can be sorted by their score on a certain assignment.
   *
   * @throws Exception Exception.
   */
  @Test
  public void compareByScoreTest() throws Exception {
    setUp();
    Student s1 = Accessors.getStudent("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6");
    Student s2 = Accessors.getStudent("1148bc88-9a4b-40f4-acce-0c86ba1f67d9");
    List<Student> sts = new ArrayList<>(Arrays.asList(s1, s2));
    Collections.sort(sts, new Student.CompareByScore("864e94ad-08c8-4c96-b78c-0399644d0334"));

    assertEquals(sts.get(0), s1);
    assertEquals(sts.get(1), s2);
    tearDown();
  }
}
