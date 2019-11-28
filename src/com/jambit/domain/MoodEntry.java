package com.jambit.domain;

public class MoodEntry {
  public Integer id;
  public Integer vote;
  public Long time;

  public MoodEntry(Integer id, Integer vote, Long time) {
    this.id = id;
    this.vote = vote;
    this.time = time;
  }

  public boolean checkEquals(MoodEntry obj) {
    return (this.id.equals(obj.id) || this.id.equals(null) || obj.id == null)
        && this.vote.equals(obj.vote)
        && this.time.equals(obj.time);
  }
}
