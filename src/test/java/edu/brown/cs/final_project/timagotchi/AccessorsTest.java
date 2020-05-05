package edu.brown.cs.final_project.timagotchi;

import edu.brown.cs.final_project.timagotchi.Leaderboard.Userboard;
import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Checkoff;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.assignments.Review;
import edu.brown.cs.final_project.timagotchi.pets.Pet;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Access;

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

  /**
   * Test for the getPet function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getPetTest() throws Exception {
    setUp();
    Pet p1 = Accessors.getPet("e06b4a1c-5f8f-4ec8-8e1b-89e13dcfdc6e");
    Pet p2 = Accessors.getPet("fake-pet");

    assertEquals(p1.getLevel(), 1);
    assertEquals(p1.getName(), "aaa");
    assertNull(p2);
    tearDown();
  }

  /**
   * Test for the getStudentPassword function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getStudentPasswordTest() throws Exception {
    setUp();
    String p1 = Accessors.getStudentPassword("st1");
    String p2 = Accessors.getStudentPassword("fake-user");

    assertEquals(p1, "68f1c007e6178d3457a5b3d7f46996868633ca0b5a5c27b5a008146388104c29");
    assertEquals(p2, "");
    tearDown();
  }

  /**
   * Test for the getTeacherPassword function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getTeacherPasswordTest() throws Exception {
    setUp();
    String p1 = Accessors.getTeacherPassword("t1");
    String p2 = Accessors.getTeacherPassword("fake-user");

    assertEquals(p1, "628b49d96dcde97a430dd4f597705899e09a968f793491e4b704cae33a40dc02");
    assertEquals(p2, "");
    tearDown();
  }

  /**
   * Test for the getStudent function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getStudentTest() throws Exception {
    setUp();
    Student s1 = Accessors.getStudent("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6");
    Student s2 = Accessors.getStudent("fake-student");

    assertEquals(s1.getClassIds().get(0), "00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    assertEquals(s1.getName(), "student");
    assertEquals(s1.getPetId(), "7988e490-be53-4eeb-8428-6aee876b87e5");
    assertNull(s2);
    tearDown();
  }

  /**
   * Test for the getTeacher function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getTeacherTest() throws Exception {
    setUp();
    Teacher t1 = Accessors.getTeacher("cb68062d-0870-4f6c-9658-4ac7112c9a5e");
    Teacher t2 = Accessors.getTeacher("fake-teacher");

    assertEquals(t1.getClassIds().get(0), "00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    assertEquals(t1.getName(), "teacher");
    assertEquals(t1.getUsername(), "t1");
    assertNull(t2);
    tearDown();
  }

  /**
   * Test for the getQuestion function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getQuestionTest() throws Exception {
    setUp();
    Question q1 = Accessors.getQuestion("d14ade97-1f3b-4cc9-b1e6-20d95f619c65");
    Question q2 = Accessors.getQuestion("fake-question");

    assertEquals(q1.getPrompt(), "hi mom");
    assertTrue(q1.getAnswers().get(0) == 0);
    assertEquals(q1.getChoices().size(), 4);
    assertEquals(q1.getChoices().get(0), "a");
    assertNull(q2);
    tearDown();
  }

  /**
   * Test for the getAllQuestions function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getAllQuestionsTest() throws Exception {
    setUp();
    List<Question> qs1 = Accessors.getAllQuestions("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    List<Question> qs2 = Accessors.getAllQuestions("fake-class");

    assertEquals(qs1.size(), 4);
    assertTrue(qs1.get(0).getPrompt().equals("hi mom") || qs1.get(0).getPrompt().equals("hi dad")
            || qs1.get(0).getPrompt().equals("different words") || qs1.get(0).getPrompt().equals("should not appear"));
    assertTrue(qs2.isEmpty());
    tearDown();
  }

  /**
   * Test for the getClass function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getClassTest() throws Exception {
    setUp();
    Class c1 = Accessors.getClass("00cfee5e-c9f0-4420-96ea-b94cd36f289d");
    Class c2 = Accessors.getClass("fake-class");

    assertEquals(c1.getName(), "c1");
    assertEquals(c1.getStudentIds().size(), 2);
    assertEquals(c1.getAssignmentIds().size(), 2);
    assertNull(c2);
    tearDown();
  }

  /**
   * Test for the checkValidClassID function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void checkValidClassIDTest() throws Exception {
    setUp();
    assertTrue(Accessors.checkValidClassID("00cfee5e-c9f0-4420-96ea-b94cd36f289d"));
    assertFalse(Accessors.checkValidClassID("fake-class"));
    assertFalse(Accessors.checkValidClassID("c1"));
    assertFalse(Accessors.checkValidClassID("792167b6-b4bc-454d-9da6-812eed5141b6"));
    tearDown();
  }

  /**
   * Test for the getStudentIDFromUsername function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getStudentIDFromUsernameTest() throws Exception {
    setUp();
    String id1 = Accessors.getStudentIDFromUsername("st1");
    String id2 = Accessors.getStudentIDFromUsername("fake-student");

    assertEquals(id1, "e82d9c61-d2f5-4671-a372-cc29d0f6f8b6");
    assertNull(id2);
    tearDown();
  }

  /**
   * Test for the getTeacherIDFromUsername function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getTeacherIDFromUsernameTest() throws Exception {
    setUp();
    String id1 = Accessors.getTeacherIDFromUsername("t1");
    String id2 = Accessors.getTeacherIDFromUsername("fake-teacher");

    assertEquals(id1, "cb68062d-0870-4f6c-9658-4ac7112c9a5e");
    assertNull(id2);
    tearDown();
  }

  /**
   * Test for the getAllClassIds function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getAllClassIds() throws Exception {
    setUp();
    List<String> cs = Accessors.getAllClassIds();

    assertEquals(2, cs.size());
    assertTrue(cs.get(0).equals("00cfee5e-c9f0-4420-96ea-b94cd36f289d")
            || cs.get(0).equals("15238a4d-8610-441f-8792-775363e5d39b"));
    assertTrue(cs.get(1).equals("00cfee5e-c9f0-4420-96ea-b94cd36f289d")
            || cs.get(1).equals("15238a4d-8610-441f-8792-775363e5d39b"));
    tearDown();
  }

  /**
   * Test for the getStudentsFromAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void getStudentsFromAssignment() throws Exception {
    setUp();
    List<String> sts = Accessors.getStudentsFromAssignment("792167b6-b4bc-454d-9da6-812eed5141b6");

    assertEquals(2, sts.size());
    assertTrue(sts.get(0).equals("1148bc88-9a4b-40f4-acce-0c86ba1f67d9")
            || sts.get(0).equals("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6"));
    assertTrue(sts.get(1).equals("1148bc88-9a4b-40f4-acce-0c86ba1f67d9")
            || sts.get(1).equals("e82d9c61-d2f5-4671-a372-cc29d0f6f8b6"));
    tearDown();
  }
}
