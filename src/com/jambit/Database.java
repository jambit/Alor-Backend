package com.jambit;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Database {
  public static Database database = null;

  private static final String PROPERTY_PATH = ("config/app.properties");
  private Properties catalogProps = new Properties();

  private Connection activeDatabaseConnection;

  public Database() throws MultipleConnectionException {
    if (database == null) {
      database = this;
    } else {
      throw new MultipleConnectionException("Limited to one connection");
    }
  }

  /** Connects to the database */
  private void connect() throws IOException, MultipleConnectionException, SQLException {
    if (database == null) {
      catalogProps.load(new FileInputStream(PROPERTY_PATH));
      activeDatabaseConnection =
          DriverManager.getConnection(
              "jdbc:mysql://"
                  + catalogProps.getProperty("database.hostIP")
                  + ":"
                  + catalogProps.getProperty("database.port")
                  + "/"
                  + catalogProps.getProperty("database.databaseName"),
              catalogProps.getProperty("database.username"),
              catalogProps.getProperty("database.password"));

    } else {
      throw new MultipleConnectionException("Limited to one connection");
    }
  }

  /**
   * This function returns mood entries. When t = -1 this function returns all entries.
   *
   * @param t
   * @return
   * @throws SQLException
   */
  public ArrayList fetchMoodEntries(float t) throws SQLException {
    long currentTime = getCurrentTime();
    ArrayList<Integer> entries = new ArrayList<>();
    StringBuilder query = new StringBuilder();
    query
        .append("SELECT * FROM ")
        .append(catalogProps.getProperty("table.moodMeter"))
        .append(" WHERE ")
        .append(catalogProps.getProperty("table.moodMeter.time"));

    if (t != -1) {
      query.append(" BETWEEN ").append(currentTime - (t * 120)).append(" AND ").append(currentTime);
    }

    Statement st = activeDatabaseConnection.createStatement();
    ResultSet rs = st.executeQuery(query.toString());
    while (rs.next()) {
      entries.add(rs.getInt(catalogProps.getProperty("table.moodMeter.entry")));
    }

    return entries;
  }

  public void writeMoodEntries(String input) throws SQLException, TimeOutException {
    checkValid();

    StringBuilder query =
        new StringBuilder()
            .append("INSERT INTO ")
            .append(catalogProps.getProperty("table.moodMeter"))
            .append("(")
            .append(catalogProps.getProperty("table.moodMeter.entry"))
            .append(",")
            .append(catalogProps.getProperty("table.moodMeter.time"))
            .append(")")
            .append(" VALUES (")
            .append(input)
            .append(",")
            .append(getCurrentTime())
            .append(")");

    Statement st = activeDatabaseConnection.createStatement();
    st.executeUpdate(query.toString());
  }

  public void basicWrite(String table, String columns, String data) {
    try {
      Statement st = activeDatabaseConnection.createStatement();
      st.executeUpdate("INSERT INTO " + table + "(" + columns + ") VALUES (" + data + ")");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private long getCurrentTime() {
    return System.currentTimeMillis() / 1000L;
  }

  private void checkValid() throws SQLException, TimeOutException {
    if (!activeDatabaseConnection.isValid(1)) {
      throw new TimeOutException("Connection to Database has been lost");
    }
  }

  public static class MultipleConnectionException extends Exception {
    public MultipleConnectionException(String errorMessage) {
      super(errorMessage);
    }
  }

  public static class TimeOutException extends Exception {
    public TimeOutException(String errorMessage) {
      super(errorMessage);
    }
  }
}
