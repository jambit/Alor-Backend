package com.jambit;

import static org.junit.jupiter.api.Assertions.*;

import com.jambit.domain.MoodEntry;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/*
TODO:
   - Delete Database content after all tests (@Afterall)
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
  public void writeIntoDatabase() throws SQLException {
    MoodEntry expectedOutput =
        new MoodEntry(0, (int) Math.round(Math.random() * 10), System.currentTimeMillis() / 1000);
    databaseConnection.writeMoodEntry(expectedOutput);
    ArrayList<MoodEntry> actualOutput = databaseConnection.fetchAllMoodEntries();
    assertTrue(actualOutput.get(actualOutput.size() - 1).checkEquals(expectedOutput));
  }

  @Test
  @Order(2)
  public void readOutOfDatabase() throws SQLException {
    long currentTime = System.currentTimeMillis() / 1000L;
    float hours = (int) Math.round(Math.random() * 10 + 1);
    long randomTime = (long) (currentTime - (hours * 60 * 60));

    ArrayList<MoodEntry> actualFilteredValues = databaseConnection.fetchMoodEntries(hours);
    ArrayList<MoodEntry> expectedFilteredValues = new ArrayList<>();
    {
      ArrayList<MoodEntry> dbAllValues = databaseConnection.fetchAllMoodEntries();
      for (MoodEntry moodEntry : dbAllValues) {
        if (moodEntry.time <= currentTime && moodEntry.time >= randomTime) {
          expectedFilteredValues.add(moodEntry);
        }
      }
    }

    assertEquals(actualFilteredValues.size(), expectedFilteredValues.size());
    for (int i = 0; i < actualFilteredValues.size(); i++) {
      assertTrue(expectedFilteredValues.get(i).checkEquals(actualFilteredValues.get(i)));
    }
  }

  @Test
  public void SingletonObjectCreation() throws IOException, SQLException {
    DatabaseConnection newInstance = DatabaseConnection.getInstance();
    assertEquals(newInstance, databaseConnection);
  }
}
