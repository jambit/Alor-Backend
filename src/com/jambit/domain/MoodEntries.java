package com.jambit.domain;

public class MoodEntries {
  public int id;
  public int vote;
  public long time;

  public MoodEntries(int id, int vote, long time) {
    this.id = id;
    this.vote = vote;
    this.time = time;
  }

  public boolean checkEquals(MoodEntries obj) {
    if (this.id != obj.id || this.vote != obj.vote || this.time != obj.time) {
      return false;
    } else {
      return true;
    }
  }
}
