package com.jambit;

import static org.junit.jupiter.api.Assertions.*;

import com.jambit.domain.MoodEntries;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*
TODO:
   - More Tests
   - Implement H2
   - Delete Database content after all tests
 */

class DatabaseConnectionTest {
  private static DatabaseConnection databaseConnection;

  @BeforeAll
  static void init() throws IOException, SQLException {
    DatabaseConnection.changePropertyPath("config/appTest.properties");
    databaseConnection = DatabaseConnection.getInstance();
  }

  @Test
  public void insertTest() throws SQLException {
    MoodEntries testValue =
        new MoodEntries(0, (int) Math.round(Math.random() * 10), System.currentTimeMillis() / 1000);
    databaseConnection.writeMoodEntries(testValue);
    ArrayList<MoodEntries> dbOutputValues = databaseConnection.fetchAllMoodEntries();
    assertEquals(dbOutputValues.get(dbOutputValues.size() - 1).time, testValue.time);
    assertEquals(dbOutputValues.get(dbOutputValues.size() - 1).vote, testValue.vote);
  }
}
