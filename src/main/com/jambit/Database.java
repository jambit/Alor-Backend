package com.jambit;

import java.sql.*;

public class Database {
  private static final String HOST = "azubipi1.client.jambit.com";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "123";
  private static final String DATABASE = "new_schema";
  private static final int PORT = 3306;

  private Connection con;

  public Database() {
    connect();
  }

  private void connect() {
    try {
      con =
          DriverManager.getConnection(
              "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void basicRead(String table) {
    try {
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("select * from " + table);
      while (rs.next()) {
        System.out.println(rs.getInt(1) + "  " + rs.getInt(2) + "  " + rs.getString(3));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void basicWrite(String table, String columns, String data) {
    try {
      Statement st = con.createStatement();
      st.executeUpdate("INSERT INTO " + table + "(" + columns + ") VALUES (" + data + ")");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
