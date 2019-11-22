package com.jambit;

import static org.junit.jupiter.api.Assertions.*;

import com.jambit.domain.MoodEntries;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/*
TODO:
   - More Tests
   - Delete Database content after all tests (@Afterall)
   - Implement H2 (optional)
 */

class DatabaseConnectionTest {
  private static DatabaseConnection databaseConnection;

  @BeforeAll
  static void init() throws IOException, SQLException {
    DatabaseConnection.changePropertyPath("config/appTest.properties");
    databaseConnection = DatabaseConnection.getInstance();
  }

  @Test
  @Order(1)
  public void insertTest() throws SQLException {
    MoodEntries testValue =
        new MoodEntries(0, (int) Math.round(Math.random() * 10), System.currentTimeMillis() / 1000);
    databaseConnection.writeMoodEntries(testValue);
    ArrayList<MoodEntries> dbOutputValues = databaseConnection.fetchAllMoodEntries();
    assertEquals(dbOutputValues.get(dbOutputValues.size() - 1).time, testValue.time);
    assertEquals(dbOutputValues.get(dbOutputValues.size() - 1).vote, testValue.vote);
  }

  @Test
  @Order(2)
  public void readTest() throws SQLException {
    long currentTime = System.currentTimeMillis() / 1000L;
    int hours = (int) Math.round(Math.random() * 10 + 1);
    long randomTime = currentTime - (hours * 60 * 60);

    ArrayList<MoodEntries> dbFilteredValues = databaseConnection.fetchMoodEntries(hours);
    ArrayList<MoodEntries> dbFilterOfAllValues = new ArrayList<>();
    {
      ArrayList<MoodEntries> dbAllValues = databaseConnection.fetchAllMoodEntries();
      for (int i = 0; i < dbAllValues.size(); i++) {
        if (dbAllValues.get(i).time <= currentTime && dbAllValues.get(i).time >= randomTime) {
          dbFilterOfAllValues.add(dbAllValues.get(i));
          dbAllValues.get(i);
        }
      }
    }

    assertEquals(dbFilteredValues.size(), dbFilterOfAllValues.size());
    for (int i = 0; i < dbFilteredValues.size(); i++) {
      assertTrue(dbFilterOfAllValues.get(i).checkEquals(dbFilteredValues.get(i)));
    }
  }

  @Test
  public void instanceTest() throws IOException, SQLException {
      DatabaseConnection newInstance = DatabaseConnection.getInstance();
      assertEquals(newInstance, databaseConnection);
  }
}
