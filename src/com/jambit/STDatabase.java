package com.jambit;

import java.io.IOException;
import java.sql.SQLException;

public class STDatabase {
  private static Database database = null;

  private STDatabase() {}

  public static Database getInstance()
      throws Database.MultipleConnectionException, IOException, SQLException,
          Database.TimeOutException {
    if (database == null) {
      database = new Database();
      database.connect();
    } else {
      database.checkValid();
    }
    return database;
  }
}
