package com.jambit.services.moodmeter;

import com.jambit.domain.MoodEntries;
import com.jambit.services.Service;
import java.util.ArrayList;

public class MoodMeterDistributionService implements Service {

  private float time = 0;
  private static MoodMeterDistributionService instance = null;

  private MoodMeterDistributionService() {}

  public Object run() {
    return getDistribution(new ArrayList<MoodEntries>());
  }

  public static MoodMeterDistributionService getInstance() {
    if (instance == null) {
      instance = new MoodMeterDistributionService();
    }
    return instance;
  }

  private int[] getDistribution(ArrayList<MoodEntries> moodEntries) {
    int[] distro = new int[10];
    for (MoodEntries moodEntry : moodEntries) {
      if (moodEntry.vote > 0 && moodEntry.vote <= 10) {
        distro[moodEntry.vote - 1]++;
      }
    }
    return distro;
  }

  public void setTime(float time) {
    this.time = time;
  }
}