package com.jambit;

import com.jambit.domain.MoodEntries;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseConnection {

  private static DatabaseConnection databaseInstance = null;
  private static String PROPERTY_PATH = ("config/app.properties");
  private static Properties databaseProps = new Properties();

  private Connection activeDatabaseConnection;

  private DatabaseConnection() {}

  public static DatabaseConnection getInstance() throws IOException, SQLException {
    if (databaseInstance == null) {
      databaseInstance = new DatabaseConnection();
      databaseInstance.connect();
    }
    return databaseInstance;
  }

  /** Connects to the database */
  private void connect() throws SQLException, IOException {

    databaseProps.load(new FileInputStream(PROPERTY_PATH));
    System.out.println(
        "Connecting to: jdbc:mysql://"
            + databaseProps.getProperty("database.hostIP")
            + ":"
            + databaseProps.getProperty("database.port")
            + "/"
            + databaseProps.getProperty("database.databaseName"));
    activeDatabaseConnection =
        DriverManager.getConnection(
            "jdbc:mysql://"
                + databaseProps.getProperty("database.hostIP")
                + ":"
                + databaseProps.getProperty("database.port")
                + "/"
                + databaseProps.getProperty("database.databaseName"),
            databaseProps.getProperty("database.username"),
            databaseProps.getProperty("database.password"));
    System.out.println("Connection successful!!!");
  }

  /**
   * This function returns mood entries with a chosen timespan.
   *
   * @param t equals time in hours
   * @return An ArrayList with all entries in the chosen timespan
   */
  public ArrayList<MoodEntries> fetchMoodEntries(float t) throws SQLException {
    long currentTime = getCurrentTime();

    StringBuilder query =
        new StringBuilder()
            .append("SELECT * FROM ")
            .append(databaseProps.getProperty("table.moodMeter"))
            .append(" WHERE ")
            .append(databaseProps.getProperty("table.moodMeter.time"))
            .append(" BETWEEN ")
            .append(currentTime - (t * 60 * 60))
            .append(" AND ")
            .append(currentTime);

    return moodMeterSQLQuery(query.toString());
  }

  /**
   * This function returns all mood entries form the database.
   *
   * @return An ArrayList with all entries.
   */
  public ArrayList<MoodEntries> fetchAllMoodEntries() throws SQLException {
    StringBuilder query =
        new StringBuilder()
            .append("SELECT * FROM ")
            .append(databaseProps.getProperty("table.moodMeter"));
    return moodMeterSQLQuery(query.toString());
  }

  /**
   * Execute SQL command on the database for the moodMeter
   *
   * @param sql SQL command you would like to use
   * @return returns ArrayList with moodMeter entries
   */
  private ArrayList<MoodEntries> moodMeterSQLQuery(String sql) throws SQLException {
    ArrayList<MoodEntries> entries = new ArrayList<>();
    Statement st = activeDatabaseConnection.createStatement();
    ResultSet rs = st.executeQuery(sql);
    while (rs.next()) {
      entries.add(
          new MoodEntries(
              rs.getInt(databaseProps.getProperty("table.moodMeter.id")),
              rs.getInt(databaseProps.getProperty("table.moodMeter.entry")),
              rs.getLong(databaseProps.getProperty("table.moodMeter.time"))));
    }

    return entries;
  }

  /**
   * Write mood entries into the database
   *
   * @param input The object to use for the entry
   */
  public void writeMoodEntries(MoodEntries input) throws SQLException {
    StringBuilder query =
        new StringBuilder()
            .append("INSERT INTO ")
            .append(databaseProps.getProperty("table.moodMeter"))
            .append("(")
            .append(databaseProps.getProperty("table.moodMeter.entry"))
            .append(",")
            .append(databaseProps.getProperty("table.moodMeter.time"))
            .append(")")
            .append(" VALUES (")
            .append(input.vote)
            .append(",")
            .append(input.time)
            .append(")");

    Statement st = activeDatabaseConnection.createStatement();
    st.executeUpdate(query.toString());
  }

  /**
   * Get the Unix timestamp in seconds
   *
   * @return Unix timestamp in seconds
   */
  private long getCurrentTime() {
    return System.currentTimeMillis() / 1000L;
  }

  /**
   * Changes the path to the property file
   *
   * @param path the file path to set it to
   */
  public static void changePropertyPath(String path) throws IOException {
    PROPERTY_PATH = path;
    databaseProps.load(new FileInputStream(PROPERTY_PATH));
  }
}
