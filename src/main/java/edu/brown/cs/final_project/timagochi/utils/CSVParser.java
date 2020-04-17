package edu.brown.cs.final_project.timagochi.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

/**
 * The CSV Parser. Contains one static method that returns a list of Strings
 * in the CSV file.
 */
public final class CSVParser {
  private CSVParser() { }

  /**
   * Parses the given file.
   *
   * @param file The file to be read.
   * @return A List of the CSV rows as Strings. No processing is done.
   */
  public static List<String> parse(File file) {
    try {
      List<String> ret = new ArrayList<>();
      BufferedReader reader = new BufferedReader(new FileReader(file));
      // Loop until there are no more lines.
      while (true) {
        String out = reader.readLine();
        if (out != null) {
          ret.add(out);
        } else {
          break;
        }
      }
      reader.close();
      return ret;
    } catch (FileNotFoundException err) {
      System.err.println("ERROR: File does not exist.");
    } catch (IOException err) {
      System.err.println("ERROR: Reading failed.");
    }
    return null;
  }
}
