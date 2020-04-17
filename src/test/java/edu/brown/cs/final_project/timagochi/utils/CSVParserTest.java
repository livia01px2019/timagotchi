package edu.brown.cs.final_project.timagochi.utils;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * CSV testing.
 */
public class CSVParserTest {

  /**
   * * Tests basic CSV reading.
   */
  /*@Test
  public void CSVTest() {
    assertNull(CSVParser.parse(new File("fileDoesn'tExist")));
    assertEquals(CSVParser.parse(new File("data/stars/student/empty-file.csv")), new ArrayList<>());
    // Correctly parses one line.
    List<String> clownList = new ArrayList<>();
    clownList.add("clowns are the superior race");
    assertEquals(CSVParser.parse(new File("data/stars/student/clown.csv")), clownList);
    // Correctly parses two lines. By induction, must correctly parse all lines.
    List<String> starRet = new ArrayList<>();
    starRet.add("StarID,ProperName,X,Y,Z");
    starRet.add("1,Lonely Star,5,-2.24,10.04");
    assertEquals(CSVParser.parse(new File("data/stars/one-star.csv")), starRet);
  }*/

}
