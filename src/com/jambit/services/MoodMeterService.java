package com.jambit.services;

import com.jambit.domain.MoodEntries;

import java.util.ArrayList;

public class MoodMeterService implements Service {

    private float time = 0;

    public Object run() {
        return calculateMoodAverage(new ArrayList<MoodEntries>());
    }


    private float calculateMoodAverage(ArrayList<MoodEntries> moodEntries) {
        float entryCount = moodEntries.size();
        float entrySumm = 0;
        for (int i = 0; i < entryCount; i++) {
            entrySumm = entrySumm + moodEntries.get(i).vote;
        }
        float average = (float)(Math.round((entrySumm / entryCount) * 100)) / 100;
        return average;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
