package com.jambit;

import static org.junit.jupiter.api.Assertions.*;

import com.jambit.domain.MoodEntry;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.*;

class DatabaseConnectionTest {
  private static DatabaseConnection databaseConnection;

  @BeforeAll
  static void init() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnection.setPropertyPath("config/appTest.properties");
    databaseConnection = DatabaseConnection.getInstance();

    if (DatabaseConnection.getDatabaseDriver() == DatabaseConnection.databaseDrivers.h2) {
      StringBuilder sql =
          new StringBuilder()
              .append(
                  "CREATE TABLE MoodMeter "
                      + "( ID INT NOT NULL auto_increment,"
                      + " Time INT NOT NULL,"
                      + " Vote INT NOT NULL"
                      + ");");
      databaseConnection
          .getActiveDatabaseConnection()
          .createStatement()
          .executeUpdate(sql.toString());
    }
  }

  @Test
  @Order(1)
  public void writeMoodEntry_Persist_ReturnSingleEntry() throws SQLException {
    MoodEntry expected = generateMoodEntryTestData(1).get(0);
    databaseConnection.writeMoodEntry(expected);
    ArrayList<MoodEntry> actual = databaseConnection.fetchAllMoodEntries();
    assertEquals(1, actual.size());
    assertTrue(actual.get(0).checkEquals(expected));
  }

  @Test
  @Order(2)
  public void fetchMoodEntries_ReturnListOfAllEntriesInTimeSpan() throws SQLException {
    long currentTime = System.currentTimeMillis() / 1000L;
    float time = 5;
    Random random = new Random();

    ArrayList<MoodEntry> expected = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      long randomTime = currentTime - ((random.nextInt((int) time) + 1) * 60 * 60);
      MoodEntry moodEntry = new MoodEntry(5);
      moodEntry = databaseConnection.writeMoodEntry(moodEntry);
      if (randomTime >= currentTime - (time * 60 * 60)) {
        expected.add(moodEntry);
      }
    }

    ArrayList<MoodEntry> actual = databaseConnection.fetchMoodEntries(time);

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i).getId(), actual.get(i).getId());
    }
  }

  @Test
  public void singletonDatabase_ReturnSameObjectID() throws IOException, SQLException, ClassNotFoundException {
    DatabaseConnection newInstance = DatabaseConnection.getInstance();
    assertEquals(newInstance, databaseConnection);
  }

  private ArrayList<MoodEntry> generateMoodEntryTestData(int amount) {
    ArrayList<MoodEntry> testData = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      MoodEntry input = new MoodEntry((int) Math.round(Math.random() * 10));
      input.setTime((System.currentTimeMillis() / 1000) - Math.round(Math.random() * 10 * 60 * 60));
      testData.add(input);
    }

    return testData;
  }

  @AfterEach
  void deleteData() throws SQLException {
    StringBuilder sql = new StringBuilder();
    Connection connection = databaseConnection.getActiveDatabaseConnection();
    Statement st = connection.createStatement();

    sql.append("DELETE ")
        .append("FROM ")
        .append(DatabaseConnection.getDatabaseProps().getProperty("table.moodMeter"));
    System.out.println(sql);
    st.executeUpdate(sql.toString());
  }
}
