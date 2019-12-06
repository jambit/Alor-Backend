package com.jambit.services.moodmeter;

import com.jambit.DatabaseConnection;
import com.jambit.domain.MoodEntry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoodMeterAverageService {

  private float time = 0;
  private static MoodMeterAverageService instance = null;

  private MoodMeterAverageService() {}

  public Object run() throws SQLException, IOException, ClassNotFoundException {
    DatabaseConnection connection = DatabaseConnection.getInstance();

    return calculateMoodAverage(connection.fetchMoodEntries(time));
  }

  public static MoodMeterAverageService getInstance() {
    if (instance == null) {
      instance = new MoodMeterAverageService();
    }
    return instance;
  }

  private float calculateMoodAverage(ArrayList<MoodEntry> moodEntries) {
    float entryCount = moodEntries.size();
    float entrySumm = 0;
    for (int i = 0; i < entryCount; i++) {
      entrySumm += moodEntries.get(i).getVote();
    }
    float average = (float) (Math.round((entrySumm / entryCount) * 100)) / 100;
    return average;
  }

  public void setTime(float time) {
    this.time = time;
  }
}
