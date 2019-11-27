package com.jambit;

import static org.junit.jupiter.api.Assertions.*;

import com.jambit.domain.MoodEntry;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.junit.jupiter.api.*;

/*
TODO:
   - Implement H2 (optional)
 */

class DatabaseConnectionTest {
  private static DatabaseConnection databaseConnection;

  @BeforeAll
  static void init() throws IOException, SQLException {
    DatabaseConnection.setPropertyPath("config/appTest.properties");
    databaseConnection = DatabaseConnection.getInstance();
  }

  @Test
  @Order(1)
  public void writeMoodEntry_Persist_ReturnSingleEntry() throws SQLException {
    MoodEntry expected = generateMoodEntryTestData(1).get(0);
    databaseConnection.writeMoodEntry(expected);
    ArrayList<MoodEntry> actual = databaseConnection.fetchAllMoodEntries();
    assertEquals(1,actual.size());
    assertTrue(actual.get(0).checkEquals(expected));
  }

  @Test
  @Order(2)
  public void filterMoodEntries_ReturnListOfAllEntriesInTimeSpan() throws SQLException {
    long currentTime = System.currentTimeMillis() / 1000L;
    float hours = (int) Math.round(Math.random() * 10 + 1);
    long randomTime = (long) (currentTime - (hours * 60 * 60));
    ArrayList<MoodEntry> expected = new ArrayList<>();

    for (MoodEntry moodEntry : generateMoodEntryTestData(100)) {
      databaseConnection.writeMoodEntry(moodEntry);
      if (moodEntry.time <= currentTime && moodEntry.time >= randomTime) {
        expected.add(moodEntry);
      }
    }

    ArrayList<MoodEntry> actual = databaseConnection.fetchMoodEntries(hours);

    assertEquals(actual.size(), expected.size());
    for (int i = 0; i < actual.size(); i++) {
      assertTrue(expected.get(i).checkEquals(actual.get(i)));
    }
  }

  @Test
  public void singletonDatabase_ReturnSameObjectID() throws IOException, SQLException {
    DatabaseConnection newInstance = DatabaseConnection.getInstance();
    assertEquals(newInstance, databaseConnection);
  }

  private ArrayList<MoodEntry> generateMoodEntryTestData(int amount) {
    ArrayList<MoodEntry> testData = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      testData.add(
          new MoodEntry(
              null,
              (int) Math.round(Math.random() * 10),
              (System.currentTimeMillis() / 1000) - Math.round(Math.random() * 10)));
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
