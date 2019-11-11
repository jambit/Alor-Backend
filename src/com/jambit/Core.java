package com.jambit;

public class Core {
  private DataBaseIO dataBaseIO;
  private SoundInput soundInput;

  /** */
  public Core() {
    dataBaseIO = new DataBaseIO();
    soundInput = new SoundInput();
  }

  /** */
  public void run() {}
}
