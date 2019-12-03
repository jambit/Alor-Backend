package com.jambit.domain;

public class MoodEntry {

  private Integer id;
  private Integer vote;
  private Long time;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getVote() {
    return vote;
  }

  public void setVote(Integer vote) {
    this.vote = vote;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public MoodEntry() {}

  public MoodEntry(Integer vote) {
    this.vote = vote;
  }

  public boolean checkEquals(MoodEntry obj) {
    return (this.id == obj.id || this.id == null || obj.id == null)
        && this.vote == obj.vote
        && this.time == obj.time;
  }
}
