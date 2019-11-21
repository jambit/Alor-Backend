package com.jambit.domain;

public class MoodMeterDomain {
  private int id;
  private int vote;
  private long time;

  public MoodMeterDomain(int id, int vote, int time) {
    this.id = id;
    this.vote = vote;
    this.time = time;
  }
}
