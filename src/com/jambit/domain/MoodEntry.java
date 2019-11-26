package com.jambit.domain;

public class MoodEntry {
  public int id;
  public int vote;
  public long time;

  public MoodEntry(int id, int vote, long time) {
    this.id = id;
    this.vote = vote;
    this.time = time;
  }

  public boolean checkEquals(MoodEntry obj) {
    return this.id == obj.id && this.vote == obj.vote && this.time == obj.time;
  }
}
