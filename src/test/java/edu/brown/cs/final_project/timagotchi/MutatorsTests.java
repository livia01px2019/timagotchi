package edu.brown.cs.final_project.timagotchi;

import edu.brown.cs.final_project.timagotchi.assignments.Assignment;
import edu.brown.cs.final_project.timagotchi.assignments.Question;
import edu.brown.cs.final_project.timagotchi.assignments.Quiz;
import edu.brown.cs.final_project.timagotchi.pets.Pet;
import edu.brown.cs.final_project.timagotchi.users.Class;
import edu.brown.cs.final_project.timagotchi.users.Student;
import edu.brown.cs.final_project.timagotchi.users.Teacher;
import edu.brown.cs.final_project.timagotchi.utils.DBProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MutatorsTests {
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
   * Test for the createTeacherCommand function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void createTeacherTest() throws Exception {
    setUp();
    Teacher t1 = Mutators.createTeacherCommand("user", "pass", "teacher");
    assertNull(t1);
    tearDown();
  }

  /**
   * Test for the createClassCommand function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void createClassTest() throws Exception {
    setUp();
    Class c1 = Mutators.createClassCommand("class", "id");
    Class c2 = Mutators.createClassCommand("", "");

    assertEquals("class", c1.getName());
    assertEquals(1, c1.getTeacherIds().size());
    assertEquals("id", c1.getTeacherIds().get(0));
    assertEquals("", c2.getName());
    assertEquals(1, c2.getTeacherIds().size());
    assertEquals("", c2.getTeacherIds().get(0));
    tearDown();
  }

  /**
   * Test for the addPet function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addPetTest() throws Exception {
    setUp();
    Pet p1 = Mutators.addPet("stud1", "pet");
    Pet p2 = Mutators.addPet("", "");

    assertEquals(p1.getName(), "pet");
    assertEquals(p1.getLevel(), 1);
    assertTrue(p1.getXp() == 0);
    assertEquals(p2.getName(), "");
    assertEquals(p2.getLevel(), 1);
    assertTrue(p2.getXp() == 0);
    tearDown();
  }

  /**
   * Test for the addStudentRecord function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addStudentRecordTest() throws Exception {
    setUp();
    Assignment a = Mutators.addStudentRecord("ae5feeb3-8331-4982-9f90-e4a35198596d",
            "fake-assignment", "15238a4d-8610-441f-8792-775363e5d39b",
            new ArrayList<>(Arrays.asList("D")), new ArrayList<>(Arrays.asList("false")));

    assertNull(a);
    tearDown();
  }

  /**
   * Test for the completeAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void completeAssignmentTest() throws Exception {
    setUp();
    Assignment a = Mutators.completeAssignment("ae5feeb3-8331-4982-9f90-e4a35198596d",
            "fake-assignment");

    assertNull(a);
    tearDown();
  }

  /**
   * Test for the uncompleteAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void uncompleteAssignmentTest() throws Exception {
    setUp();
    Assignment a = Mutators.uncompleteAssignment("ae5feeb3-8331-4982-9f90-e4a35198596d",
            "fake-assignment");

    assertNull(a);
    tearDown();
  }

  /**
   * Test for the addStudentIDToClass function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addStudentIDToClassTest() throws Exception {
    setUp();
    Class c = Mutators.addStudentIDToClassCommand("c1", "s1");

    assertNull(c);
    tearDown();
  }

  /**
   * Test for the createStudentCommand function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void createStudentTest() throws Exception {
    setUp();
    Student s = Mutators.createStudentCommand("user", "pass", "student");

    assertNull(s);
    tearDown();
  }

  /**
   * Test for the addAssignmentToStudent and deleteAssignment functions.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addDeleteAssignmentTest() throws Exception {
    setUp();
    Assignment a1 = Mutators.addCheckoffAssignment("c1", "a1", "100");
    Mutators.addAssignmentToStudent("ae5feeb3-8331-4982-9f90-e4a35198596d", a1.getId());
    Mutators.deleteAssignment(a1.getId());

    assertEquals(a1.getName(), "a1");
    tearDown();
  }

  /**
   * Test for the addCheckoffAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addCheckoffAssignmentTest() throws Exception {
    setUp();
    Assignment a1 = Mutators.addCheckoffAssignment("c1", "a1", "100");
    Assignment a2 = Mutators.addCheckoffAssignment("", "", "");

    assertEquals(a1.getName(), "a1");
    assertEquals(a1.getReward(), 100);
    assertTrue(a1.getTotalScore() == 1);
    assertNull(a2);
    tearDown();
  }

  /**
   * Test for the addCheckoffAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addQuestionTest() throws Exception {
    setUp();
    Question q1 = Mutators.addQuestion(
            "prompt", "o1", "o2", "o3", "o4", "2");
    Question q2 = Mutators.addQuestion("", "", "", "", "", "");

    List<String> o1 = q1.getChoices();
    List<Integer> a1 = q1.getAnswers();
    assertEquals(o1.size(), 4);
    assertTrue(a1.get(0) == 2);
    assertNull(q2);
    tearDown();
  }

  /**
   * Test for the addReviewAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addReviewAssignmentTest() throws Exception {
    setUp();
    Assignment a1 = Mutators.addReviewAssignment("c1", "a1", "100");
    Assignment a2 = Mutators.addReviewAssignment("", "", "");

    assertEquals(a1.getName(), "a1");
    assertEquals(a1.getReward(), 100);
    assertTrue(a1.getTotalScore() == 10);
    assertNull(a2);
    tearDown();
  }

  /**
   * Test for the addQuizAssignment function.
   *
   * @throws Exception Exception.
   */
  @Test
  public void addQuizAssignmentTest() throws Exception {
    setUp();
    Assignment a1 = Mutators.addQuizAssignment("c1", "a1", "100",
            "competitive", new ArrayList<>(Arrays.asList("1", "2")));

    assertNull(a1);
    tearDown();
  }
}
