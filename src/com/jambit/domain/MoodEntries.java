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
}
