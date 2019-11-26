package com.jambit.services.moodmeter;

import com.jambit.DatabaseConnection;
import com.jambit.domain.MoodEntry;
import com.jambit.services.Service;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoodMeterAverageService implements Service {

  private float time = 0;
  private static MoodMeterAverageService instance = null;

  private MoodMeterAverageService() {}

  public Object run() throws IOException, SQLException {
    DatabaseConnection db = DatabaseConnection.getInstance();
    return calculateMoodAverage(db.fetchMoodEntries(time));
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
      entrySumm = entrySumm + moodEntries.get(i).vote;
    }
    float average = (float) (Math.round((entrySumm / entryCount) * 100)) / 100;
    return average;
  }

  public void setTime(float time) {
    this.time = time;
  }
}
