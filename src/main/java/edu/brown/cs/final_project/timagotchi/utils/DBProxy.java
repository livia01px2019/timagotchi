package edu.brown.cs.final_project.timagotchi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Generic database proxy class, which handles db connection, query execution,
 * and caching.
 */
public final class DBProxy {
  private static Connection conn;

  /**
   * Private Constructor.
   */
  private DBProxy() {
  }

  /**
   * Connects to the database.
   *
   * @param filename File to connect DB to.
   * @throws SQLException           Exception.
   * @throws ClassNotFoundException Exception.
   */
  public static void connect(String filename) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Queries for DB connection.
   *
   * @return True if DB connected, false if not.
   */
  public static boolean isConnected() {
    return conn != null;
  }

  /**
   * Disconnects the DB.
   */
  public static void disconnect() {
    conn = null;
  }

  /**
   * Executes SQL Query directly.
   *
   * @param sqlCommand SQL Command.
   * @return SQL output.
   * @throws Exception Exception.
   */
  public static List<List<String>> executeQuery(String sqlCommand) throws Exception {
    if (isConnected()) {
      // Create a prepared statement.
      PreparedStatement prep = null;
      List<List<String>> result = null;
      try {
        prep = conn.prepareStatement(sqlCommand);
        ResultSet rs = prep.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        result = new ArrayList<>();
        // Create an array of array of strings from the DB.
        while (rs.next()) {
          List<String> row = new ArrayList<>();
          for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
            row.add("" + rs.getObject(i));
          }
          result.add(row);
        }
      } catch (SQLException e) {
        System.err.println("ERROR: SQL Exception Error.");
      } finally {
        // Close the prepared statement.
        prep.close();
        return result;
      }
    } else {
      throw new Exception("ERROR: Database not connected.");
    }
  }

  /**
   * Executes SQL Query through Parameters.
   *
   * @param sqlCommand SQL Command.
   * @param parameters Parameters to be placed into SQL Command.
   * @return SQL Output.
   * @throws Exception Exception.
   */
  public static List<List<String>> executeQueryParameters(String sqlCommand,
      List<String> parameters) throws Exception {
    if (isConnected()) {
      // Creates a prepared statement.
      PreparedStatement prep = null;
      List<List<String>> result = null;
      try {
        prep = conn.prepareStatement(sqlCommand);
        for (int i = 0; i < parameters.size(); i++) {
          prep.setString(i + 1, parameters.get(i));
        }
        ResultSet rs = prep.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        result = new ArrayList<>();
        // Create an array of array of strings from the DB.
        while (rs.next()) {
          ArrayList<String> row = new ArrayList<>();
          for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
            row.add("" + rs.getObject(i));
          }
          result.add(row);
        }
      } catch (SQLException e) {
        System.err.println("ERROR: SQL Exception Error.");
      } finally {
        // Close the prepared statement.
        prep.close();
        return result;
      }
    } else {
      throw new Exception("ERROR: Database not connected.");
    }
  }

}
