package com.jambit.domain;

public class ArrivalEntity {
  public Integer id;
  public String name;
  public Long time;

  public ArrivalEntity(Integer id, String name, Long time) {
    this.id = id;
    this.name = name;
    this.time = time;
  }

  public boolean checkEquals(ArrivalEntity obj) {
    return (this.id == obj.id || this.id == null || obj.id == null)
        && this.name.equals(obj.name)
        && this.time == obj.time;
  }
}
