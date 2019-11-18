package com.jambit;

import org.junit.jupiter.api.*;

public class DatabaseTest {

  private Database db = new Database();

  @BeforeAll
  public static void init() {}

  @Test
  void basicTest() {
    db.basicWrite("moodMeter", "mood, time", "4, '2019-10-12 12:12:12'");
    db.basicRead("moodMeter");
    Assertions.assertTrue(true);
  }
}
